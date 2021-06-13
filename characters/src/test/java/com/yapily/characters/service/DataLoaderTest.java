package com.yapily.characters.service;

import java.io.IOException;
import java.util.Set;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.client.RestTemplate;

import com.yapily.characters.dto.Character;
import com.yapily.characters.utils.Constants;
import com.yapily.characters.utils.Utils;

import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

@ActiveProfiles("test")
@SpringBootTest
class DataLoaderTest {
	
	private static MockWebServer mockWebServer;
	
	@DynamicPropertySource
    static void properties(DynamicPropertyRegistry r) throws IOException {
        r.add("marvel.characters.url", () -> "http://localhost:" + mockWebServer.getPort() + "/v1/public/characters");
    }
	
	@BeforeAll
	public static void setUp() {
		try {
			mockWebServer = new MockWebServer();
			mockWebServer.start();
			String response = Utils.readFile("sampleAllChar.txt");
			MockResponse mockResponseAll = new MockResponse().addHeader("Content-Type", "application/json; charset=utf-8")
					.setBody(response);
			mockWebServer.enqueue(mockResponseAll);
			
			response = Utils.readFile("sampleSingleChar.txt");
			MockResponse mockResponseSingle = new MockResponse().addHeader("Content-Type", "application/json; charset=utf-8")
					.setBody(response);
			mockWebServer.enqueue(mockResponseSingle);
			
			response = "";
			MockResponse mockResponseEmpty = new MockResponse().addHeader("Content-Type", "application/json; charset=utf-8")
					.setBody(response);
			mockWebServer.enqueue(mockResponseEmpty);

			Dispatcher mDispatcher = new Dispatcher() {
				@Override
				public MockResponse dispatch(RecordedRequest request) {
					if (request.getPath().contains("/1009679")) {
						return mockResponseSingle;
					}
					if (request.getPath().contains("/characters")) {
						return mockResponseAll;
					}		
					if (request.getPath().contains("/empty")) {
						return mockResponseEmpty;
					}
					return new MockResponse().setResponseCode(404);
				}
			};
			mockWebServer.setDispatcher(mDispatcher);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@AfterAll
	public static void destroy () throws IOException {
		mockWebServer.shutdown();
	}
	
	@InjectMocks
	@Autowired
	private DataLoader loader;
	
	@Test
	void testLoadCharacters() {
		int size = loader.loadCharacters(0, mockWebServer.url("/characters").toString());
		Assertions.assertEquals(1493, size);
	}
	
	@Test
	void testLoadCharactersEmpty() {
		int size = loader.loadCharacters(0, mockWebServer.url("/empty").toString());
		Assertions.assertEquals(0, size);
	}
	
	@Test
	void testgetAllKeys() {
		loader.loadCharacters(0, mockWebServer.url("/characters").toString());
		Set<Long> set = loader.getAllKeys();
		Assertions.assertEquals(20, set.size());
	}
	
	@Test
	void testCallMarvelAPI() {
		Character character = loader.callMarvelAPI(1009679, mockWebServer.url("/1009679").toString());
		Assertions.assertEquals(1009679, character.getId());
	}
	
	@Test
	void testCallMarvelAPIEmpty() {
		Character character = loader.callMarvelAPI(1000000, mockWebServer.url("/empty").toString());
		Assertions.assertNull(character);
	}
	
	@Test
	void testLoadCharactersDataI() throws InterruptedException {
		loader.loadCharactersData();
		Assertions.assertEquals(20, loader.getAllKeys().size());
	}
	
	@Test
	void testGetHashValue() {
		String hash = loader.getHashValue(1, "1234", "abcd");
		Assertions.assertNotEquals(Constants.EMPTY, hash);
	}
	
	@Test
	void testGetMarvelUrl() {
		String url = loader.getMarvelUrl();
		Assertions.assertTrue(url.contains("v1/public/characters"));
	}
}
