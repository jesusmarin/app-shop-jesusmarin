package com.appshop.jesus.appshop.productos.services;
import com.appshop.jesus.appshop.shared.exceptions.ResourceNotFoundException;
import com.appshop.jesus.appshop.productos.dtos.ProductoDTO;
import com.appshop.jesus.appshop.productos.models.Producto;
import com.appshop.jesus.appshop.productos.repositories.ProductoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final ModelMapper modelMapper;

    public ProductoService(ProductoRepository productoRepository, ModelMapper modelMapper) {
        this.productoRepository = productoRepository;
        this.modelMapper = modelMapper;
    }

    public ProductoDTO crearProducto(ProductoDTO productoDTO) {
        Producto producto = modelMapper.map(productoDTO, Producto.class);
        producto = productoRepository.save(producto);
        return modelMapper.map(producto, ProductoDTO.class);
    }

    public ProductoDTO obtenerProductoPorId(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
        return modelMapper.map(producto, ProductoDTO.class);
    }

    public List<ProductoDTO> obtenerTodosLosProductos() {
        List<Producto> productos = productoRepository.findAll();
        return productos.stream()
                .map(producto -> modelMapper.map(producto, ProductoDTO.class))
                .collect(Collectors.toList());
    }

    public ProductoDTO actualizarProducto(Long id, ProductoDTO productoDTO) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
        modelMapper.map(productoDTO, producto);
        producto = productoRepository.save(producto);
        return modelMapper.map(producto, ProductoDTO.class);
    }

    public void eliminarProducto(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
        productoRepository.delete(producto);
    }
}