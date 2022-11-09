package com.alex.datajpa.app.view.xml;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.xml.MarshallingView;

import com.alex.datajpa.app.models.entity.Client;


@Component("listar.xml")    // listar x el controller ver() retorna listar
public class ClienteListXmlView extends MarshallingView {

    
    @Autowired
    public ClienteListXmlView(Jaxb2Marshaller marshaller) {
        super(marshaller);
    }

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
                
        // Solo necesitamos a los clients q se pasan al model en el controller, title/page NO
        model.remove("title");
        model.remove("page");

        @SuppressWarnings("unchecked")  // IDE no es capaz de validar el casteo
        Page<Client> clients = (Page<Client>) model.get("clients"); // tal cual en el controller
        model.remove("clients");


        //.getContent transforma el Page a List - aqui viene paginado, podriamso hacer otra consulta para traer todo
        model.put("listClient", new ClientList(clients.getContent()));


        super.renderMergedOutputModel(model, request, response);
    }
    
    

    
}
