package com.abc.AWSLambdaAuthorizer;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;

public class AADKeySet {

	@JsonAlias("keys")
	private List<JsonWebKey> keys = new ArrayList<>();

	public List<JsonWebKey> getKeys() {
		return keys;
	}

	public void setKeys(List<JsonWebKey> keys) {
		this.keys = keys;
	}
	
	
	
}
