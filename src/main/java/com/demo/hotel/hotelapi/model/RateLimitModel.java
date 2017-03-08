package com.demo.hotel.hotelapi.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class RateLimitModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4950110988203331021L;
	public int currentCounter;
	public int counter;
	public Timestamp nextFrame;
	public boolean suspended;

}
