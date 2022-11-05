package com.alex.datajpa.app.models.dao;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.alex.datajpa.app.models.entity.Client;


// ================================================================
// // //  SE USA CUANDO NO SE USA EL CRUDREPOSITORY DE SPRING BOOT
// ================================================================


// @Repository("clientDaoJPA")   // anotarlo como repository para q pueda ser injec
@Repository()   // anotarlo como repository para q pueda ser injec
// public class ClientDaoImpl implements IClientDao {
public class ClientDaoImpl {


  @PersistenceContext // w con H2 x ahora
  private EntityManager em;


  // @Override
  public List<Client> getAll() {
    
    return em.createQuery("SELECT c FROM Client c").getResultList();
  }


  // @Override
  public Client getById(Long id) {
      
    return em.find(Client.class, id);
  }


  // @Override
  public void save(Client client) {
    if (client.getId() != null && client.getId() > 0) em.merge(client);     // update
    else em.persist(client);                                                // create
  }


  // @Override
  public void delete(Long id) {

    em.remove(getById(id));    
  }

  
}
