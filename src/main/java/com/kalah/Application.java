package com.kalah;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
	
	
	public Application(){//NOSONAR
		super();
	}
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);//NOSONAR
	}
	

}
