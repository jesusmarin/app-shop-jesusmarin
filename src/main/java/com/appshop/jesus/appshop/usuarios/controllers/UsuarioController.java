package com.appshop.jesus.appshop.usuarios.controllers;

import com.appshop.jesus.appshop.usuarios.dtos.UsuarioDTO;
import com.appshop.jesus.appshop.usuarios.models.Usuario;
import com.appshop.jesus.appshop.usuarios.services.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> obtenerTodosLosUsuarios() {
        List<UsuarioDTO> usuarios = usuarioService.obtenerTodosLosUsuarios();
        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UsuarioDTO> crearUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        System.out.println("Usuario a crear: " + usuarioDTO.toString());
        UsuarioDTO usuarioCreado = usuarioService.crearUsuario(usuarioDTO);
        System.out.println("Usuario creado: " + usuarioCreado.toString());
        return new ResponseEntity<>(usuarioCreado, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> obtenerUsuarioPorId(@PathVariable Long id) {
        System.out.println("Usuario id: " + id);
        UsuarioDTO usuario = usuarioService.obtenerUsuarioPorId(id);
        if (usuario != null) {
            return new ResponseEntity<>(usuario, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @GetMapping("/u/{username}")
    public ResponseEntity<UsuarioDTO> obtenerUsuarioPorUsername(@PathVariable String username) {
        System.out.println("Usuario username: " + username);
        UsuarioDTO usuario = usuarioService.obtenerUsuarioPorUsername(username);
        if (usuario != null) {
            return new ResponseEntity<>(usuario, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> actualizarUsuario(@PathVariable Long id, @RequestBody UsuarioDTO usuarioDTO) {
        UsuarioDTO usuarioActualizado = usuarioService.actualizarUsuario(id, usuarioDTO);
        if (usuarioActualizado != null) {
            return new ResponseEntity<>(usuarioActualizado, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password) {

        String token = usuarioService.login(username, password);
        System.out.println("Token: " + token);
        if (token != null) {
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        // Invalida la sesión HTTP
        request.getSession().invalidate();

        // Elimina el token JWT del encabezado Authorization (opcional)
        response.setHeader("Authorization", "");

        return ResponseEntity.ok().build();
    }

    @PostMapping("/getId")
    public long registrarUsuario(@RequestBody Usuario usuario) {
        long idUsuario = usuarioService.obtenerUltimoId();
        return idUsuario;
    }
}
