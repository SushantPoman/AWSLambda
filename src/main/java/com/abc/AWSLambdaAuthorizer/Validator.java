package com.abc.AWSLambdaAuthorizer;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.IncorrectClaimException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MissingClaimException;
import io.jsonwebtoken.SignatureException;

public class Validator implements RequestHandler<APIGatewayProxyRequestEvent, Response>{
	
    public static void main(String[] args) {
        String token = "eyJ0eXAioiJk1QiLCJhsGciOiJSUxI1NiIsImtpZGDDKSvkmdl29j.eyJhdjsDSdfsdlksFD2GddfjlsdjfldsJLSJDKLJSLDJkdjfslkjdl.fjdlilkds323dmflksjdl3DSgjdsldDofdsjlkjdslfjd";
        Validator validator = new Validator();
        Response response = validator.callAuthorizer(token, Optional.empty());
        System.out.println("Response: "+response.getPrincipalId() +" \nPolicy: "+response.getPolicyDocument());
    }

	@Override
	public Response handleRequest(APIGatewayProxyRequestEvent input, Context context) {
		LambdaLogger logger = context.getLogger();
		Map<String, String> headersMap = input.getHeaders();
		String authToken = headersMap.get("authorization");
		logger.log("AuthToken: "+authToken);
		return callAuthorizer(authToken, Optional.of(logger));
	} 
	
	public Response callAuthorizer(String token, Optional<LambdaLogger> logger) {
		String auth = "Deny";
		String arn = String.format("arn:aws:execute-api:%s:%s:%s:%s:%s:%s", "eu-west-2", System.getenv("ACCOUNT_ID"), System.getenv("API_ID"),"*", "*","*");
		
		try {
			auth = "Allow";
			Jws<Claims> claims = validateAccessToken(token);
			Statement statement = Statement.builder().effect(auth).resource(arn).build();
			PolicyDocument policyDocument = PolicyDocument.builder().statements(Collections.singletonList(statement)).build();
			
			if(logger.isPresent()) {
				logger.get().log(arn);
				logger.get().log("Validate! Got a token with the following claims: ");
				logger.get().log("Issuer: "+ claims.getBody().getIssuer());
				logger.get().log("Audience: "+claims.getBody().getAudience());
				logger.get().log("Issued at: "+claims.getBody().getIssuedAt());
				logger.get().log("Expires on: "+claims.getBody().getExpiration());
			}
			
			return Response.builder().principalId(System.getenv("ACCOUNT_ID")).policyDocument(policyDocument).build();
		} catch (Exception ex) {
			if(logger.isPresent()) {
				logger.get().log(arn);
				logger.get().log("Exception: "+ex.getMessage());
				logger.get().log("Exception class: "+ex.getClass());
				logger.get().log("Exception detail: "+ex.toString());
			}
			auth = "Deny";
			Statement statement = Statement.builder().effect(auth).resource(arn).build();
			PolicyDocument policyDocument = PolicyDocument.builder().statements(Collections.singletonList(statement)).build();
			
			return Response.builder().principalId(System.getenv("ACCOUNT_ID")).policyDocument(policyDocument).build();
		}
	}
	
	private static Jws<Claims> validateAccessToken(String accessToken) throws Exception {
		
		SigningKeyResolver signingKeyResolver = new SigningKeyResolver(System.getenv("AUTHORITY"));
		Jws<Claims> claims;
		try {
			
			claims = Jwts.parser().setSigningKeyResolver(signingKeyResolver)
					.requireAudience(System.getenv("API_APP_ID"))
					.requireIssuer(System.getenv("ISSUER"))
					.require("scp", System.getenv("SCOPE_VALIDATE"))
					.parseClaimsJws(accessToken);
			
		} catch (SignatureException ex) {
			ex.printStackTrace();
			throw new JwtValidationException("Jwt validation failed: invalid signature", ex);
		} catch (ExpiredJwtException ex) {
			ex.printStackTrace();
			throw new JwtValidationException("Jwt validation failed: access token us expired", ex);
		} catch (MissingClaimException ex) {
			ex.printStackTrace();
			throw new JwtValidationException("Jwt validation failed: missing required claim", ex);
		} catch (IncorrectClaimException ex) {
			ex.printStackTrace();
			throw new JwtValidationException("Jwt validation failed: required claim has incorrect value", ex);
		} 
		
		return claims;
	}

}
