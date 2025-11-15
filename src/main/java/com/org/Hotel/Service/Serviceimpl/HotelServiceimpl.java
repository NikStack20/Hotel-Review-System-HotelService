package com.org.Hotel.Service.Serviceimpl;
import java.time.Duration;  
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.org.Hotel.Service.GlobalExceptionHandler.DBExceptions;
import com.org.Hotel.Service.entities.Hotel;
import com.org.Hotel.Service.loadouts.HotelDto;
import com.org.Hotel.Service.loadouts.RatingDto;
import com.org.Hotel.Service.repository.HotelRepo;
import com.org.Hotel.Service.service.HotelService;

import reactor.core.publisher.Mono;
 

@Service
public class HotelServiceimpl implements HotelService {

	@Autowired
	private HotelRepo hotelRepo;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
    private WebClient webClient;
	
	private Logger logger = LoggerFactory.getLogger(HotelServiceimpl.class);
    
    
	
	@Override
    public HotelDto createHotel(HotelDto hotelDto) {
	      Hotel hotel = this.modelMapper.map(hotelDto, Hotel.class);
	   // 3) Generate ID if missing
			if (hotel.getHotelId() == null || hotel.getHotelId().isBlank()) {
				hotel.setHotelId(UUID.randomUUID().toString());
			}
		
	      Hotel saved = this.hotelRepo.save(hotel);
	      
	    return this.modelMapper.map(saved, HotelDto.class) ;
	}

	@Override
	public List<HotelDto> getAllHotels() {
		   List<Hotel> hotels = this.hotelRepo.findAll();
		   List<HotelDto> hotelDtos = hotels.stream().map((hotel) -> this.modelMapper.map(hotel, HotelDto.class)).collect(Collectors.toList());
		return hotelDtos;
	}

	@Override
	public HotelDto getHotel(String hotelId) {

	    Hotel hotel = hotelRepo.findById(hotelId)
	            .orElseThrow(() -> new DBExceptions("Hotel not found"));

	    // defensive check
	    if (webClient == null) {
	        logger.error("webClient is null - bean not configured");
	        
	        HotelDto dtoNoClient = modelMapper.map(hotel, HotelDto.class);
	        dtoNoClient.setRatings(Collections.emptyList());
	        return dtoNoClient;
	    }

	    List<RatingDto> ratingDtos = Collections.emptyList();

	    try {
	        ratingDtos = webClient.get()
	            .uri("/ratings/getAllByHotelId/{hotelId}", hotelId)   //   actual path for getAllRatings with hotel
	            .retrieve()
	            .onStatus(
	                status -> status.is4xxClientError() || status.is5xxServerError(),
	                resp -> resp.createException().flatMap(Mono::error)
	                            .defaultIfEmpty(resp.statusCode().toString())
	                            // NOTE:- the explicit generic <Throwable> to satisfy the compiler we added
	                            .flatMap(body -> Mono.<Throwable>error(
	                                  new DBExceptions("Ratings service error: " 
	                                                        + resp.statusCode() + " -> " + body)))
	            )
	            .bodyToFlux(RatingDto.class)
	            .collectList()
	            .timeout(Duration.ofSeconds(5))
	            .block();
	    } catch (Exception ex) {
	        logger.warn("Could not fetch ratings for hotel {} : {}", hotelId, ex.toString());
	        ratingDtos = Collections.emptyList();
	    }
	    if (ratingDtos == null) ratingDtos = Collections.emptyList();

	    HotelDto dto = modelMapper.map(hotel, HotelDto.class);
	    dto.setRatings(ratingDtos);
	    return dto;

	}



}
