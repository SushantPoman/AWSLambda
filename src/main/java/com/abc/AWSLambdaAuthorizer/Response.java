package com.abc.AWSLambdaAuthorizer;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = Response.Builder.class)
public class Response {
	
	@JsonProperty("principalId")
	private String principalId;
	
	@JsonProperty("policyDocument")
	private PolicyDocument policyDocument;

	private Response(Builder builder) {
		this.principalId = builder.principalId;
		this.policyDocument = builder.policyDocument;
	}

	public String getPrincipalId() {
		return principalId;
	}

	public PolicyDocument getPolicyDocument() {
		return policyDocument;
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	@JsonPOJOBuilder(withPrefix = "")
	public static final class Builder {
		private String principalId;
		private PolicyDocument policyDocument;
		
		private Builder() {
		}
		
		public Builder principalId(String principalId) {
			this.principalId = principalId;
			return this;
		}
		
		public Builder policyDocument(PolicyDocument policyDocument) {
			this.policyDocument = policyDocument;
			return this;
		}
		
		public Response build() {
			return new Response(this);
		}
	}


	@Override
	public String toString() {
		return "Response [principalId=" + principalId + ", policyDocument=" + policyDocument + "]";
	}
	
	

}
