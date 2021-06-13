package com.yapily.characters.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yapily.characters.service.MarvelServiceIntf;

/**
 * Controller class which endpoints for all services
 * @author Sandesh
 *
 */
@RestController
@RequestMapping("/")
public class CharactersController {

	@Autowired
	private MarvelServiceIntf marvelService;

	/**
	 * Fetches list of characters
	 * @return
	 */
	@GetMapping("/characters")
	public ResponseEntity<Object> getAllCharacters() {
		return new ResponseEntity<>(marvelService.getAllCharacters(), HttpStatus.OK);
	}

	/**
	 * Fetches single character by ID
	 * @param characterId
	 * @return
	 */
	@GetMapping("/characters/{characterId}")
	public ResponseEntity<Object> getCharacterDetails(@PathVariable long characterId) {
		return new ResponseEntity<>(marvelService.getCharacterDetails(characterId), HttpStatus.OK);
	}

}
