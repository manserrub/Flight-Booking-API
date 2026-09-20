package com.manserrub.flight_booking_api.domain.exception;

/**
 * Exception thrown when passenger data is invalid.
 */
public class InvalidPassengerDataException extends RuntimeException {
    public InvalidPassengerDataException(String message) {
        super(message);
    }

    public InvalidPassengerDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public static InvalidPassengerDataException invalidEmail(String email) {
        return new InvalidPassengerDataException("Invalid email format: " + email);
    }

    public static InvalidPassengerDataException invalidAge(int age) {
        return new InvalidPassengerDataException("Passenger must be at least 18 years old, but is " + age);
    }

    public static InvalidPassengerDataException duplicatePassport(String passportNumber) {
        return new InvalidPassengerDataException("Passenger with passport number '" + passportNumber + "' already exists");
    }

    public static InvalidPassengerDataException missingRequiredField(String fieldName) {
        return new InvalidPassengerDataException("Required field is missing: " + fieldName);
    }
}
