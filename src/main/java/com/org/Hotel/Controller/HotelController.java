package com.org.Hotel.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.org.Hotel.Services.HotelService;
import com.org.Hotel.loadouts.HotelDto;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/hotels")
public class HotelController {
	
	@Autowired
	private HotelService hotelService;
	
	
	   //create
	@PostMapping("/create")
	ResponseEntity<HotelDto> createHotel(@Valid @RequestBody HotelDto hotelDto) {
	      return new ResponseEntity<HotelDto>(this.hotelService.createHotel(hotelDto), HttpStatus.CREATED) ;
	}

	
	//get single
	@GetMapping("/getHotel/{hotelId}") 
	ResponseEntity<HotelDto> getHotel(@PathVariable String hotelId) {
		return new ResponseEntity<HotelDto> (this.hotelService.getHotel(hotelId), HttpStatus.OK);
		
	}
	
	//get all
	@GetMapping("/") 
	ResponseEntity<List<HotelDto>> getAll() {
		  return new ResponseEntity<List<HotelDto>> (this.hotelService.getAllHotels(), HttpStatus.OK);
	}


}
