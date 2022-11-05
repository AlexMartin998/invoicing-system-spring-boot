package com.alex.datajpa.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.alex.datajpa.app.models.services.IUploadFileService;

@SpringBootApplication
public class DataJpaApplication implements CommandLineRunner {

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
	}

}
