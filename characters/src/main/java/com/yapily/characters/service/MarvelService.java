package com.yapily.characters.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yapily.characters.dto.Character;

/**
 * Service class for Marvel APIs
 * @author Sandesh
 *
 */
@Service
public class MarvelService implements MarvelServiceIntf {
		
	@Autowired
	private DataLoader dataLoader;
	
	@Override
	public Set<Long> getAllCharacters() {
		return dataLoader.getAllKeys();
	}
	
	@Override
	public Character getCharacterDetails (long characterId) {
		return dataLoader.callMarvelAPI(characterId, dataLoader.getMarvelUrl());
	}
}
