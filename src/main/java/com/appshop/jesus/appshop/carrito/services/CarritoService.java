package com.appshop.jesus.appshop.carrito.services;


import com.appshop.jesus.appshop.carrito.dtos.CarritoDTO;
import com.appshop.jesus.appshop.carrito.dtos.ItemCarritoDTO;
import com.appshop.jesus.appshop.carrito.models.Carrito;
import com.appshop.jesus.appshop.carrito.models.EstadoCarrito;
import com.appshop.jesus.appshop.carrito.models.ItemCarrito;
import com.appshop.jesus.appshop.carrito.repositories.CarritoRepository;
import com.appshop.jesus.appshop.carrito.repositories.ItemCarritoRepository;
import com.appshop.jesus.appshop.productos.models.Producto;
import com.appshop.jesus.appshop.productos.repositories.ProductoRepository;
import com.appshop.jesus.appshop.shared.exceptions.ResourceNotFoundException;
import com.appshop.jesus.appshop.usuarios.models.Usuario;
import com.appshop.jesus.appshop.usuarios.repositories.UsuarioRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CarritoService {

    private final CarritoRepository carritoRepository;
    private final ItemCarritoRepository itemCarritoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;
    private final ModelMapper modelMapper;

    public CarritoService(CarritoRepository carritoRepository, ItemCarritoRepository itemCarritoRepository, UsuarioRepository usuarioRepository, ProductoRepository productoRepository, ModelMapper modelMapper) {
        this.carritoRepository = carritoRepository;
        this.itemCarritoRepository = itemCarritoRepository;
        this.usuarioRepository = usuarioRepository;
        this.productoRepository = productoRepository;
        this.modelMapper = modelMapper;
    }

    public CarritoDTO agregarProductoAlCarrito(String username, ItemCarritoDTO itemCarritoDto) {
        Usuario usuario = usuarioRepository.findByUsername(username).orElse(null);

        if (usuario == null) {
            return null;
        }

        Carrito carrito = carritoRepository.findFirstByUsuarioId(usuario.getId()).orElseGet(() -> {
            Carrito nuevoCarrito = new Carrito();
            nuevoCarrito.setUsuario(usuario);
            nuevoCarrito.setFecha(System.currentTimeMillis());
            nuevoCarrito.setIdOrden(UUID.randomUUID().toString());
            nuevoCarrito.setEstado(EstadoCarrito.ACTIVO);
            return carritoRepository.save(nuevoCarrito);
        });
        if(carrito.getFecha() <= 0) {
            carrito.setFecha(System.currentTimeMillis());
        }
        if(carrito.getIdOrden() == null) {
            carrito.setIdOrden(UUID.randomUUID().toString());
        }

        Producto producto = productoRepository.findById(itemCarritoDto.getProductoId()).orElse(null);

        if (producto == null) {
            return  modelMapper.map(carrito, CarritoDTO.class);
        }

        ItemCarrito newItem = new ItemCarrito();
        newItem.setProducto(producto);
        newItem.setCantidad(itemCarritoDto.getCantidad());
        newItem.setCarrito(carrito);

        boolean productoExistente = false;
        for (ItemCarrito item : carrito.getItems()) {
            if (item.getProducto().getId().equals(producto.getId())) {
                item.setCantidad(item.getCantidad() + itemCarritoDto.getCantidad());
                productoExistente = true;
                break;
            }
        }

        if (!productoExistente) {
            carrito.getItems().add(newItem);
        }

        double totalCarrito = carrito.getItems().stream().mapToDouble(item -> item.getProducto().getPrecio() * item.getCantidad()).sum();
        carrito.setEstado(EstadoCarrito.ACTIVO);
        carrito.setValor(totalCarrito);
        carritoRepository.save(carrito);

        return  modelMapper.map(carrito, CarritoDTO.class);
   }
    private ItemCarritoDTO convertToDto(ItemCarrito itemCarrito) {
        return modelMapper.map(itemCarrito, ItemCarritoDTO.class);
    }

    public CarritoDTO eliminarProductoDelCarrito(String username, Long productoId) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con username: " + username));
        Carrito carrito = carritoRepository.findFirstByUsuarioAndEstado(usuario, EstadoCarrito.ACTIVO)
                .orElseThrow(() -> new ResourceNotFoundException("Carrito activo no encontrado para el usuario con id: " + username));


        ItemCarrito itemCarrito = itemCarritoRepository.findByCarritoAndProducto_Id(carrito, productoId)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado en el carrito"));
        itemCarritoRepository.delete(itemCarrito);

        return modelMapper.map(carrito, CarritoDTO.class);
    }

    public CarritoDTO actualizarCantidadProductoEnCarrito(String username, ItemCarritoDTO itemCarritoDTO) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + username));
        Carrito carrito = carritoRepository.findFirstByUsuarioAndEstado(usuario, EstadoCarrito.ACTIVO)
                .orElseThrow(() -> new ResourceNotFoundException("Carrito activo no encontrado para el usuario con id: " + username));

        ItemCarrito itemCarrito = itemCarritoRepository.findByCarritoAndProducto_Id(carrito, itemCarritoDTO.getProductoId())
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado en el carrito"));
        itemCarrito.setCantidad(itemCarritoDTO.getCantidad());
        itemCarritoRepository.save(itemCarrito);

        return modelMapper.map(carrito, CarritoDTO.class);
    }

    public CarritoDTO vaciarCarrito(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con username: " + username));
        Carrito carrito = carritoRepository.findFirstByUsuarioAndEstado(usuario, EstadoCarrito.ACTIVO)
                .orElseThrow(() -> new ResourceNotFoundException("Carrito activo no encontrado para el usuario con id: " + username));

        itemCarritoRepository.deleteAll(carrito.getItems());
        carrito.getItems().clear();
        carritoRepository.save(carrito);

        return modelMapper.map(carrito, CarritoDTO.class);
    }

    public CarritoDTO obtenerCarritoPorUsuario(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con username: " + username));

        Carrito carrito = carritoRepository.findFirstByUsuarioAndEstado(usuario, EstadoCarrito.ACTIVO)
                .orElseThrow(() -> new ResourceNotFoundException("Carrito activo no encontrado para el usuario con id: " + username));

        return modelMapper.map(carrito, CarritoDTO.class);
    }
    public List<CarritoDTO> obtenerHistorialPedidos(String username, EstadoCarrito estado) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con username: " + username));

        List<Carrito> carritos;
        if (estado != null) {
            carritos = carritoRepository.findByUsuarioAndEstado(usuario, estado);
        } else {
            carritos = carritoRepository.findByUsuario(usuario);
        }

        return carritos.stream()
                .map(carrito -> modelMapper.map(carrito, CarritoDTO.class))
                .collect(Collectors.toList());
    }

    public void finalizarCompra(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username).orElse(null);

        if (usuario == null) {
            throw new RuntimeException("Usuario no encontrado");
        }

        Carrito carrito = carritoRepository.findFirstByUsuarioId(usuario.getId()).orElse(null);

        if (carrito == null) {
            throw new RuntimeException("Carrito no encontrado");
        }

        carrito.setEstado(EstadoCarrito.COMPRADO); // Actualiza el estado del carrito
        carrito.setFecha(System.currentTimeMillis()); // Establece la fecha de compra
        carritoRepository.save(carrito);


    }
}
