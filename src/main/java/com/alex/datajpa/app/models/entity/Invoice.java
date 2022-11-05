package com.alex.datajpa.app.models.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "invoices")   // name se puede omitir si la clase lleva el mismo name en singular
public class Invoice implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // autoincremental
    private Long id;

    @Temporal(TemporalType.DATE)    // es temporal, maneja fecha 
    @Column(name = "created_at")    // nombre q tiene en DB esta column
    private Date createdAt;

    // Estas campos se mapean de forma automtica xq asi se llaman en la tabla
    private String observation;
    @NotEmpty
    private String description;


    

    // muchas facturas(many) 1 client(one) -- fetch lazy: solo cuando se le llama con getClient se realiza la consulta del cliente de la factura, asi no sobrecargamos la DB con consultas innecesarias en primera instancia
    @ManyToOne(fetch = FetchType.LAZY)
    private Client client;

    // 1 factura muchos items -- cascade: si eliminamos 1 factura, q elimine a todos sus hijos InvoiceItem
    // JoinColum() crea la foreing key q las relaciona, se usa xq la relacion NO es en ambos sentidos. Invoice tiene InvoiceItem como attribute, but IncoiceItem NO tiene Invoice como attribute    <--   Relacion unidireccional
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "invoice_id")    // foreing key en la tabal InvoiceItems q las relaciona y es invoice_id
    private List<InvoiceItem> items;



    // constructor
    public Invoice () {
        this.items = new ArrayList<>();
    }

  
    public void addInvoiceItem(InvoiceItem item) {
        this.items.add(item);
    }


    public Double getTotal() {
        Double total = 0.0;
        int size = items.size();    // cantidad de items

        for (int i = 0; i < size; i++) {
            total += items.get(i).calculateAmount();
        }

        return total;
    }


    // setters and getters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public List<InvoiceItem> getInvoiceItem() {
        return items;
    }

    public void setInvoiceItem(List<InvoiceItem> invoiceItem) {
        this.items = invoiceItem;
    }

    public List<InvoiceItem> getItems() {
        return items;
    }
    public void setItems(List<InvoiceItem> items) {
        this.items = items;
    }


    @PrePersist     // antes de persistir la factura asigna la fecha
    public void prePersist() {
        createdAt = new Date();
    }


    // Eclipse pide esto, pero vscode no lo sugiere
    private static final long serialVersionUID = 1L;

}
