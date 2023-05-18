package com.example.service.impl;

import com.example.service.WeatherDataService;
import com.example.vo.WeatherResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@Service("weatherDataService")
@Slf4j
public class WeatherDataServiceImpl implements WeatherDataService {
    @Resource
    private RestTemplate restTemplate;

    private final String WEATHER_API = "http://wthrcdn.etouch.cn/weather_mini";

    @Override
    public WeatherResponse getDataByCityId(String cityId) {
        String uri = WEATHER_API + "?citykey=" + cityId;
        return this.doGetWeatcherData(uri);
    }

    @Override
    public WeatherResponse getDataByCityName(String cityName) {
        String uri = WEATHER_API + "?city=" + cityName;
        return this.doGetWeatcherData(uri);
    }

    private WeatherResponse doGetWeatcherData(String uri) {
        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
        String strBody = null;
        if (response.getStatusCodeValue() == 200) {
            strBody = response.getBody();
        }

        ObjectMapper mapper = new ObjectMapper();
        WeatherResponse weatherResponse = null;

        try {
            weatherResponse = mapper.readValue(strBody, WeatherResponse.class);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }

        return weatherResponse;
    }
}
