package com.demo.hotel.hotelapi.aspect;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.demo.hotel.hotelapi.configuration.RateLimitConfiguration;
import com.demo.hotel.hotelapi.model.GetHotelListResponse;

@Component
@Aspect
@Order(value = Ordered.HIGHEST_PRECEDENCE + 1)
public class RateLimitAspect {

	static Logger log = Logger.getLogger(RateLimitAspect.class.getName());

	@Autowired
	private RateLimitConfiguration rateLimitConfiguration;

	@Around("within(com.demo.hotel.hotelapi.controller..*) && @annotation(api) && args(apiKey,..)")
	public Object limitApiRate(ProceedingJoinPoint pjp, RateLimiter api, String apiKey) throws Throwable {

		String key = StringUtils.isEmpty(apiKey) ? api.apiName() : apiKey;
		if (rateLimitConfiguration.incrementCounter(key)) {

			return pjp.proceed();

		} else {

			log.info("Api limit reached for api key : " + key);
			GetHotelListResponse response = new GetHotelListResponse();
			response.status = "FORBIDDEN";
			response.errorCode = "403";
			response.errorMessage = "Api rate limit exceeded";
			return response;
		}
	}
}