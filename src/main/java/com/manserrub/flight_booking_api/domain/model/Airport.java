package com.manserrub.flight_booking_api.domain.model;

import java.util.Objects;

/**
 * Represents an airport domain model.
 * This is a domain entity with no Spring framework dependencies.
 */
public class Airport {

    private Long id;
    private String iataCode;
    private String name;
    private String city;
    private String country;

    /**
     * Default constructor required for framework instantiation.
     */
    public Airport() {
    }

    /**
     * Constructor with all fields.
     *
     * @param id the airport ID
     * @param iataCode the IATA code (3-4 characters, unique, not null)
     * @param name the airport name (not null)
     * @param city the city name (not null)
     * @param country the country name (not null)
     */
    public Airport(Long id, String iataCode, String name, String city, String country) {
        this.id = id;
        this.iataCode = validateIataCode(iataCode);
        this.name = Objects.requireNonNull(name, "Airport name cannot be null");
        this.city = Objects.requireNonNull(city, "City cannot be null");
        this.country = Objects.requireNonNull(country, "Country cannot be null");
    }

    /**
     * Constructor without ID (for new airports).
     */
    public Airport(String iataCode, String name, String city, String country) {
        this(null, iataCode, name, city, country);
    }

    /**
     * Validates IATA code format.
     * IATA codes are 3-4 uppercase characters.
     */
    private static String validateIataCode(String iataCode) {
        Objects.requireNonNull(iataCode, "IATA code cannot be null");
        if (iataCode.length() < 3 || iataCode.length() > 4) {
            throw new IllegalArgumentException("IATA code must be 3-4 characters");
        }
        if (!iataCode.matches("[A-Z]{3,4}")) {
            throw new IllegalArgumentException("IATA code must contain only uppercase letters");
        }
        return iataCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIataCode() {
        return iataCode;
    }

    public void setIataCode(String iataCode) {
        this.iataCode = validateIataCode(iataCode);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = Objects.requireNonNull(name, "Airport name cannot be null");
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = Objects.requireNonNull(city, "City cannot be null");
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = Objects.requireNonNull(country, "Country cannot be null");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Airport airport = (Airport) o;
        return Objects.equals(id, airport.id) &&
                Objects.equals(iataCode, airport.iataCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, iataCode);
    }

    @Override
    public String toString() {
        return "Airport{" +
                "id=" + id +
                ", iataCode='" + iataCode + '\'' +
                ", name='" + name + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}