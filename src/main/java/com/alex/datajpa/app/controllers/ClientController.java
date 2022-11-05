package com.alex.datajpa.app.controllers;

import java.io.IOException;
import java.net.MalformedURLException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alex.datajpa.app.models.entity.Client;
import com.alex.datajpa.app.models.services.IClientService;
import com.alex.datajpa.app.models.services.IUploadFileService;
import com.alex.datajpa.app.util.paginator.PageRender;






@Controller
// nombre q se pasa a la view - Persistir en la vista attributos q no estan en el form - mejor q 1 input hidden - ya no hace falta pasarlo con el mode.addAttr
@SessionAttributes("client")
public class ClientController {
  
  @Autowired
  // @Qualifier("clientDaoJPA")
  private IClientService clientService;
  
  @Autowired
  private IUploadFileService uploadFileService;
  

  // trae la img - No requiere MvcConfig.java
  @GetMapping("/uploads/{filename:.+}") // :.+ para q NO omita la extension
  public ResponseEntity<Resource> getPhoto(@PathVariable String filename) {

    Resource recurso = null;
    try {
      recurso = uploadFileService.load(filename);
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }

    return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"")
				.body(recurso);

  }


  // get photo
  @GetMapping(value = "/ver/{id}")
  public String ver(@PathVariable Long id, Model model, RedirectAttributes flash) { //@PathVariable mismo name omite value
    Client client = clientService.fetchByIdWithInvoices(id);
    if (client == null) {
      flash.addFlashAttribute("error", "Client not found!");
      return "redirect:/listar";
    }

    model.addAttribute("client", client);
    model.addAttribute("title", "Client details: ".concat(client.getName()));


    return "ver";
  }

  
  @GetMapping({"/listar", ""})
  public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {

    // Paginado:
    Pageable pageRequest = PageRequest.of(page, 5);
    Page<Client> clients = clientService.getAll(pageRequest);

    PageRender<Client> pageRender = new PageRender<>("/listar", clients); // pasamos la url
    model.addAttribute("page", pageRender);


    model.addAttribute("title", "Client List");
    model.addAttribute("clients", clients);

    return "listar";
  }


  @GetMapping(value="/form")
  public String crear(Model model) {
    Client client = new Client();
    
    model.addAttribute("title", "Form user");
    model.addAttribute("client", client);

    return "form";
  }


  // @Valid y BindingResult (se inyecta en auto x Valid) deben estar juntos --
  // RedirectAttributes para pasar msg -- MultipartFile para capturar el file
  @PostMapping("/form")
  public String save(@Valid Client client, BindingResult result, Model model, @RequestParam("file") MultipartFile photo,
      RedirectAttributes flash,
      SessionStatus sessionStatus) {

    if (result.hasErrors()) {
      model.addAttribute("title", "Form user");
      return "form"; // retornamos la vista del form - los errors se pasa en auto
    }

    // // save photo
    if (!photo.isEmpty()) {

      // update photo: user de db q quiere reemplazar un photo
      if (client.getId() != null && client.getId() > 0 && client.getPhoto() != null
          && client.getPhoto().length() > 0) {
        uploadFileService.delete(client.getPhoto());
      }

      // save photo
      String uniqueFilename = null;
      try {
        uniqueFilename = uploadFileService.copy(photo);
      } catch (IOException e) {
        e.printStackTrace();
      }
      // message
      flash.addFlashAttribute("info", "file upload successfully! " + uniqueFilename);

      // Setearle al client
      client.setPhoto(uniqueFilename);
    }

    String message = client.getId() != null ? "Client has been updated!" : "Client has been created!";

    clientService.save(client);

    sessionStatus.setComplete(); // elimina al cliente de la session

    // enviar msg
    flash.addFlashAttribute("success", message);

    return "redirect:/listar";
  }

  @GetMapping("/form/{id}")
  public String update(@PathVariable Long id, Model model, RedirectAttributes flash) {
    Client client = null;

    if (id > 0) {
      client = clientService.getById(id);

      if(client == null) {
        flash.addFlashAttribute("error", "Invalid ID!");
        return "redirect:/listar";
      }
    } else {
      // enviar msg
      flash.addFlashAttribute("error", "Invalid ID!");
      return "redirect:/listar";
    }

    model.addAttribute("title", "Edit user");
    model.addAttribute("client", client);


    return "form";
  }


  @GetMapping(value = "/delete/{id}")
  public String delete(@PathVariable Long id, RedirectAttributes flash) {

    if (id > 0) {
      Client client = clientService.getById(id);

      clientService.delete(id);
      flash.addFlashAttribute("success", "Client has been deleted!");


      // Eliminar photo del client eliminado
      if (uploadFileService.delete(client.getPhoto())) {
        flash.addFlashAttribute("success", "Foto " + client.getPhoto() + " eliminada!");
      }
    }

    return "redirect:/listar";
  }
  

  
}
