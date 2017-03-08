package com.demo.hotel.hotelapi.configuration;

import org.springframework.core.convert.converter.Converter;

import com.demo.hotel.hotelapi.enums.SortOrder;

public class SortOrderConverter implements Converter<String, SortOrder> {

	@Override
	public SortOrder convert(String arg0) {

		return SortOrder.getSortOrder(arg0);
	}

}
