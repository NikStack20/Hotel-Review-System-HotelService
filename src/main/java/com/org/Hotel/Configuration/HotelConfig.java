package com.org.Hotel.Configuration;

import org.modelmapper.ModelMapper; 
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class HotelConfig {

	@Bean
	ModelMapper modelMapper() {
		return new ModelMapper();
	}
	@Configuration
	public class HttpClientConfig {

	    @Bean
	     WebClient webClient(WebClient.Builder builder) {
	        return builder
	                .baseUrl("http://localhost:7060")   // your rating service base
	                .build();
	    }
	}

}
