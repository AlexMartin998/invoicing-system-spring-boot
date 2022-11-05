package com.alex.datajpa.app.models.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.alex.datajpa.app.models.entity.Invoice;



public interface IInvoiceDao extends CrudRepository<Invoice, Long> {
    
    
   // 1 sola consulta a db sin LAZY para el datails de la factura
   @Query("SELECT i FROM Invoice i JOIN FETCH i.client c JOIN FETCH i.items l JOIN FETCH l.product WHERE i.id=?1")
   public Invoice getAllDataOfClientById(Long id);

}
