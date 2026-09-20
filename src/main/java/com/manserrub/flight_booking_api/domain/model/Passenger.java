package com.manserrub.flight_booking_api.domain.model;

import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Represents a passenger domain model.
 * This is a domain entity with no Spring framework dependencies.
 */
public class Passenger {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );
    private static final int MIN_AGE = 18;

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String passportNumber;
    private LocalDate birthDate;

    /**
     * Default constructor required for framework instantiation.
     */
    public Passenger() {
    }

    /**
     * Constructor with all fields.
     *
     * @param id the passenger ID
     * @param firstName the first name (not null)
     * @param lastName the last name (not null)
     * @param email the email (valid email format, not null)
     * @param passportNumber the passport number (unique, not null)
     * @param birthDate the birth date (must be 18+ years old)
     */
    public Passenger(Long id, String firstName, String lastName, String email, String passportNumber, LocalDate birthDate) {
        this.id = id;
        this.firstName = Objects.requireNonNull(firstName, "First name cannot be null");
        this.lastName = Objects.requireNonNull(lastName, "Last name cannot be null");
        this.email = validateEmail(email);
        this.passportNumber = Objects.requireNonNull(passportNumber, "Passport number cannot be null");
        this.birthDate = validateBirthDate(birthDate);
    }

    /**
     * Constructor without ID (for new passengers).
     */
    public Passenger(String firstName, String lastName, String email, String passportNumber, LocalDate birthDate) {
        this(null, firstName, lastName, email, passportNumber, birthDate);
    }

    /**
     * Validates email format.
     */
    private static String validateEmail(String email) {
        Objects.requireNonNull(email, "Email cannot be null");
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Invalid email format: " + email);
        }
        return email;
    }

    /**
     * Validates that passenger is at least 18 years old.
     */
    private static LocalDate validateBirthDate(LocalDate birthDate) {
        Objects.requireNonNull(birthDate, "Birth date cannot be null");
        LocalDate today = LocalDate.now();
        int age = Period.between(birthDate, today).getYears();
        if (age < MIN_AGE) {
            throw new IllegalArgumentException("Passenger must be at least " + MIN_AGE + " years old");
        }
        return birthDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = Objects.requireNonNull(firstName, "First name cannot be null");
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = Objects.requireNonNull(lastName, "Last name cannot be null");
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = validateEmail(email);
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = Objects.requireNonNull(passportNumber, "Passport number cannot be null");
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = validateBirthDate(birthDate);
    }

    /**
     * Gets the passenger's full name.
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }

    /**
     * Gets the passenger's age.
     */
    public int getAge() {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Passenger passenger = (Passenger) o;
        return Objects.equals(id, passenger.id) &&
                Objects.equals(passportNumber, passenger.passportNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, passportNumber);
    }

    @Override
    public String toString() {
        return "Passenger{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", passportNumber='" + passportNumber + '\'' +
                ", birthDate=" + birthDate +
                ", age=" + getAge() +
                '}';
    }
}
