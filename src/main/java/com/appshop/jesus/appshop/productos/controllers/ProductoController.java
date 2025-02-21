package com.appshop.jesus.appshop.productos.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.appshop.jesus.appshop.productos.dtos.ProductoDTO;
import com.appshop.jesus.appshop.productos.services.ProductoService;

import java.util.List;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @PostMapping
    public ResponseEntity<ProductoDTO> crearProducto(@RequestBody ProductoDTO productoDTO) {
        ProductoDTO productoCreado = productoService.crearProducto(productoDTO);
        return new ResponseEntity<>(productoCreado, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> obtenerProductoPorId(@PathVariable Long id) {
        ProductoDTO producto = productoService.obtenerProductoPorId(id);
        return new ResponseEntity<>(producto, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ProductoDTO>> obtenerTodosLosProductos() {
        List<ProductoDTO> productos = productoService.obtenerTodosLosProductos();
        return new ResponseEntity<>(productos, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoDTO> actualizarProducto(@PathVariable Long id, @RequestBody ProductoDTO productoDTO) {
        ProductoDTO productoActualizado = productoService.actualizarProducto(id, productoDTO);
        return new ResponseEntity<>(productoActualizado, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        productoService.eliminarProducto(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
