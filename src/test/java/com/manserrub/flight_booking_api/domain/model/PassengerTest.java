package com.manserrub.flight_booking_api.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Passenger Domain Model Tests")
class PassengerTest {

    private final LocalDate validBirthDate = LocalDate.now().minusYears(30);
    private final LocalDate invalidBirthDate = LocalDate.now().minusYears(17);

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("should create passenger with valid data")
        void shouldCreatePassengerWithValidData() {
            var passenger = new Passenger(1L, "John", "Doe", "john@example.com", "A123456", validBirthDate);

            assertThat(passenger).isNotNull();
            assertThat(passenger.getId()).isEqualTo(1L);
            assertThat(passenger.getFirstName()).isEqualTo("John");
            assertThat(passenger.getLastName()).isEqualTo("Doe");
            assertThat(passenger.getEmail()).isEqualTo("john@example.com");
            assertThat(passenger.getPassportNumber()).isEqualTo("A123456");
            assertThat(passenger.getBirthDate()).isEqualTo(validBirthDate);
        }

        @Test
        @DisplayName("should create passenger without ID")
        void shouldCreatePassengerWithoutId() {
            var passenger = new Passenger("Jane", "Smith", "jane@example.com", "B789012", validBirthDate);

            assertThat(passenger).isNotNull();
            assertThat(passenger.getId()).isNull();
            assertThat(passenger.getFirstName()).isEqualTo("Jane");
        }

        @Test
        @DisplayName("should throw exception when first name is null")
        void shouldThrowExceptionWhenFirstNameIsNull() {
            assertThatThrownBy(() -> new Passenger(1L, null, "Doe", "john@example.com", "A123456", validBirthDate))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("First name cannot be null");
        }

        @Test
        @DisplayName("should throw exception when last name is null")
        void shouldThrowExceptionWhenLastNameIsNull() {
            assertThatThrownBy(() -> new Passenger(1L, "John", null, "john@example.com", "A123456", validBirthDate))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("Last name cannot be null");
        }

        @Test
        @DisplayName("should throw exception when email is null")
        void shouldThrowExceptionWhenEmailIsNull() {
            assertThatThrownBy(() -> new Passenger(1L, "John", "Doe", null, "A123456", validBirthDate))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("Email cannot be null");
        }

