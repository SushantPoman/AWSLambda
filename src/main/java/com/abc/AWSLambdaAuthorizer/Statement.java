package com.abc.AWSLambdaAuthorizer;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = Statement.Builder.class)
public class Statement {
	
	public final String action = "execute-api:Invoke";
	public String effect;
	public String resoure;
	
	private Statement(Builder builder) {
		this.effect = builder.effect;
		this.resoure = builder.resource;
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static final class Builder {
		private String effect;
		private String resource;
		
		private Builder() {
			
		}
		
		public Builder effect(String effect) {
			this.effect = effect;
			return this;
		}
		
		public Builder resource(String resource) {
			this.resource = resource;
			return this;
		}
		
		public Statement build() {
			return new Statement(this);
		}
	}

	@Override
	public String toString() {
		return "Statement [action=" + action + ", effect=" + effect + ", resoure=" + resoure + "]";
	}
	

}
