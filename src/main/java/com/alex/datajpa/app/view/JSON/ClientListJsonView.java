package com.alex.datajpa.app.view.JSON;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.alex.datajpa.app.models.entity.Client;



@Component("listar.json")   // listar del ver() controller
public class ClientListJsonView extends MappingJackson2JsonView {


    @Override
    @SuppressWarnings("unchecked")  // IDE no es capaz de validar el casteo
    protected Object filterModel(Map<String, Object> model) {
        
        model.remove("title");
        model.remove("page");

        // Serializar solo los clients y NO la data de Paginable
        Page<Client> clients = (Page<Client>) model.get("clients"); // tal cual en el controller
        model.remove("clients");
        model.put("clients", clients.getContent());


        return super.filterModel(model);
    }

}
