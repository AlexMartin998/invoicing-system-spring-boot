package com.alex.datajpa.app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alex.datajpa.app.models.entity.Client;
import com.alex.datajpa.app.models.services.IClientService;



@RestController
@RequestMapping("/api/clients")
public class ClientRestController {
    
    @Autowired
    private IClientService clientService;
    


    @GetMapping("/listar")
    public List<Client> listar() {
        return clientService.getAll();
    }

}
