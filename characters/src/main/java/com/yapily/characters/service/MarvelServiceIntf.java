package com.yapily.characters.service;

import java.util.Set;

import com.yapily.characters.dto.Character;

public interface MarvelServiceIntf {

	public Set<Long> getAllCharacters();
	
	public Character getCharacterDetails (long characterId);
}
