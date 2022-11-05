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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;



// // // Mapea las tablas a objects

@Entity
@Table(name = "clients")    // opt: darle el nombre exacto de la tabla en DB
public class Client implements Serializable {

  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY) // id en DB es AutoIncremental
  private Long id;

  
  // Estas campos se mapean de forma automtica xq asi se llaman en la tabla
  @NotEmpty
  private String name;

  @NotEmpty
  private String lastname;

  @NotEmpty
  @Email
  private String email;


  @NotNull
  @Column(name = "created_at")   // nombre q tiene en DB esta column
  @Temporal(TemporalType.DATE)  // es temporal, maneja fecha
  @DateTimeFormat(pattern = "yyyy-MM-dd")   // formato del date picker de html
  private Date createdAt;

  
  private String photo;



  // Relacion con Factura 1 cliente tiene muchas facturas/invoices   --  fetch: asi no llama a todas las facturas de cada cliente, solo se realiza la consulta a DB con el method getInvoice.   --  cascade: si el cliente se persiste lo va a hacer con todos sus hijos, asi mismo, si se elimina un cliente, se eliminan sus hijos, es decir, sus facturas (elimina los relacionados con foreing key)   --   mappendBy: el mismo attribute del Invoice q se debe mapear <- crea la foreing key client_id en la tabla factura DB
  // mappendBy indica relacion en ambos sentidos xq client tiene factura y factura tiene client como attribute <- Bidireccional
  @OneToMany(mappedBy = "client", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private List<Invoice> invoices;



  // Eclipse pide esto, pero vscode no lo sugiere
  private static final long serialVersionUID = 1L;




  // // Constructor
  public Client() {
    this.invoices = new ArrayList<>();
  }


  // @PrePersist   // se llama antes de persistir en DB
  // public void prePersist() {
  //   createdAt = new Date();
  // }



  // getters and setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getLastname() {
    return lastname;
  }

  public void setLastname(String lastname) {
    this.lastname = lastname;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  public static long getSerialversionuid() {
    return serialVersionUID;
  }

  public String getPhoto() {
    return photo;
  }

  public void setPhoto(String photo) {
    this.photo = photo;
  }

  public List<Invoice> getInvoices() {
    return invoices;
  }

  public void setInvoices(List<Invoice> invoices) {
    this.invoices = invoices;
  }
  


  public void addInvoice(Invoice invoice) {
    invoices.add(invoice);
  }


  

  @Override
  public String toString() {
    return name.concat(" ").concat(lastname);
  }
  

  
}
