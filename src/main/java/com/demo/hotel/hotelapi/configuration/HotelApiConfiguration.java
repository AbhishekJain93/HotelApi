package com.demo.hotel.hotelapi.configuration;

import java.io.File;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.demo.hotel.hotelapi.model.HotelModel;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.google.common.collect.HashMultimap;

@Configuration
public class HotelApiConfiguration extends WebMvcConfigurationSupport {

	static Logger log = Logger.getLogger(HotelApiConfiguration.class.getName());

	@Value(value = "classpath:hoteldb.csv")
	private Resource hotelCsv;

	private HashMultimap<String, HotelModel> hotelDb = HashMultimap.create();

	@Override
	public FormattingConversionService mvcConversionService() {
		FormattingConversionService f = super.mvcConversionService();
		f.addConverter(new SortOrderConverter());
		return f;
	}

	@PostConstruct
	private void setupHotelDb() {
		try {
			CsvSchema bootstrapSchema = CsvSchema.emptySchema().withHeader();
			CsvMapper mapper = new CsvMapper();
			File file = hotelCsv.getFile();
			MappingIterator<HotelModel> readValues = mapper.readerFor(HotelModel.class).with(bootstrapSchema)
					.readValues(file);

			while (readValues.hasNext()) {

				HotelModel hotel = readValues.next();
				hotelDb.put(hotel.city, hotel);
			}

		} catch (Exception e) {

			log.error("Exception occured while reading hotel values from csv file : hoteldb.csv", e);

		}

	}

	public HashMultimap<String, HotelModel> getHotelDb() {
		return hotelDb;
	}

}
