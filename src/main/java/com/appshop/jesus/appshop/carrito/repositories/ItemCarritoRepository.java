package com.appshop.jesus.appshop.carrito.repositories;

import com.appshop.jesus.appshop.carrito.models.Carrito;
import com.appshop.jesus.appshop.carrito.models.ItemCarrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemCarritoRepository extends JpaRepository<ItemCarrito, Long> {

    @Query("SELECT i FROM ItemCarrito i WHERE i.carrito = :carrito AND i.producto.id = :productoId")
    Optional<ItemCarrito> findByCarritoAndProducto_Id(@Param("carrito") Carrito carrito, @Param("productoId") Long productoId);
}
