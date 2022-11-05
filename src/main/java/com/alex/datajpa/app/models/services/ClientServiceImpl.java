package com.alex.datajpa.app.models.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alex.datajpa.app.models.dao.IClientDao;
import com.alex.datajpa.app.models.dao.IInvoiceDao;
import com.alex.datajpa.app.models.dao.IProductDao;
import com.alex.datajpa.app.models.entity.Client;
import com.alex.datajpa.app.models.entity.Invoice;
import com.alex.datajpa.app.models.entity.Product;


@Service
public class ClientServiceImpl implements IClientService{

    // podemos tener varios daos
    @Autowired
    private IClientDao clientDao;

    @Autowired  // se puede inject xq extiende de CrudRepository y ya es un bean
    private IProductDao productDao;

    @Autowired
    private IInvoiceDao invoiceDao;




    // Nombres de los methods son los q da CrudRepository: findAll, findById
    @Override
    @Transactional(readOnly = true)   // solo es de lectura, no modifica nada en db
    public List<Client> getAll() {
        return (List<Client>) clientDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)   // solo es de lectura, no modifica nada en db
    public Client getById(Long id) {
        return clientDao.findById(id).orElse(null);
    }

    @Override
    @Transactional    // es de escritura
    public void save(Client client) {
        clientDao.save(client);
    }

    @Override
    @Transactional    // es de escritura
    public void delete(Long id) {
        clientDao.deleteById(id);
    }

    

    // Paginador
    @Transactional(readOnly = true)
    @Override
    public Page<Client> getAll(Pageable pageable) {
        
        return clientDao.findAll(pageable);
    }

    
    // buscador de productos en factura
    @Transactional(readOnly = true)
    @Override
    public List<Product> getByName(String term) {
        
        // return productDao.getByName(term);   // con la query escrita en JPLQ
        return productDao.findByNameLikeIgnoreCase("%" + term + "%");  // % necesarios para el LIKE
    }


    // factura
    @Override
    @Transactional
    public void saveInvoice(Invoice invoice) {
        invoiceDao.save(invoice);
    }


    // 
    @Override
    @Transactional(readOnly = true)
    public Product findProductById(Long id) {
        return productDao.findById(id).orElse(null);
    }


    @Override
    @Transactional(readOnly = true)
    public Invoice findInvoiceById(Long id) {
        return invoiceDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void deleteInvoice(Long id) {
        invoiceDao.deleteById(id);
    }

   

    // Mejorar las consultas a DB cuando trae toda la info de las tablas relacionadas, y no 1x1 con LAZY
    @Override
    @Transactional
    public Invoice getClientWithInvoiceItemWithProductById(Long id) {
        return invoiceDao.getAllDataOfClientById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Client fetchByIdWithInvoices(Long id) {
        return clientDao.fetchByIdWithInvoices(id);
    }

    
}
