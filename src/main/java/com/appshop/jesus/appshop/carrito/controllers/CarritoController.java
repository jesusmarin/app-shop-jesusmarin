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

    @PostMapping("/{username}/productos")
    public ResponseEntity<CarritoDTO> agregarProductoAlCarrito(@PathVariable String username, @RequestBody ItemCarritoDTO itemCarritoDTO) {
        CarritoDTO carrito = carritoService.agregarProductoAlCarrito(username, itemCarritoDTO);
        return new ResponseEntity<>(carrito, HttpStatus.OK);
    }

    @DeleteMapping("/{username}/productos/{productoId}")
    public ResponseEntity<CarritoDTO> eliminarProductoDelCarrito(@PathVariable String username, @PathVariable Long productoId) {
        CarritoDTO carrito = carritoService.eliminarProductoDelCarrito(username, productoId);
        return new ResponseEntity<>(carrito, HttpStatus.OK);
    }

    @PutMapping("/{username}/productos")
    public ResponseEntity<CarritoDTO> actualizarCantidadProductoEnCarrito(@PathVariable String username, @RequestBody ItemCarritoDTO itemCarritoDTO) {
        CarritoDTO carrito = carritoService.actualizarCantidadProductoEnCarrito(username, itemCarritoDTO);
        return new ResponseEntity<>(carrito, HttpStatus.OK);
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<CarritoDTO> vaciarCarrito(@PathVariable String username) {
        CarritoDTO carrito = carritoService.vaciarCarrito(username);
        return new ResponseEntity<>(carrito, HttpStatus.OK);
    }


    @GetMapping("/{username}")
    public ResponseEntity<CarritoDTO> obtenerCarritoPorUsuario(@PathVariable String username) {
        CarritoDTO carrito = carritoService.obtenerCarritoPorUsuario(username);
        return new ResponseEntity<>(carrito, HttpStatus.OK);
    }

    @GetMapping("/{username}/historial")
    public ResponseEntity<List<CarritoDTO>> obtenerHistorialPedidos(@PathVariable String username, @RequestParam(required = false) EstadoCarrito estado) {
        List<CarritoDTO> historico = carritoService.obtenerHistorialPedidos(username, estado);
        return new ResponseEntity<>(historico, HttpStatus.OK);
    }
    @PostMapping("/{username}/finalizar")
    public ResponseEntity<Void> finalizarCompra(@PathVariable String username) {
        try {
            carritoService.finalizarCompra(username);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build(); // Devuelve un error 400 si hay un problema
        }
    }
}
