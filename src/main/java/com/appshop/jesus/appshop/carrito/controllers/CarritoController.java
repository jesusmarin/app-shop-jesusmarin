package com.appshop.jesus.appshop.carrito.controllers;


import com.appshop.jesus.appshop.carrito.dtos.CarritoDTO;
import com.appshop.jesus.appshop.carrito.dtos.ItemCarritoDTO;
import com.appshop.jesus.appshop.carrito.models.EstadoCarrito;
import com.appshop.jesus.appshop.carrito.services.CarritoService;
import com.appshop.jesus.appshop.config.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carritos")
public class CarritoController {

    private final CarritoService carritoService;
    private final JwtUtil jwtUtil;

    public CarritoController(CarritoService carritoService, JwtUtil jwtUtil) {
        this.carritoService = carritoService;
        this.jwtUtil = jwtUtil;
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
    public ResponseEntity<List<CarritoDTO>> obtenerHistorialPedidos(@PathVariable String username, @RequestParam(required = false) String estado) {
        EstadoCarrito estadoCarrito = EstadoCarrito.ACTIVO;
        if(estado != null) {
            estadoCarrito =  EstadoCarrito.fromString(estado);
        }
        List<CarritoDTO> historico = carritoService.obtenerHistorialPedidos(username, estadoCarrito);
        return new ResponseEntity<>(historico, HttpStatus.OK);
    }

   /* @GetMapping("/{username}/historial")
    public ResponseEntity<List<CarritoDTO>> obtenerHistorialPedidos__(
            @PathVariable String username, HttpServletRequest request, @RequestParam(required = false) EstadoCarrito estado) {
        try {
            String token = extractToken(request);
            String usernameFromToken = jwtUtil.getUsernameFromToken(token);
            System.out.println("username for token: "+usernameFromToken+ " / username: "+username);
            if (!username.equals(usernameFromToken)) {
                return ResponseEntity.status(403).build(); // No autorizado
            }

            List<CarritoDTO> historial = carritoService.obtenerHistorialPedidos(username, estado);
            return ResponseEntity.ok(historial);
        } catch (Exception e) {
            return ResponseEntity.status(401).build(); // No autorizado o token inválido
        }
    }

    @GetMapping("/{username}/historial")
    public ResponseEntity<List<CarritoDTO>> obtenerHistorialCompras(
            @PathVariable String username, HttpServletRequest request, @RequestParam(required = false) String estado) {
              EstadoCarrito estadoCarrrito = EstadoCarrito.ACTIVO;
        try {
            String token = extractToken(request);
            if (token == null) {
                return ResponseEntity.status(401).build(); // No autorizado
            }

            if (!jwtUtil.validateToken(token, username)) {
                return ResponseEntity.status(403).build(); // No autorizado
            }
            if(estado != null) {
               estadoCarrrito =  EstadoCarrito.fromString(estado);
            }
            System.out.println("username for token: "+token+ " / username: "+username);
            List<CarritoDTO> historial = carritoService.obtenerHistorialPedidos(username, estadoCarrrito);
            return ResponseEntity.ok(historial);
        } catch (Exception e) {
            return ResponseEntity.status(401).build(); // No autorizado o token inválido
        }
    }*/

    private String extractToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7); // Elimina "Bearer "
        }
        return null;
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
