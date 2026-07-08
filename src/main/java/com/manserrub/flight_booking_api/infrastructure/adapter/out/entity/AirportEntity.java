package com.manserrub.flight_booking_api.infrastructure.adapter.out.entity;

import jakarta.persistence.*;

@Entity
@Table(name="airport")
public class AirportEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "iata_code", unique = true, nullable = false, length = 3)
    private String iataCode;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String country;

    public AirportEntity(Long id, String iataCode, String name, String city, String country) {
        this.id = id;
        this.iataCode = iataCode;
        this.name = name;
        this.city = city;
        this.country = country;
    }

    public AirportEntity() {
    }

    public Long getId() { return id; }
    public String getIataCode() { return iataCode; }
    public String getName() { return name; }
    public String getCity() { return city; }
    public String getCountry() { return country; }

    public void setId(Long id) { this.id = id; }
    public void setIataCode(String iataCode) { this.iataCode = iataCode; }
    public void setName(String name) { this.name = name; }
    public void setCity(String city) { this.city = city; }
    public void setCountry(String country) { this.country = country; }

}
