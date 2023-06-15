package com.fantasy.fantasyapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootApplication
public class FantasyApiApplication {

	@Bean
	public WebClient.Builder getRestTemplate()
	{
		return WebClient.builder();
	}

	@Bean
	public ObjectMapper getDefaultObjectMapper()
	{
		return new ObjectMapper();
	}

	public static void main(String[] args) 
	{
		SpringApplication.run(FantasyApiApplication.class, args);
	}
}
