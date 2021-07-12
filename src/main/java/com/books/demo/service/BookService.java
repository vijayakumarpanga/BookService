package com.books.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.books.demo.Payload.Response;

@Component
public class BookService {

	private static final Logger LOGGER=LoggerFactory.getLogger(BookService.class);
	@Autowired
	private Environment environment;
	@Autowired
	private AccessTokenService tokenService;

	public String getAccessToken() {
		String token = null;
		try {
			token = tokenService.getAuthToken().getAccess_token();
		}catch(Exception e){
			e.printStackTrace();
		}
		return token;
	}

	public Response getBook(Integer id,String token){
		Response response=null;
		int retry = 1;
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization","Bearer "+token);
			HttpEntity<String> entity = new HttpEntity<String>(headers);

			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<JSONObject> result = restTemplate.exchange(environment.getProperty("api_base_url")+"/book/"+id,HttpMethod.GET,entity,JSONObject.class);
			if(result.getStatusCodeValue() == 200) {
				if(result.getBody() != null) {
					LOGGER.info("Response of endpoint : "+result.getBody());
					response = new Response("success",result.getStatusCode(),new ArrayList<JSONObject>(List.of(result.getBody())),null);
				}
			}else if(result.getStatusCodeValue()==401) {
				if(retry == 1) {
					LOGGER.error("Authenaction failed and retry to get access token");
					response= getBook(id,tokenService.getAuthTokenUpdate().getAccess_token());	
					retry++;
				}else {
					LOGGER.error("Failed -> get books result failed");
					response = new Response("Failed",result.getStatusCode(),null,"Authenaction failed");
				}
			}else {
				LOGGER.error("Failed -> get books result failed");
				response = new Response("Failed",result.getStatusCode(),null,"Request failed");
			}
		}
		catch (HttpStatusCodeException e) {
			if(e.getStatusCode().value()==401) {
				if(retry == 1) {
					LOGGER.error("Authenaction failed and retry to get access token");
					response= getBook(id,tokenService.getAuthTokenUpdate().getAccess_token());	
					retry++;
				}else {
					LOGGER.error("Failed -> get books result failed");
					response = new Response("Failed",e.getStatusCode(),null,"Authenaction failed");

				}
			}else {
				LOGGER.error("Exception occured in getBook"+e.getMessage());
				response = new Response("Failed",e.getStatusCode(),null,"Request failed");
			}
		}
		return response;
	}


	public Response getBooks(String token){
		Response response = null;
		int retry = 1;
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization","Bearer "+token);

			HttpEntity<String> entity = new HttpEntity<String>(headers);

			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<List<JSONObject>> result = restTemplate.exchange(environment.getProperty("api_base_url")+"/books",HttpMethod.GET,entity,new ParameterizedTypeReference<List<JSONObject>>() {});
			if(result.getStatusCodeValue() == 200) {
				if(result.getBody() != null) {
					response = new Response("success",result.getStatusCode(),result.getBody(),null);
					LOGGER.info("Response of endpoint : "+result.getBody());
				}
			}else if(result.getStatusCodeValue() == 401) {
				if(retry == 1) {
					LOGGER.error("Authenaction failed and retry to get access token");
					response= getBooks(tokenService.getAuthTokenUpdate().getAccess_token());
					retry++;
				}else {
					LOGGER.error("Authenaction failed and retry to get access token");
					response = new Response("Failed",result.getStatusCode(),null,"Authenaction failed");

				}
			}
			else {
				LOGGER.error("Failed -> get books result failed");
				response = new Response("Failed",result.getStatusCode(),null,"Request failed");

			}
		}
		catch (HttpStatusCodeException e) {
			if(e.getStatusCode().value() == 401) {
				if(retry == 1) {
					LOGGER.error("Authenaction failed and retry to get access token");
					response= getBooks(tokenService.getAuthTokenUpdate().getAccess_token());
					retry++;
				}else {
					LOGGER.error("Authenaction failed and retry to get access token");
					response = new Response("Failed",e.getStatusCode(),null,"Authenaction failed");
				}
			}else{
				LOGGER.error("Exception occured in getBooks"+e.getMessage());
				response = new Response("Failed",e.getStatusCode(),null,"Request failed");
			}
		}
		return response;
	}


}


