package com.example.maps_service.service.impl;

import com.example.maps_service.dto.CoordinatesDto;
import com.example.maps_service.service.MapsService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MapsServiceImpl implements MapsService {

    @Value("${maps.api.key}")
    private String API_KEY;

    @Override
    public CoordinatesDto getCoordinates(String address) {
        String url = String.format(
                "https://us1.locationiq.com/v1/search?key=%s&q=%s&format=json",
                API_KEY, address.replace(" ", "+"));

        RestTemplate restTemplate = new RestTemplate();

        String response = restTemplate.getForObject(url, String.class);

        JSONArray result = new JSONArray(response);

        if (result.length() > 0) {
            JSONObject location = result.getJSONObject(0);
            double lat = Double.parseDouble(location.getString("lat"));
            double lon = Double.parseDouble(location.getString("lon"));
            String displayName = location.getString("display_name");
            return new CoordinatesDto(lat, lon,displayName);

        } else {
            throw new IllegalArgumentException("No results found for the given address: " + address);
        }
    }

    @Override
    public String getAddressFromCoordinates(double latitude, double longitude) {
        {

            String url = String.format(
                    "https://us1.locationiq.com/v1/reverse?key=%s&lat=%f&lon=%f&format=json",
                    API_KEY, latitude, longitude);

            RestTemplate restTemplate = new RestTemplate();

            String response = restTemplate.getForObject(url, String.class);

            JSONObject result = new JSONObject(response);

            if (result.has("display_name")) {
                return result.getString("display_name");
            } else {
                throw new IllegalArgumentException("No address found for the given coordinates: " + latitude + ", " + longitude);
            }
        }

    }
}
