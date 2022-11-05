package com.alex.datajpa.app;

// import java.nio.file.Paths;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
// import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;



// ================================================================
// // //  NO se usa xq implementamos   @GetMapping("/uploads/{filename:.+}")  q trae la img a la view q esta en upload/ al nivel de /src
// ================================================================

// // configurar el path externo para guardar los files subidos
// Ya NO es necesario si usamos 

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    /* Path local del server, externos al project
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        WebMvcConfigurer.super.addResourceHandlers(registry);

       
        // registrar el new path | 1. path de la view q se va a mapear | 2. path del dir externo de los recursos <- = al del controller
        registry.addResourceHandler("/uploads/**")
		.addResourceLocations("file:///home/adrian/z_delete/z_spring-uploads/uploads/");
    } */
    
    /* 
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        WebMvcConfigurer.super.addResourceHandlers(registry);

        // /uploads al nivel /src dentro del project
        String resourcePath = Paths.get("uploads/**").toAbsolutePath().toUri().toString();
        log.info(resourcePath);

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(resourcePath);
    } */


    

    // // // // Controller parametrizable q renderiza views, but NO tiene logica
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/error-403").setViewName("error-403");
    }


}
