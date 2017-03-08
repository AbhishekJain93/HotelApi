package com.demo.hotel.hotelapi.configuration;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.demo.hotel.hotelapi.model.RateLimitModel;

@Component
public class RateLimitConfiguration {

	static Logger log = Logger.getLogger(RateLimitConfiguration.class.getName());

	@Value("#{${ratelimit.map}}")
	private Map<String, String> apiRateMap = new HashMap<>();

	@Value("${ratelimit.globalrate:1P1S}")
	private String defaultRate;

	@Value("${ratelimit.suspensiontime.minutes:5}")
	private int suspensionMinutes;

	private Map<String, RateLimitModel> runtimeConfig = new HashMap<>();

	public Map<String, String> getMap() {
		return this.apiRateMap;
	}

	@PostConstruct
	private void init() {
		Set<Entry<String, String>> entrySet = apiRateMap.entrySet();

		for (Map.Entry<String, String> entry : entrySet) {

			String apiName = entry.getKey();

			RateLimitModel rateLimitModel = new RateLimitModel();
			rateLimitModel.currentCounter = 0;
			rateLimitModel.counter = getMaxCounter(apiName, defaultRate);
			rateLimitModel.nextFrame = getNextTimeFrame(apiName, defaultRate);
			runtimeConfig.put(entry.getKey(), rateLimitModel);

		}

	}

	public boolean incrementCounter(String apiName) {

		RateLimitModel rateLimitModel = runtimeConfig.get(apiName);

		if (rateLimitModel == null) {

			log.info(" Api rate not configured for apiKey : " + apiName + ". So falling back to default rate : "
					+ defaultRate);

			rateLimitModel = new RateLimitModel();
			rateLimitModel.currentCounter = 1;
			rateLimitModel.counter = getMaxCounter(apiName, defaultRate);
			rateLimitModel.nextFrame = getNextTimeFrame(apiName, defaultRate);
			runtimeConfig.put(apiName, rateLimitModel);
			return true;

		}

		Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
		if (rateLimitModel.nextFrame.before(currentTimestamp)) {

			log.info(" Api called after the time frame has renewed for apiKey : " + apiName);

			rateLimitModel.currentCounter = 1;
			rateLimitModel.counter = getMaxCounter(apiName, defaultRate);
			rateLimitModel.nextFrame = getNextTimeFrame(apiName, defaultRate);
			rateLimitModel.suspended = false;
			return true;

		} else {

			if (rateLimitModel.currentCounter + 1 <= rateLimitModel.counter) {
				log.info(" Api called within the time frame for apiKey : " + apiName
						+ ". So incrementing the counter for the api key.");

				rateLimitModel.currentCounter++;
				return true;

			} else {

				if (rateLimitModel.suspended) {
					log.info(" Api called when in SUSPENDED state for apiKey : " + apiName
							+ ". So blocking the call to the api.");

					return false;
				}
				rateLimitModel.currentCounter = getMaxCounter(apiName, defaultRate);
				rateLimitModel.counter = getMaxCounter(apiName, defaultRate);
				rateLimitModel.nextFrame = new Timestamp(System.currentTimeMillis() + suspensionMinutes * 60 * 1000);
				rateLimitModel.suspended = true;

				log.info(" Api counter has exceeded the permissible value for apiKey : " + apiName
						+ ". So suspending the api key for " + suspensionMinutes + " minutes.");

				return false;

			}
		}
	}

	public int getMaxCounter(String apiName, String defaultRate) {

		String rateString = StringUtils.isEmpty(apiRateMap.get(apiName)) ? defaultRate : apiRateMap.get(apiName);
		String[] parts = rateString.split("[A-Z]");

		return Integer.parseInt(parts[0]);

	}

	public Timestamp getNextTimeFrame(String apiName, String defaultRate) {

		String rateString = StringUtils.isEmpty(apiRateMap.get(apiName)) ? defaultRate : apiRateMap.get(apiName);
		String[] parts = rateString.split("[A-Z]");

		int multiplier = Integer.parseInt(parts[1]);
		char timeUnit = rateString.charAt(rateString.length() - 1);
		switch (timeUnit) {

		case 'S':
		default:
			return new Timestamp(System.currentTimeMillis() + multiplier * 1000);
		case 'M':
			return new Timestamp(System.currentTimeMillis() + multiplier * 60 * 1000);
		case 'H':
			return new Timestamp(System.currentTimeMillis() + multiplier * 60 * 60 * 1000);

		}

	}

}
