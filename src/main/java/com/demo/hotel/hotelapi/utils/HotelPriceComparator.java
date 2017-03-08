package com.demo.hotel.hotelapi.utils;

import java.util.Comparator;

import org.springframework.stereotype.Component;

import com.demo.hotel.hotelapi.model.HotelModel;

@Component
public class HotelPriceComparator implements Comparator<HotelModel> {

	@Override
	public int compare(HotelModel o1, HotelModel o2) {

		if (o1 == null || o2 == null)
			return 0;

		return o1.price.compareTo(o2.price);
	}

}
