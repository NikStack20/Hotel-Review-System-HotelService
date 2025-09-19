package com.org.Hotel.Serviceimpl;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.org.Hotel.GlobalExceptionHandler.DBExceptions;
import com.org.Hotel.Repository.HotelRepo;
import com.org.Hotel.Service.entities.Hotel;
import com.org.Hotel.Services.HotelService;
import com.org.Hotel.loadouts.HotelDto;
 

@Service
public class HotelServiceimpl implements HotelService {

	@Autowired
	private HotelRepo hotelRepo;

	@Autowired
	private ModelMapper modelMapper;
	
	
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
		Hotel hotel = this.hotelRepo.findById(hotelId).orElseThrow(() -> new DBExceptions("Hotel with given hotelId:" + hotelId + ", not Found on server x_X"));
		return this.modelMapper.map(hotel, HotelDto.class);
	}

}
