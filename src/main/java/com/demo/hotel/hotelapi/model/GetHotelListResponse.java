package com.demo.hotel.hotelapi.model;

import java.io.Serializable;
import java.util.List;

public class GetHotelListResponse implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3918744166044135974L;
	public List<HotelModel> hotelList;
	public String errorCode;
	public String errorMessage;
	public String status;

}
