package com.demo.hotel.hotelapi.model;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HotelModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5415851874952600120L;

	@JsonProperty("CITY")
	public String city;

	@JsonProperty("HOTELID")
	public String hotelId;

	@JsonProperty("ROOM")
	public String room;

	@JsonProperty("PRICE")
	public BigDecimal price;

}
