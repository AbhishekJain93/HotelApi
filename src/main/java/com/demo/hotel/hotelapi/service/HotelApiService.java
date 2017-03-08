package com.demo.hotel.hotelapi.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.hotel.hotelapi.configuration.HotelApiConfiguration;
import com.demo.hotel.hotelapi.enums.SortOrder;
import com.demo.hotel.hotelapi.model.HotelModel;

@Service
public class HotelApiService {

	@Autowired
	private HotelApiConfiguration hotelConfig;

	@Autowired
	private Comparator<HotelModel> hotelComparator;

	public List<HotelModel> getHotelsForCity(String cityId, SortOrder sortOrder) {

		List<HotelModel> hotelList = new ArrayList<>(hotelConfig.getHotelDb().get(cityId));

		if (SortOrder.ASC.equals(sortOrder)) {

			Collections.sort(hotelList, hotelComparator);

		} else if (SortOrder.DESC.equals(sortOrder)) {

			Collections.sort(hotelList, Collections.reverseOrder(hotelComparator));

		}
		return hotelList;
	}
}
