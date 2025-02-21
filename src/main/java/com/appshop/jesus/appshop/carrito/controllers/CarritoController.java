package com.appshop.jesus.appshop.carrito.controllers;


import com.appshop.jesus.appshop.carrito.dtos.CarritoDTO;
import com.appshop.jesus.appshop.carrito.dtos.ItemCarritoDTO;
import com.appshop.jesus.appshop.carrito.models.EstadoCarrito;
import com.appshop.jesus.appshop.carrito.services.CarritoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carritos")
public class CarritoController {

    private final CarritoService carritoService;

    public CarritoController(CarritoService carritoService) {
        this.carritoService = carritoService;
    }

    @PostMapping("/{usuarioId}/productos")
    public ResponseEntity<CarritoDTO> agregarProductoAlCarrito(@PathVariable Long usuarioId, @RequestBody ItemCarritoDTO itemCarritoDTO) {
        CarritoDTO carrito = carritoService.agregarProductoAlCarrito(usuarioId, itemCarritoDTO);
        return new ResponseEntity<>(carrito, HttpStatus.OK);
    }

    @DeleteMapping("/{usuarioId}/productos/{productoId}")
    public ResponseEntity<CarritoDTO> eliminarProductoDelCarrito(@PathVariable Long usuarioId, @PathVariable Long productoId) {
        CarritoDTO carrito = carritoService.eliminarProductoDelCarrito(usuarioId, productoId);
        return new ResponseEntity<>(carrito, HttpStatus.OK);
    }

    @PutMapping("/{usuarioId}/productos")
    public ResponseEntity<CarritoDTO> actualizarCantidadProductoEnCarrito(@PathVariable Long usuarioId, @RequestBody ItemCarritoDTO itemCarritoDTO) {
        CarritoDTO carrito = carritoService.actualizarCantidadProductoEnCarrito(usuarioId, itemCarritoDTO);
        return new ResponseEntity<>(carrito, HttpStatus.OK);
    }

    @DeleteMapping("/{usuarioId}")
    public ResponseEntity<CarritoDTO> vaciarCarrito(@PathVariable Long usuarioId) {
        CarritoDTO carrito = carritoService.vaciarCarrito(usuarioId);
        return new ResponseEntity<>(carrito, HttpStatus.OK);
    }


    @GetMapping("/{usuarioId}")
    public ResponseEntity<CarritoDTO> obtenerCarritoPorUsuario(@PathVariable Long usuarioId) {
        CarritoDTO carrito = carritoService.obtenerCarritoPorUsuario(usuarioId);
        return new ResponseEntity<>(carrito, HttpStatus.OK);
    }

    @GetMapping("/{usuarioId}/historial")
    public ResponseEntity<List<CarritoDTO>> obtenerHistorialPedidos(@PathVariable Long usuarioId, @RequestParam(required = false) EstadoCarrito estado) {
        List<CarritoDTO> historico = carritoService.obtenerHistorialPedidos(usuarioId, estado);
        return new ResponseEntity<>(historico, HttpStatus.OK);
    }
}
