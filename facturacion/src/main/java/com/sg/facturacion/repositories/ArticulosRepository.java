package com.sg.facturacion.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sg.facturacion.models.Articulos;

@Repository
public interface ArticulosRepository extends JpaRepository<Articulos, Integer> {

}
