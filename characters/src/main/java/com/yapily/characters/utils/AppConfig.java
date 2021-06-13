package com.yapily.characters.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;

import com.yapily.characters.service.DataLoader;

/**
 * Loading all character data into memory at application load time
 * @author acer
 *
 */
@Profile("!test")
@Configuration
public class AppConfig {

	@Autowired
	private DataLoader dataLoader;
	
	@EventListener(ApplicationReadyEvent.class)
	public void loadMetaData() throws InterruptedException {
		dataLoader.loadCharactersData();
	}
	
}
