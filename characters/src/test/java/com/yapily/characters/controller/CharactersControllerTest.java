package com.yapily.characters.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.HttpClientErrorException;

import com.yapily.characters.dto.Character;
import com.yapily.characters.service.MarvelService;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class CharactersControllerTest {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;
	
	@MockBean
	private MarvelService marvelService; 
	
	@InjectMocks
	private CharactersController controller;
	
	@Nested
    @DisplayName("Tests for the REST API /characters")
	class All {
		@Test
		void testAllCharacters() {
			Set<Long> set = new HashSet<>();
			set.add(12345L);
			set.add(67890L);
			
			Mockito.when(marvelService.getAllCharacters()).thenReturn(set);
			
			ResponseEntity<List> response = restTemplate.getForEntity("http://localhost:" + port + "/characters",
					List.class);
			Assertions.assertEquals(2, response.getBody().size());
			
		}
		
		@Test
		void testAllCharactersHttpError() {
			
			Mockito.when(marvelService.getAllCharacters()).thenThrow(new HttpClientErrorException(HttpStatus.CONFLICT, "{1234}", "ErrorResponse".getBytes(), null));
			
			ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/characters",
					String.class);
			Assertions.assertEquals("ErrorResponse", response.getBody());
			
		}
		
		@Test
		void testAllCharactersError() {
			
			Mockito.when(marvelService.getAllCharacters()).thenThrow(new RuntimeException("ErrorResponse"));
			
			ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/characters",
					String.class);
			Assertions.assertEquals("Internal Service Error, please contact administrator", response.getBody());
			
		}
	}
	
	
	@Nested
    @DisplayName("Tests for the REST API /characters/{characterId}")
	class Single {
		@Test
		void testSingleCharacters() {
			Character character = new Character();
			character.setId(12345L);
			character.setName("Iron Man");
			
			Mockito.when(marvelService.getCharacterDetails(12345L)).thenReturn(character);
			
			ResponseEntity<Character> response = restTemplate.getForEntity("http://localhost:" + port + "/characters/12345",
					Character.class);

			Assertions.assertEquals("Iron Man", response.getBody().getName());
		}
		
		@Test
		void testSingleCharactersHttpError() {		
			
			Mockito.when(marvelService.getCharacterDetails(12345L)).thenThrow(new HttpClientErrorException(HttpStatus.CONFLICT, "{1234}", "ErrorResponse".getBytes(), null));
			
			ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/characters/12345",
					String.class);

			Assertions.assertEquals("ErrorResponse", response.getBody());
		}
		
		@Test
		void testSingleCharactersError() {
					
			Mockito.when(marvelService.getCharacterDetails(12345L)).thenThrow(new RuntimeException("ErrorResponse"));
			
			ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/characters/12345",
					String.class);

			Assertions.assertEquals("Internal Service Error, please contact administrator", response.getBody());
		}
	}

}
