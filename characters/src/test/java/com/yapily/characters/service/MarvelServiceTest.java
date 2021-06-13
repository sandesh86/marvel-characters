package com.yapily.characters.service;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.yapily.characters.dto.Character;

@ActiveProfiles("test")
@SpringBootTest
class MarvelServiceTest {

	@Mock
	private DataLoader loader;
	
	@InjectMocks
	private MarvelService marvelService;
	
	@Test
	void testGetCharacterDetails() {
		Character character = new Character();
		character.setId(1234L);
		character.setName("thor");
		Mockito.when(loader.callMarvelAPI(1234, loader.getMarvelUrl())).thenReturn(character);
		Character charact = marvelService.getCharacterDetails(1234);
		Assertions.assertEquals("thor", charact.getName());
	}

	@Test
	void testGetAllCharacters() {
		Character character = new Character();
		character.setId(1234L);
		character.setName("thor");
		Set<Long> setDummy = new HashSet<>();
		setDummy.add(1234l);
		Mockito.when(loader.getAllKeys()).thenReturn(setDummy);
		Set<Long> set = marvelService.getAllCharacters();
		Assertions.assertEquals(1, set.size());
	}
}
