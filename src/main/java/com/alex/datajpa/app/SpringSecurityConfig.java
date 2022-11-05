package com.alex.datajpa.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import com.alex.datajpa.app.auth.handler.LoginSuccessHandler;

// // W en memoria, NO a production
// en la version actualizada de spring boot ya NO extends de ...Adapter q esta deprecated
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)  // habilitar la seguridad/persmisos x Annotations en el controller
@Configuration
public class SpringSecurityConfig {

    @Autowired
    private LoginSuccessHandler successHandler;


    @Bean
    public static BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() throws Exception {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();

        manager.createUser(
                User.withUsername("alex").password(passwordEncoder().encode("123123")).roles("USER").build());

        manager.createUser(
                User.withUsername("admin").password(passwordEncoder().encode("123123")).roles("ADMIN", "USER")
                        .build());

        return manager;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/", "/css/**", "/js/**", "/images/**", "/listar").permitAll()
                /* Forma programatica, colocamos a mano las rutas y el role q tendra acceso
                // Se le pude dar Seguridad directamente en el Controller con  @Secured("ROLE")
                .antMatchers("/ver/**").hasAnyRole("USER")
                .antMatchers("/uploads/**").hasAnyRole("USER")
                .antMatchers("/form/**").hasAnyRole("ADMIN")
                .antMatchers("/delete/**").hasAnyRole("ADMIN")
                .antMatchers("/invoice/**").hasAnyRole("ADMIN")
                 */
                .antMatchers("/invoice/**").hasAnyRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                // .formLogin().permitAll() // envia la pag login de spring security cuando no esta auth
                    .formLogin()
                        .successHandler(successHandler)     // pasar el mensaje flash de login success desde otro package
                        .loginPage("/login")
                    .permitAll() // la manejamos nosotros con nuestro login controller
                .and()
                    .logout().permitAll()
                .and()
                    .exceptionHandling().accessDeniedPage("/error-403")    // manejamos la pagina de error
                ;

        return http.build();
    }

}
