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

import java.util.List;
import java.util.Optional;
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

    public CarritoDTO agregarProductoAlCarrito(Long usuarioId, ItemCarritoDTO itemCarritoDTO) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + usuarioId));
        Producto producto = productoRepository.findById(itemCarritoDTO.getProductoId())
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + itemCarritoDTO.getProductoId()));

        Carrito carrito = carritoRepository.findFirstByUsuarioAndEstado(usuario, EstadoCarrito.ACTIVO)
                .orElseGet(() -> {
                    Carrito nuevoCarrito = new Carrito();
                    nuevoCarrito.setUsuario(usuario);
                    nuevoCarrito.setEstado(EstadoCarrito.ACTIVO); // Establecer el estado a ACTIVO
                    return carritoRepository.save(nuevoCarrito);
                });

        ItemCarrito itemCarrito = modelMapper.map(itemCarritoDTO, ItemCarrito.class);
        itemCarrito.setCarrito(carrito);
        itemCarrito.setProducto(producto);
        itemCarritoRepository.save(itemCarrito);

        return modelMapper.map(carrito, CarritoDTO.class);
    }

    public CarritoDTO eliminarProductoDelCarrito(Long usuarioId, Long productoId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + usuarioId));
        Carrito carrito = carritoRepository.findFirstByUsuarioAndEstado(usuario, EstadoCarrito.ACTIVO)
                .orElseThrow(() -> new ResourceNotFoundException("Carrito activo no encontrado para el usuario con id: " + usuarioId));


        ItemCarrito itemCarrito = itemCarritoRepository.findByCarritoAndProducto_Id(carrito, productoId)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado en el carrito"));
        itemCarritoRepository.delete(itemCarrito);

        return modelMapper.map(carrito, CarritoDTO.class);
    }

    public CarritoDTO actualizarCantidadProductoEnCarrito(Long usuarioId, ItemCarritoDTO itemCarritoDTO) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + usuarioId));
        Carrito carrito = carritoRepository.findFirstByUsuarioAndEstado(usuario, EstadoCarrito.ACTIVO)
                .orElseThrow(() -> new ResourceNotFoundException("Carrito activo no encontrado para el usuario con id: " + usuarioId));

        ItemCarrito itemCarrito = itemCarritoRepository.findByCarritoAndProducto_Id(carrito, itemCarritoDTO.getProductoId())
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado en el carrito"));
        itemCarrito.setCantidad(itemCarritoDTO.getCantidad());
        itemCarritoRepository.save(itemCarrito);

        return modelMapper.map(carrito, CarritoDTO.class);
    }

    public CarritoDTO vaciarCarrito(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + usuarioId));
        Carrito carrito = carritoRepository.findFirstByUsuarioAndEstado(usuario, EstadoCarrito.ACTIVO)
                .orElseThrow(() -> new ResourceNotFoundException("Carrito activo no encontrado para el usuario con id: " + usuarioId));

        itemCarritoRepository.deleteAll(carrito.getItems());
        carrito.getItems().clear();
        carritoRepository.save(carrito);

        return modelMapper.map(carrito, CarritoDTO.class);
    }

    public CarritoDTO obtenerCarritoPorUsuario(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + usuarioId));

        Carrito carrito = carritoRepository.findFirstByUsuarioAndEstado(usuario, EstadoCarrito.ACTIVO)
                .orElseThrow(() -> new ResourceNotFoundException("Carrito activo no encontrado para el usuario con id: " + usuarioId));

        return modelMapper.map(carrito, CarritoDTO.class);
    }
    public List<CarritoDTO> obtenerHistorialPedidos(Long usuarioId, EstadoCarrito estado) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + usuarioId));

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
}
