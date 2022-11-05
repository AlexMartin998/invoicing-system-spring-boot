package com.alex.datajpa.app.controllers;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alex.datajpa.app.models.entity.Client;
import com.alex.datajpa.app.models.entity.Invoice;
import com.alex.datajpa.app.models.entity.InvoiceItem;
import com.alex.datajpa.app.models.entity.Product;
import com.alex.datajpa.app.models.services.IClientService;


// @Secured("ROLE_ADMIN")    // se aplica a todos los handlers de este controller  -- Me da error al protegerla asi
// @PreAuthorize("hasRole('ROLE_ADMIN')")       // igual me da error - lo mismo q el @Secured al usarlo en toda la class
@Controller
@RequestMapping("/invoice")
@SessionAttributes("invoice") // mantener en session y pasar a la view hasta q se persista la factura
public class InvoiceController {

    @Autowired
    private IClientService clientService;

    // logs
    private final Logger log = LoggerFactory.getLogger(getClass());


    @GetMapping("/ver/{id}")
    private String ver(@PathVariable Long id, Model model, RedirectAttributes flash) {
        // 1 sola consulta para 1 entity con muchas relaciones
        Invoice invoice = clientService.getClientWithInvoiceItemWithProductById(id);
        if (invoice == null) {
            flash.addAttribute("error", "Invoice does not exist in DB!");
            return "redirect:/listar";
        }

        model.addAttribute("invoice", invoice);
        model.addAttribute("title", "Invoice : ".concat(invoice.getDescription()));
        
        return "invoice/ver";
    }



    // enviar el form para crear
    @GetMapping("/form/{clientId}")
    public String create(@PathVariable Long clientId, Model model, RedirectAttributes flash) {
        Client client = clientService.getById(clientId);
        if (client == null) {
            flash.addFlashAttribute("error", "Client not found!");
            return "redirect:/listar";
        }
        model.addAttribute("title", "Add new invoice");

        // asignamos la factura con 1 client
        Invoice invoice = new Invoice();
        invoice.setClient(client);

        model.addAttribute("invoice", invoice);

        return "invoice/form";
    }


    // @ResposeBody omite enviar una view de thymeleaf y pueba en el body de la response lo q retornamos    -  transforma la salida a JSON y la guarda en el body de la response
    @GetMapping(value = "/load-products/{term}", produces = {"application/json"})   // produce una respuesta JSON
    public @ResponseBody List<Product> loadProducts(@PathVariable String term) {

        return clientService.getByName(term);
    }


    // en la view no tenemos campos mapeados directamente a Invoice  -  Valid y BindingResult para las validaciones
    @PostMapping("/form")
    public String save(@Valid Invoice invoice, BindingResult result,
            @RequestParam(name = "item_id[]", required = false) Long[] itemId, Model model,
            @RequestParam(name = "quantity[]", required = false) Integer[] quantity, RedirectAttributes flash,
            SessionStatus status) {

        if (result.hasErrors()) {
            model.addAttribute("title", "Form user");
            return "invoice/form"; // retornamos la vista del form - los errors se pasa en auto
        }
        if (itemId == null || itemId.length <= 0) {
            model.addAttribute("title", "Form user");
            model.addAttribute("error", "Error: Lines are required in an invoice!");
            return "invoice/form"; // retornamos la vista del form - los errors se pasa en auto
        }

        for (int i = 0; i < itemId.length; i++) {
            Product product = clientService.findProductById(itemId[i]);

            InvoiceItem linea = new InvoiceItem();
            linea.setQuantity(quantity[i]);
            linea.setProduct(product);

            invoice.addInvoiceItem(linea);

            log.info("ID" + itemId[i].toString() + ". quantity: " + quantity);
        }

        // guardar en DB
        clientService.saveInvoice(invoice);

        status.setComplete(); // eliminamos el invoice de la session
        flash.addFlashAttribute("success", "Invoice successfully created!");

        return "redirect:/ver/" + invoice.getClient().getId();
    }


    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes flash) {
        Invoice invoice = clientService.findInvoiceById(id);
        if (invoice == null) {
            flash.addFlashAttribute("error", "Invalid Invoice!");
            return "redirect:/listar";
        }

        clientService.deleteInvoice(id);
        flash.addFlashAttribute("success", "Invoice succssesfully deleted!");

        return "redirect:/ver/" + invoice.getClient().getId();
    }

}
