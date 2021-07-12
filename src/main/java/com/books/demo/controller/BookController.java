package com.books.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.books.demo.Payload.Response;
import com.books.demo.service.BookService;

@RestController
@RequestMapping("books-api")
public class BookController{
	
	@Autowired
	private static final Logger LOGGER=LoggerFactory.getLogger(BookService.class);
	@Autowired
	private BookService bookService;

	@GetMapping("book/{id}")
	ResponseEntity<Response> getBookById(@PathVariable("id") Integer id){
		Response response = null;
		ResponseEntity<Response> responseEntity = null;
		try {
			response= bookService.getBook(id,bookService.getAccessToken());
			if(response != null && response.getStatus() != null && response.getStatus().equalsIgnoreCase("Success")) 
				responseEntity = new ResponseEntity<>(response,HttpStatus.OK);
			else
				responseEntity = new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
		}
		catch(Exception e) {
			LOGGER.error("Exception occured in getBookById:"+e.getMessage());
			responseEntity = new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
		}
		return responseEntity;

	}

	@GetMapping("books")
	ResponseEntity<Response> getAllBooks(){
		Response response = null;
		ResponseEntity<Response> responseEntity = null;
		try {
			response=bookService.getBooks(bookService.getAccessToken());
			if(response != null && response.getStatus() != null && response.getStatus().equalsIgnoreCase("Success")) 
				responseEntity = new ResponseEntity<>(response,HttpStatus.OK);
			else
				responseEntity = new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
		}
		catch(Exception e) {
			LOGGER.error("Exception occured in getBookById:"+e.getMessage());
			responseEntity = new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
		}
		return responseEntity;	
	}
}
