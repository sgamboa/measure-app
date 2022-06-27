package com.example.measure.service;

import com.example.measure.entity.Measure;
import com.example.measure.repository.IMeasureRepository;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.measure.client.Response;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MeasureService implements IMeasureService {

    private IMeasureRepository measureRepository;

    final String uri = "https://nominatim.openstreetmap.org/search?q=";

    final RestTemplate restTemplate = new RestTemplate();

    public MeasureService(IMeasureRepository measureRepository) {
        this.measureRepository = measureRepository;
    }
 
    @Override
    public List<Measure> getAllMeasures() {
        return measureRepository.findAll();
    }

    @Override
    public Measure saveNewMeasure(Measure measure) {
        measure.setDistance(calculateDistance(measure));

        return measureRepository.save(measure);
    }

    public Double calculateDistance(Measure measure) {
        Response origin = callForLonAndLat(measure.getOrigin());
        Response destination = callForLonAndLat(measure.getDestination());

        //Using Haversine formula to calculate de distance
        return calculateDistanceWithHaversineFormula(Double.parseDouble(origin.getLat()),
            Double.parseDouble(destination.getLat()),
            Double.parseDouble(origin.getLon()),
            Double.parseDouble(destination.getLon()));
    }

    private double calculateDistanceWithHaversineFormula(double lat1, double lat2, double lon1, double lon2) {
        int R = 6371; // Radius of the earth in km
        double dLat = pointByFactor(lat2-lat1);  // deg2rad below
        double dLon = pointByFactor(lon2-lon1);
        double a =
            Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(pointByFactor(lat1)) * Math.cos(pointByFactor(lat2)) *
                    Math.sin(dLon/2) * Math.sin(dLon/2)
            ;
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = R * c; // Distance in km
        return d;
    }

    private double pointByFactor(double point) {
        return point * (Math.PI/180);
    }

    private Response callForLonAndLat(String address){
        HttpRequest request = HttpRequest
            .newBuilder()
            .uri(URI.create(uri+ URLEncoder.encode(address, StandardCharsets.UTF_8)+"&format=json"))
            .method("GET", HttpRequest.BodyPublishers.noBody())
            .build();
        HttpResponse<String> response = null;

        try {
            response = HttpClient
                .newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        String responseString = response.body();
        System.out.println(responseString);

        Response[] data = null;
        try {
            data = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .readValue(responseString, Response[].class);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println(data);
        return data[0];
    }
    
}
