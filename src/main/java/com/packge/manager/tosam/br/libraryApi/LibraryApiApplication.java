package com.packge.manager.tosam.br.libraryApi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class LibraryApiApplication {

	public static void main(String[] args) {

		 SpringApplication.run(LibraryApiApplication.class, args);
	}




}