package com.org.Hotel.Services;

import java.util.List;

import com.org.Hotel.loadouts.HotelDto;

public interface HotelService {

	// create
	HotelDto createHotel(HotelDto hotelDto);

	// getAll
	List<HotelDto> getAllHotels();

	// getSingleHotel
	HotelDto getHotel(String hotelId);
	
	

}
