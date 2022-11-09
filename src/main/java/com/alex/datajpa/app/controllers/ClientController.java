package com.alex.datajpa.app.controllers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;

import com.alex.datajpa.app.models.entity.Client;
import com.alex.datajpa.app.models.services.IClientService;
import com.alex.datajpa.app.models.services.IUploadFileService;
import com.alex.datajpa.app.util.paginator.PageRender;
import com.alex.datajpa.app.view.xml.ClientList;






@Controller
// nombre q se pasa a la view - Persistir en la vista attributos q no estan en el form - mejor q 1 input hidden - ya no hace falta pasarlo con el mode.addAttr
@SessionAttributes("client")
public class ClientController {
  
  // logs
  private final Logger log = LoggerFactory.getLogger(getClass());

  @Autowired
  // @Qualifier("clientDaoJPA")
  private IClientService clientService;
  
  @Autowired
  private IUploadFileService uploadFileService;
  

  // multilenguaje
  @Autowired
  private MessageSource messageSource;


  // trae la img - No requiere MvcConfig.java
  @Secured("ROLE_USER")   // permisos directamente en el controller
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
  @Secured({"ROLE_USER", "ROLE_OTRO-ROLE"})   // arreglo de roles
  // @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_OTHER')") // otra forma de proteger con un arreglo de roles
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


  @GetMapping({ "/listar", "" })
  public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model,
      Authentication authentication, HttpServletRequest request, Locale locale) {

    // // Paginado:
    Pageable pageRequest = PageRequest.of(page, 5);
    Page<Client> clients = clientService.getAll(pageRequest);

    PageRender<Client> pageRender = new PageRender<>("/listar", clients); // pasamos la url
    model.addAttribute("page", pageRender);

    // // title en multilanguages - code: mismo q en properties - locale de java.util
    model.addAttribute("title", messageSource.getMessage("text.client.listar.title", null, locale));
    model.addAttribute("clients", clients);

    
    // // NO lo utiliza en nada
    //  obtener el user auth 1) inyect en el methos y 2) con el SecurityContextHolder <- asi lo puedes obtener en donde sea
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if(authentication != null) {
      // podriamos hacer algo
    }

    // verificar role en el controller - manual
    if (hasRole("ROLE_ADMIN")) {
      log.info("Hola ".concat(auth.getName()).concat(" tienes acceso!"));
    } else {
      log.info("Hola ".concat(auth.getName()).concat(" NO tienes acceso!"));
    }

    // otra forma inject HttpServletRequest & SecurityContextHolderAwareRequestWrapper
    SecurityContextHolderAwareRequestWrapper securityContext = new SecurityContextHolderAwareRequestWrapper(request, "");
    if (securityContext.isUserInRole("ROLE_ADMIN")) {  // ADMIN por el rolePrefix
      log.info("Usando SecurityContextHolderAwareRequestWrapper --> Hola ".concat(auth.getName()).concat(" tienes acceso!"));
    } else {
      log.info("Usando SecurityContextHolderAwareRequestWrapper --> Hola ".concat(auth.getName()).concat(" NO tienes acceso!"));
    }

    // otra inject HttpServletRequest 
    if (request.isUserInRole("ROLE_ADMIN")) {  // ADMIN por el rolePrefix
      log.info("Usando Solor el Request --> Hola ".concat(auth.getName()).concat(" tienes acceso!"));
    } else {
      log.info("Usando Solor el Request --> Hola ".concat(auth.getName()).concat(" NO tienes acceso!"));
    }


    return "listar";
  }



  
  // // // // API Rest handler - NO retorna una view como los demas, retorna directamente un JSON
  // Debe retornar un objeto simple de Java (POJO), un entity con setters/getters o un Java bean
  @GetMapping("/listar-rest")
  @ResponseBody // return de este method se alamcena en el Body de la Response y Spring lo serializa a REST
  // public List<Client> listarRest() {  // solo JSON
  public ClientList listarRest() {    // ambos, xml x default y JSON tb como param =  listar-rest?format=json

    // return clientService.getAll(); // solo soporte a JSON
    return new ClientList(clientService.getAll());    // sporta xml y json xq ambos son respuestas REST
  }







  
  @Secured("ROLE_ADMIN")  // segirudad directamente en el controller
  @GetMapping(value = "/form")
  public String crear(Model model) {
    Client client = new Client();

    model.addAttribute("title", "Form user");
    model.addAttribute("client", client);

    return "form";
  }


  // @Valid y BindingResult (se inyecta en auto x Valid) deben estar juntos --
  // RedirectAttributes para pasar msg -- MultipartFile para capturar el file
  @Secured("ROLE_ADMIN")  // segirudad directamente en el controller
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

  
  // @Secured("ROLE_ADMIN")  // segirudad directamente en el controller
  @PreAuthorize("hasRole('ROLE_ADMIN')")  // otra forma de proteger
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


  @Secured("ROLE_ADMIN")  // segirudad directamente en el controller
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
  

  // Verificar si tiene el role en el Controller  <-  hasta el momento NO hace nada con eso
  private boolean hasRole(String role) {
    SecurityContext context = SecurityContextHolder.getContext();
    if (context == null) return false;

    Authentication auth = context.getAuthentication();
    if (auth == null) return false;
    
    Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();

    return authorities.contains(new SimpleGrantedAuthority(role));

    /* Para cuando W con DB en la auth
    // todo role en spring security implementa GrantedAuthority
    for (GrantedAuthority authority : authorities) {
      if(role.equals(authority.getAuthority())) {
        log.info("User ".concat(auth.getName()).concat(" your role is: ").concat(authority.getAuthority()));
        return true;
      }
    }
    
    return false;
     */
  }
  
}
