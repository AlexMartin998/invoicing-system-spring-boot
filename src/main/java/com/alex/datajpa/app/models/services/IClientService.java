package com.alex.datajpa.app.models.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.alex.datajpa.app.models.entity.Client;
import com.alex.datajpa.app.models.entity.Invoice;
import com.alex.datajpa.app.models.entity.Product;

public interface IClientService {
    public List<Client> getAll();

    // Paginar - de springframwork.data.domain
    public Page<Client> getAll(Pageable pageable); // page tb es iterable

    public Client getById(Long id);

    public void save(Client client);

    public Client fetchByIdWithInvoices(Long id);

    public void delete(Long id);

    // search producto
    public List<Product> getByName(String term);

    // find product
    public Product findProductById(Long id);

    // invoice
    public void saveInvoice(Invoice invoice);

    public Invoice findInvoiceById(Long id);

    public void deleteInvoice(Long id);


    // mejorar la query a db con JOIN
    public Invoice getClientWithInvoiceItemWithProductById(Long id);


}
