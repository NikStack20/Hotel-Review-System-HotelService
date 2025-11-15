package com.org.Hotel.Service.loadouts;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatingDto {

	private String ratingId;
	private String userId;
	private String hotelId;
	private int rating;
	private String feedback;

}
