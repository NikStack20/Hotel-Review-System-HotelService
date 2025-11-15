package com.org.Hotel.Service.service;

import java.util.List; 

import com.org.Hotel.Service.loadouts.HotelDto;

public interface HotelService {

	// create
	HotelDto createHotel(HotelDto hotelDto);

	// getAll
	List<HotelDto> getAllHotels();

	// getSingleHotel
	HotelDto getHotel(String hotelId);
	
	

}
