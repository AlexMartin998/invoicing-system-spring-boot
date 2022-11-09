package com.alex.datajpa.app.models.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "invoice_items")
public class InvoiceItem implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer quantity;


    // muchos ItemFactura 1 producto
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")    // genera la foreing key product_id en InvoiceItem para relacionar con la tabla product
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})   // corregir el error al serializar a JSON (traer todas las prop)
    private Product product;



    
    // calcular importe: cantidad x precio producto
    public Double calculateAmount() {
        return quantity.doubleValue() * product.getPrice() ;
    }


    
    // setters and getters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

}
