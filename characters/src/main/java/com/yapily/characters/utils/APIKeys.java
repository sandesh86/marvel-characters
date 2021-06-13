package com.yapily.characters.utils;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ConfigurationProperties(prefix = "marvel")
public class APIKeys {
	
	private String publickey;
	private String privatekey;

}
