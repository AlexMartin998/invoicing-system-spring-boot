package com.alex.datajpa.app.view.csv;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.AbstractView;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.alex.datajpa.app.models.entity.Client;


// si tiene mediaType/mineType (.csv) debemos estableccer el contentnegotiation q lo va a resolver en el    app.properties
@Component("listar.csv")    // listar lo retorna ver() del CLientController - Si solo hubiera 1 view no hace falta la extension csv
public class ClientCsvView extends AbstractView {  // nosotros creamos la View xq no hay 1 para csv

    // configuramos aqui el datatype/contentType xq no tiene su propia abstrac class q lo maneje
    public ClientCsvView() {
        setContentType("text/csv");
    }



    // Configurar la descarga del archivo - se hace a mano xq no tiene un abstrac csv q podamos usar
    @Override
    protected boolean generatesDownloadContent() {
        return true;
    } 
    


    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
                
        response.setHeader("Content-Disposition", "attachment; filename=\"clientes.csv\"");  // set filename
        response.setContentType(getContentType());  // set al contectype

        @SuppressWarnings("unchecked")  // IDE no es capaz de validar el casteo
        Page<Client> clients = (Page<Client>) model.get("clients");   // tal cual lo establecemos en el controller  <-  es 1 paginable en el controller

        // Necesitamos un beanWirter para escribir el csv
        ICsvBeanWriter beanWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

        String[] header = {"id", "name", "lastname", "email", "createdAt"};     // mismo name q los attributes de la class
        beanWriter.writeHeader(header);

        for (Client client : clients) {
            beanWriter.write(client, header);
        }

        beanWriter.close();



    }

    

}
