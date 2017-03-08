package com.demo.hotel.hotelapi.controller;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.demo.hotel.hotelapi.aspect.RateLimiter;
import com.demo.hotel.hotelapi.enums.SortOrder;
import com.demo.hotel.hotelapi.model.GetHotelListResponse;
import com.demo.hotel.hotelapi.service.HotelApiService;

@RestController
public class HotelApiController {

	static Logger log = Logger.getLogger(HotelApiController.class.getName());
	@Autowired
	private HotelApiService hotelService;

	@RateLimiter(apiName = "getHotels")
	@RequestMapping(value = "/cities/{cityId}/hotels", method = RequestMethod.GET)
	public GetHotelListResponse getHotelList(@RequestParam(value = "apikey", required = false) String key,
			@PathVariable String cityId, @RequestParam(value = "sort", required = false) SortOrder order) {

		log.info("Get hotel list api called for city : " + cityId + ", apiKey : " + key + ", sorting order : " + order);
		GetHotelListResponse response = new GetHotelListResponse();
		response.hotelList = hotelService.getHotelsForCity(cityId, order);
		response.status = "OK";
		return response;

	}

}
