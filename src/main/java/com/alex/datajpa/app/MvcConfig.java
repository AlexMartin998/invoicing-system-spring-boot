package com.alex.datajpa.app;

import java.util.Locale;

import org.springframework.context.annotation.Bean;

// import java.nio.file.Paths;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
// import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;



// ================================================================
// // //  NO se usa xq implementamos   @GetMapping("/uploads/{filename:.+}")  q trae la img a la view q esta en upload/ al nivel de /src
// ================================================================

// // configurar el path externo para guardar los files subidos
// Ya NO es necesario si usamos 

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    // se lo puede Inject x el @Bean
    @Bean
    public static BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    

    // // // // Controller parametrizable q renderiza views, but NO tiene logica
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/error-403").setViewName("error-403");
    }



    // // // multilenguaje - se guarda en la http session   --  Son INTERCEPTORS, x eso los debemos implementar con addIntercepors()
    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver localeResolver = new SessionLocaleResolver();
        localeResolver.setDefaultLocale(new Locale("es", "ES"));        // x default sera la web estara en es_ES

        return localeResolver;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor localeInterceptor = new LocaleChangeInterceptor();
        localeInterceptor.setParamName("lang"); // va a leer de los params de la url

        return localeInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        
        registry.addInterceptor(localeChangeInterceptor());
    }


    // // // Bean para serializar a XML -- Lo Inject en la view xml
    @Bean
    public Jaxb2Marshaller jaxb2Marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(new Class[] {com.alex.datajpa.app.view.xml.ClientList.class});    // wrapper/root  

        return marshaller;
    }
    






    /* Path local del server, externos al project /uploads
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



}
