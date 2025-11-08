package com.org.Hotel.Serviceimpl;
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
import com.org.Hotel.Repository.HotelRepo;
import com.org.Hotel.Service.entities.Hotel;
import com.org.Hotel.Services.HotelService;
import com.org.Hotel.loadouts.HotelDto;
import com.org.Hotel.loadouts.RatingDto;
 

@Service
public class HotelServiceimpl implements HotelService {

	@Autowired
	private HotelRepo hotelRepo;

	@Autowired
	private ModelMapper modelMapper;

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
	            .orElseThrow(() -> new RuntimeException("Hotel not found"));

	    // ---- Call Rating Service to get ratings of this hotel ----
	    List<RatingDto> ratingDtos = webClient.get()
	            .uri("hotels/getHotel/{hotelId}", hotelId)
	            .retrieve()
	            .bodyToFlux(RatingDto.class)
	            .collectList()
	            .timeout(Duration.ofSeconds(3))
	            .onErrorReturn(Collections.emptyList())
	            .block();

	    logger.info("Ratings fetched for hotel {} -> {}", hotelId, ratingDtos.size());

	    // map Hotel → HotelDto
	    HotelDto dto = modelMapper.map(hotel, HotelDto.class);
	    dto.setRatings(ratingDtos);
	    return dto;
	}


}
