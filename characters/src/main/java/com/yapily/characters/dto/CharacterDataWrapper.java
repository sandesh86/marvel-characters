package com.yapily.characters.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CharacterDataWrapper {

	private int code;
	private String status;
	private String copyright;
	private String attributionText;
	private String attributionHTML;
	private String etag;
	private CharacterDataContainer data;
}
