package com.manserrub.flight_booking_api.domain.model;

public class Airport {

    private Long id;
    private String iataCode;
    private String name;
    private String city;
    private String country;

    public Airport(Long id, String iataCode, String name, String city, String country) {
        this.id = id;
        this.iataCode = iataCode;
        this.name = name;
        this.city = city;
        this.country = country;
    }

    public Long getId() { return id; }
    public String getIataCode() { return iataCode; }
    public String getName() { return name; }
    public String getCity() { return city; }
    public String getCountry() { return country; }


}