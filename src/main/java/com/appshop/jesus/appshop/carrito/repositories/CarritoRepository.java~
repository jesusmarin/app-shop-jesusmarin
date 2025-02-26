package com.appshop.jesus.appshop.carrito.repositories;


import com.appshop.jesus.appshop.carrito.models.Carrito;
import com.appshop.jesus.appshop.carrito.models.EstadoCarrito;
import com.appshop.jesus.appshop.usuarios.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarritoRepository extends JpaRepository<Carrito, Long> {

    List<Carrito> findByUsuario(Usuario  usuario);

    Optional<Carrito> findFirstByUsuarioAndEstado(Usuario usuario, EstadoCarrito estado);

    List<Carrito> findByUsuarioAndEstado(Usuario usuario, EstadoCarrito estado);
}
