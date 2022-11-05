package com.alex.datajpa.app.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.alex.datajpa.app.models.entity.Product;

public interface IProductDao extends CrudRepository<Product, Long> { // long x el ID de la clase Product.java

    // implemente operaciones crud x default 

    
    // // autocomplete
    // hacer la consulta para el autocomplete con JPA-JPLQ
    @Query("SELECT p FROM Product p WHERE p.name LIKE %?1%")   // JPLQ - like sea igual a un parametro term
    public List<Product> getByName(String term);

    // Spring Data traduce el nombre del method a una query 
    public List<Product> findByNameLikeIgnoreCase(String term);
}
