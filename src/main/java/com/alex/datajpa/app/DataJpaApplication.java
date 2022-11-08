package com.alex.datajpa.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.alex.datajpa.app.models.services.IUploadFileService;

@SpringBootApplication
public class DataJpaApplication implements CommandLineRunner {

	@Autowired
    private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	IUploadFileService uploadFileService;


	public static void main(String[] args) {
		SpringApplication.run(DataJpaApplication.class, args);
	}


	// crear /uploads auto al levantar el project, eliminarla recursiva al bajar el server
	@Override
	public void run(String... args) throws Exception {
		uploadFileService.deleteAll();
		uploadFileService.init();


		// Generar pass para copiar y pegarlo a DB manualmente, se debe hacer asi??? -- solo se uso para copiar y pegar en db, se puede eliminar - but como se registra 1 nuevo user y e hashea su pass al registrarlo
		String password = "123123";
		for (int i = 0; i < 2; i++) {
			String bcryptPassword = passwordEncoder.encode(password);
			System.out.println("Password: " + bcryptPassword);
		}
	}

}
