package com.appshop.jesus.appshop.productos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.appshop.jesus.appshop.productos.models.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    //  métodos personalizados aquí si es necesario
}