package com.yapily.characters.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.yapily.characters.dto.Character;
import com.yapily.characters.dto.CharacterDataWrapper;
import com.yapily.characters.utils.APIKeys;
import com.yapily.characters.utils.CallAPI;
import com.yapily.characters.utils.Constants;

import lombok.extern.slf4j.Slf4j;

/**
 * Loading all characters data and making HTTP call to all Marvel services to get the characters information
 * @author Sandesh
 *
 */
@Slf4j
@Service
@EnableConfigurationProperties(APIKeys.class)
public class DataLoader {
		
	private static ConcurrentMap<Long, Character> map = new ConcurrentHashMap<>();
	private ExecutorService executor = Executors.newCachedThreadPool();
	
	@Autowired
	private APIKeys apiKeys;

	@Value("${marvel.characters.url}")
	private String marvelUrl;

	@Autowired
	private RestTemplate restTemplate;
	
	/**
	 * Loading all characters by making HTTP calls during application load time
	 * @throws InterruptedException
	 */
	public void loadCharactersData () throws InterruptedException {
		log.info("Calling Mavel API ..");
		int offset = 0;
		
		int total = loadCharacters(0, marvelUrl);	
		int size = total % 100 == 0 ? total/100 : total/100+1;
		
		CountDownLatch latch = new CountDownLatch(total/100);		
		for (int i = 1; i < size; i++) {
			offset = i * 100;			
			CallAPI callAPI = new CallAPI(latch, offset, marvelUrl, this);
			executor.submit(callAPI);			
		}		
		latch.await();
		log.info("Character Data is loaded..");		
	}
	
	/**
	 * Loading list of characters from offset
	 * Max limit set is 100
	 * @param offset
	 * @param url
	 * @return
	 */
	public int loadCharacters(int offset, String url) {
		
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
		long timestamp = Calendar.getInstance().getTimeInMillis();
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
		        .queryParam(Constants.LIMIT, 100)
		        .queryParam(Constants.OFFSET, offset)
		        .queryParam(Constants.TIMESTAMP, timestamp)
		        .queryParam(Constants.APIKEY, apiKeys.getPublickey())
		        .queryParam(Constants.HASH, getHashValue(timestamp, apiKeys.getPublickey(), apiKeys.getPrivatekey()));

		HttpEntity<?> entity = new HttpEntity<>(headers);
		HttpEntity<CharacterDataWrapper> response = restTemplate.exchange(
		        builder.toUriString(), 
		        HttpMethod.GET, 
		        entity, 
		        CharacterDataWrapper.class);
		CharacterDataWrapper characters = response.getBody();
		int total = 0;
		
		if (null != characters) {
			int count = characters.getData().getCount();
			total = characters.getData().getTotal();
			
			Iterator<Character> itr = characters.getData().getResults().iterator();
			while (itr.hasNext()) {
				Character character = itr.next();
				map.put(character.getId(), character);
			}		
			log.debug("Count : {}, Count : {}", map.size(), count);
		}		
		return total;
	}
	
	/**
	 * Get only Ids from all character details
	 * @return
	 */
	public Set<Long> getAllKeys() {
		return map.keySet();
	}
	
	/**
	 * Loading individual character details by Id
	 * Fetching real time data by making actual call to Marvel services
	 * @param characterId
	 * @param url
	 * @return
	 */
	public Character callMarvelAPI (long characterId, String url) {
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
		long timestamp = Calendar.getInstance().getTimeInMillis();
		log.debug("characterId : {}", characterId);
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url + "/" + characterId)
		        .queryParam(Constants.TIMESTAMP, timestamp)
		        .queryParam(Constants.APIKEY, apiKeys.getPublickey())
		        .queryParam(Constants.HASH, getHashValue(timestamp, apiKeys.getPublickey(), apiKeys.getPrivatekey()));

		HttpEntity<?> entity = new HttpEntity<>(headers);
		ResponseEntity<CharacterDataWrapper> response = restTemplate.exchange(
		        builder.toUriString(), 
		        HttpMethod.GET, 
		        entity, 
		        CharacterDataWrapper.class);
		CharacterDataWrapper dataWrapper = response.getBody();
		if (null != dataWrapper) {			
			return dataWrapper.getData().getResults().get(0);
		}
		return null;		
	}
	
	/**
	 * Generate Hash value for authorization in marvel API calls
	 * @param timestamp
	 * @param publicKey
	 * @param privateKey
	 * @return
	 */
	public String getHashValue(long timestamp, String publicKey, String privateKey) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] digest = md.digest((timestamp + privateKey + publicKey).getBytes());
			return DatatypeConverter
				      .printHexBinary(digest).toLowerCase();
		} catch (NoSuchAlgorithmException e) {
			log.error("Error while creating hash value", e);
		}
		return Constants.EMPTY;
	}
	
	/**
	 * Get Marvel URL
	 * @return
	 */
	public String getMarvelUrl() {
		return marvelUrl;
	}
}
