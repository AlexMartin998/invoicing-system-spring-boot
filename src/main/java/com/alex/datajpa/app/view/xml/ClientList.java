package com.alex.datajpa.app.view.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.alex.datajpa.app.models.entity.Client;




@XmlRootElement(name = "clients")     // indica q es al clase root xml o Wrapper
public class ClientList {

    @XmlElement(name = "client")    // x la structura de XML
    public List<Client> clients;    // lis q convertiremos a XML



    // si creamos un constructor con args, nececitamos 1 empty
    public ClientList() {}

    public ClientList(List<Client> clients) {
        this.clients = clients;
    }

    public List<Client> getClients() {
        return clients;
    }


}
