package com.abc.AWSLambdaAuthorizer;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = PolicyDocument.Builder.class)
public class PolicyDocument {
	
	public final String versionString = "2012-10-17";
	public List<Statement> statements;

	private PolicyDocument(Builder builder) {
		this.statements = builder.statements;
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	@JsonPOJOBuilder(withPrefix = "")
	public static final class Builder {
		private List<Statement> statements;
		
		private Builder() {	
			statements = new ArrayList<Statement>();
		}
		
		public Builder statements(List<Statement> statements) {
			this.statements = statements;
			return this;
		}
		
		public PolicyDocument build() {
			return new PolicyDocument(this);
		}
	
	}

	@Override
	public String toString() {
		return "PolicyDocument [versionString=" + versionString + ", statements=" + statements + "]";
	}
	
	
}