        @Test
        @DisplayName("should throw exception when email format is invalid")
        void shouldThrowExceptionWhenEmailFormatIsInvalid() {
            assertThatThrownBy(() -> new Passenger(1L, "John", "Doe", "invalid-email", "A123456", validBirthDate))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Invalid email format: invalid-email");

            assertThatThrownBy(() -> new Passenger(1L, "John", "Doe", "john@", "A123456", validBirthDate))
                    .isInstanceOf(IllegalArgumentException.class);

            assertThatThrownBy(() -> new Passenger(1L, "John", "Doe", "john@example", "A123456", validBirthDate))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("should throw exception when passport number is null")
        void shouldThrowExceptionWhenPassportNumberIsNull() {
            assertThatThrownBy(() -> new Passenger(1L, "John", "Doe", "john@example.com", null, validBirthDate))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("Passport number cannot be null");
        }

        @Test
        @DisplayName("should throw exception when birth date is null")
        void shouldThrowExceptionWhenBirthDateIsNull() {
            assertThatThrownBy(() -> new Passenger(1L, "John", "Doe", "john@example.com", "A123456", null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("Birth date cannot be null");
        }

        @Test
        @DisplayName("should throw exception when passenger is less than 18 years old")
        void shouldThrowExceptionWhenPassengerIsLessThan18YearsOld() {
            assertThatThrownBy(() -> new Passenger(1L, "John", "Doe", "john@example.com", "A123456", invalidBirthDate))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Passenger must be at least 18 years old");
        }

        @Test
        @DisplayName("should accept valid email formats")
        void shouldAcceptValidEmailFormats() {
            var validEmails = new String[]{
                    "user@example.com",
                    "user.name@example.com",
                    "user+tag@example.co.uk",
                    "user_name@example.org"
            };

            for (String email : validEmails) {
                var passenger = new Passenger("John", "Doe", email, "A123456", validBirthDate);
                assertThat(passenger.getEmail()).isEqualTo(email);
            }
        }
    }

    @Nested
    @DisplayName("getFullName Tests")
    class GetFullNameTests {

        @Test
        @DisplayName("should return concatenated first and last name")
        void shouldReturnConcatenatedFirstAndLastName() {
            var passenger = new Passenger(1L, "John", "Doe", "john@example.com", "A123456", validBirthDate);

            assertThat(passenger.getFullName()).isEqualTo("John Doe");
        }
    }

    @Nested
    @DisplayName("getAge Tests")
    class GetAgeTests {

        @Test
        @DisplayName("should calculate correct age")
        void shouldCalculateCorrectAge() {
            var birthDate = LocalDate.now().minusYears(30);
            var passenger = new Passenger(1L, "John", "Doe", "john@example.com", "A123456", birthDate);

            assertThat(passenger.getAge()).isEqualTo(30);
        }

        @Test
        @DisplayName("should return 18 for someone born 18 years ago")
        void shouldReturn18ForSomeoneBorn18YearsAgo() {
            var birthDate = LocalDate.now().minusYears(18);
            var passenger = new Passenger(1L, "John", "Doe", "john@example.com", "A123456", birthDate);

            assertThat(passenger.getAge()).isGreaterThanOrEqualTo(18);
        }
    }

    @Nested
    @DisplayName("Setter Tests")
    class SetterTests {

        @Test
        @DisplayName("should update first name with valid value")
        void shouldUpdateFirstNameWithValidValue() {
            var passenger = new Passenger(1L, "John", "Doe", "john@example.com", "A123456", validBirthDate);
            passenger.setFirstName("Jane");

            assertThat(passenger.getFirstName()).isEqualTo("Jane");
        }

        @Test
        @DisplayName("should throw exception when setting null first name")
        void shouldThrowExceptionWhenSettingNullFirstName() {
            var passenger = new Passenger(1L, "John", "Doe", "john@example.com", "A123456", validBirthDate);

            assertThatThrownBy(() -> passenger.setFirstName(null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("First name cannot be null");
        }

        @Test
        @DisplayName("should update email with valid value")
        void shouldUpdateEmailWithValidValue() {
            var passenger = new Passenger(1L, "John", "Doe", "john@example.com", "A123456", validBirthDate);
            passenger.setEmail("newemail@example.com");

            assertThat(passenger.getEmail()).isEqualTo("newemail@example.com");
        }

        @Test
        @DisplayName("should throw exception when setting invalid email")
        void shouldThrowExceptionWhenSettingInvalidEmail() {
            var passenger = new Passenger(1L, "John", "Doe", "john@example.com", "A123456", validBirthDate);

            assertThatThrownBy(() -> passenger.setEmail("invalid-email"))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("should update birth date with valid value")
        void shouldUpdateBirthDateWithValidValue() {
            var passenger = new Passenger(1L, "John", "Doe", "john@example.com", "A123456", validBirthDate);
            var newBirthDate = LocalDate.now().minusYears(25);
            passenger.setBirthDate(newBirthDate);

            assertThat(passenger.getBirthDate()).isEqualTo(newBirthDate);
        }

        @Test
        @DisplayName("should throw exception when setting invalid birth date")
        void shouldThrowExceptionWhenSettingInvalidBirthDate() {
            var passenger = new Passenger(1L, "John", "Doe", "john@example.com", "A123456", validBirthDate);

            assertThatThrownBy(() -> passenger.setBirthDate(LocalDate.now().minusYears(10)))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("Equality Tests")
    class EqualityTests {

        @Test
        @DisplayName("should be equal if id and passport number are the same")
        void shouldBeEqualIfIdAndPassportNumberAreSame() {
            var passenger1 = new Passenger(1L, "John", "Doe", "john@example.com", "A123456", validBirthDate);
            var passenger2 = new Passenger(1L, "Jane", "Smith", "jane@example.com", "A123456", validBirthDate);

            assertThat(passenger1).isEqualTo(passenger2);
        }

        @Test
        @DisplayName("should not be equal if passport number differs")
        void shouldNotBeEqualIfPassportNumberDiffers() {
            var passenger1 = new Passenger(1L, "John", "Doe", "john@example.com", "A123456", validBirthDate);
            var passenger2 = new Passenger(1L, "John", "Doe", "john@example.com", "B789012", validBirthDate);

            assertThat(passenger1).isNotEqualTo(passenger2);
        }
    }
}
