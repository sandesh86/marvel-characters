package com.yapily.characters.utils;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.yapily.characters.service.DataLoader;

@ActiveProfiles("test")
@SpringBootTest
class AppConfigTest {

	@Mock
	private DataLoader dataLoader;
	
	@InjectMocks
	private AppConfig appConfig;
	
	@Test
	void testLoadMetaData() throws InterruptedException {
		appConfig.loadMetaData();
		Mockito.verify(dataLoader, Mockito.times(1)).loadCharactersData();
	}

}
