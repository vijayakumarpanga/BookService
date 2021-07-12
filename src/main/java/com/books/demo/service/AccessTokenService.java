package com.books.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.books.demo.Payload.AccessToken;

@Component
public class AccessTokenService {

	private static final Logger LOGGER=LoggerFactory.getLogger(AccessTokenService.class);
	@Autowired
	private Environment environment;
	
	@Cacheable(value="accessToken")
	public AccessToken getAuthToken() {


		AccessToken response=null;
		try {
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.set("Content-Type", "application/x-www-form-urlencoded");
			MultiValueMap<String,String> body = new LinkedMultiValueMap<String,String>();
			body.add("grant_type", environment.getProperty("grant_type"));
			body.add("client_id", environment.getProperty("client_id"));
			body.add("client_secret", environment.getProperty("client_secret"));
			body.add("scope", environment.getProperty("scope"));
			HttpEntity<MultiValueMap<String,String>> httpEntity = new HttpEntity<MultiValueMap<String,String>>(body,headers);
			ResponseEntity<AccessToken>	result = restTemplate.exchange(environment.getProperty("auth_url"), HttpMethod.POST, httpEntity, AccessToken.class);
			if(result.getStatusCodeValue()==200 && result.getBody() != null) {
				response = result.getBody();
				LOGGER.info("Successfully get accces token : "+result.getBody().getAccess_token());
				response.setStatus("Success");
			}
			else {
				response = new AccessToken();
				response.setStatus("failed");
				LOGGER.error("Authentication failed ");
			}
		} 
		catch (HttpStatusCodeException e) {
			response = new AccessToken();
			response.setStatus("failed");
			LOGGER.error("Exception occured in getAuthToken"+e.getMessage());

		}
		return response ;
	}
	
	@CachePut(value="accessToken")
	public AccessToken getAuthTokenUpdate() {


		AccessToken response=null;
		try {
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.set("Content-Type", "application/x-www-form-urlencoded");
			MultiValueMap<String,String> body = new LinkedMultiValueMap<String,String>();
			body.add("grant_type", environment.getProperty("grant_type"));
			body.add("client_id", environment.getProperty("client_id"));
			body.add("client_secret", environment.getProperty("client_secret"));
			body.add("scope", environment.getProperty("scope"));
			HttpEntity<MultiValueMap<String,String>> httpEntity = new HttpEntity<MultiValueMap<String,String>>(body,headers);
			ResponseEntity<AccessToken>	result = restTemplate.exchange(environment.getProperty("auth_url"), HttpMethod.POST, httpEntity, AccessToken.class);
			if(result.getStatusCodeValue()==200 && result.getBody() != null) {
				response = result.getBody();
				LOGGER.info("Successfully get accces token : "+result.getBody().getAccess_token());
				response.setStatus("Success");
			}
			else {
				response = new AccessToken();
				response.setStatus("failed");
				LOGGER.error("Authentication failed ");
			}
		} 
		catch (HttpStatusCodeException e) {
			response = new AccessToken();
			response.setStatus("failed");
			LOGGER.error("Exception occured in getAuthTokenUpdate"+e.getMessage());

		}
		return response ;
	}


}
