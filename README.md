API
======
  1. Search hotels by CityId
  2. Provide optional sorting of the result by Price (both ASC and DESC order).
  3. RateLimit: API calls need to be rate limited (request per 10 seconds) based on API Key provided in each http call.
  
HTTP Request Format
====================

http://localhost:8080/cities/Bangkok/hotels?apikey=myApiKey&sort=ASC

HTTP Response Format
=====================
{
	"hotelList": [{
		"CITY": "Bangkok",
		"HOTELID": "11",
		"ROOM": "Deluxe",
		"PRICE": 60
	}, {
		"CITY": "Bangkok",
		"HOTELID": "15",
		"ROOM": "Deluxe",
		"PRICE": 900
	}, {
		"CITY": "Bangkok",
		"HOTELID": "1",
		"ROOM": "Deluxe",
		"PRICE": 1000
	}],
	"errorCode": null,
	"errorMessage": null,
	"status": "OK"
}

In case of rate limit exceeded

{"hotelList":null,"errorCode":"403","errorMessage":"Api rate limit exceeded","status":"FORBIDDEN"}

Assumptions
============
  1. sort query parameter can be ASC, DESC. It is optional. Any other value is dicarded
  2. apikey is passed as query parameter. If not passed a default key is used. If unknown key is passed it is added to runtime configuration with default settings.
  3. Checksum/Signature corresponding to api key for request generation and validation is skipped for now.
  4. hotel db is in hoteldb.csv file in src/main/resources
  5. ratelimit.properties contain api rate limiter configurations
  6. UTs are not implemented yet.

