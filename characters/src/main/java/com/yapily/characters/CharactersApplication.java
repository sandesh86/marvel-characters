package com.yapily.characters;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

/**
 * Application to get the information about marvel characters
 * @author Sandesh
 *
 */
@Slf4j
@SpringBootApplication
public class CharactersApplication {
		  
	public static void main(String[] args) {		
		SpringApplication.run(CharactersApplication.class, args);
		log.info("Application Stated..");
	}
	
	@Bean
	public RestTemplate restTemplate() {		
	    return new RestTemplate();
	}

}
