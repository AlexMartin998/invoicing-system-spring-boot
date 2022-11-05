package com.alex.datajpa.app.models.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.alex.datajpa.app.models.entity.Client;



// Implementar el CrudRepository de Spring Boot -- NO hace falta Annotarlo con nada, ya es un bean xq extends
// public interface IClientDao extends CrudRepository<Client, Long>{   // Long x el ID de Client q es el 1er attribute - Methods predefinidos
public interface IClientDao extends PagingAndSortingRepository<Client, Long>{  // Hereda de CrudRepository y permite paginar


  // JOIN FETCH = INNER JOIN  -- LEFT ,si no tiene facturas hace la conslta de clients y al otro le coloca null
  @Query("SELECT c FROM Client c LEFT JOIN FETCH c.invoices i WHERE c.id = ?1")
  public Client fetchByIdWithInvoices(Long id);
  
  

/* CUando NO usamos CrudRepository de Spring Boot
  public List<Client> getAll();

  public Client getById(Long id);

  public void save(Client client);

  public void delete(Long id);
 */
}
