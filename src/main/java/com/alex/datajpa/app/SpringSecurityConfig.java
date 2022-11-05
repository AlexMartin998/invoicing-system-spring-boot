package com.alex.datajpa.app;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import com.alex.datajpa.app.auth.handler.LoginSuccessHandler;
import com.alex.datajpa.app.models.services.JpaUserDetailsService;

// // W en memoria, NO a production
// en la version actualizada de spring boot ya NO extends de ...Adapter q esta deprecated
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)  // habilitar la seguridad/persmisos x Annotations en el controller
@Configuration
public class SpringSecurityConfig {

    // @Autowired
    // private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private LoginSuccessHandler successHandler;


    // //  Con JPA
    @Autowired
    private JpaUserDetailsService userDetailsService;

    @Bean
    public UserDetailsService userDetailsService(AuthenticationManagerBuilder build) throws Exception {
        build.userDetailsService(userDetailsService);

        return build.getDefaultUserDetailsService();
    }



    /* // // // // // JDBC q me funco
    // Datasource para connection a DB
    @Autowired
    private DataSource dataSource;

    @Bean
    public UserDetailsService userDetailsService(AuthenticationManagerBuilder build) throws Exception {
        build.jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(passwordEncoder)
                .usersByUsernameQuery("SELECT username, password, enabled FROM users WHERE username=?")
                .authoritiesByUsernameQuery(
                        "SELECT u.username, a.authority FROM authorities a INNER JOIN users u ON (a.user_id=u.id) WHERE u.username=?");

        return build.getDefaultUserDetailsService();
    }
    

    

    // // // JDBC Como lo menciona la doc, but no se como implementarlo :'v
    // @Bean
    // public DataSource dataSource() {
    //     return new EmbeddedDatabaseBuilder()
    //         .setType(EmbeddedDatabaseType.H2)
    //         .addScript(JdbcDaoImpl.DEFAULT_USER_SCHEMA_DDL_LOCATION)
    //         .build();
    // }
    
    // @Bean
    // public UserDetailsManager users() {
    //     UserDetails user = User.builder()
    //             .username("user")
    //             .password("{bcrypt}$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW")
    //             .roles("ROLE_USER")
    //             .build();
    //     UserDetails admin = User.builder()
    //             .username("admin")
    //             .password("{bcrypt}$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW")
    //             .roles("ROLE_USER", "ROLE_ADMIN")
    //             .build();

    //     JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
    //     users.createUser(user);
    //     users.createUser(admin);
    //     return users;
    // }
 */


    /* // // In memory
    @Bean
    public UserDetailsService userDetailsService() throws Exception {
        // // In memory - Solo para probar en devel - NOO en production
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();

        manager.createUser(
                User.withUsername("alex").password(passwordEncoder.encode("123123")).roles("USER").build());

        manager.createUser(
                User.withUsername("admin").password(passwordEncoder.encode("123123")).roles("ADMIN", "USER")
                        .build());

        return manager;
    } */


    
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
