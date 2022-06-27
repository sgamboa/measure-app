package com.example.measure.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Response {

  private String lon;
  private String lat;

  public Response() {
  }

  public String getLon() {
    return this.lon;
  }

  public String getLat() {
    return this.lat;
  }

  public void setLon(String lon) {
    this.lon = lon;
  }

  public void setLat(String lat) {
    this.lat = lat;
  }

}

