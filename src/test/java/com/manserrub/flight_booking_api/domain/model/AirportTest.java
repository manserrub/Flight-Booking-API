package com.manserrub.flight_booking_api.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Airport Domain Model Tests")
class AirportTest {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("should create airport with valid data")
        void shouldCreateAirportWithValidData() {
            var airport = new Airport(1L, "MAD", "Adolfo Suárez Madrid-Barajas", "Madrid", "Spain");

            assertThat(airport).isNotNull();
            assertThat(airport.getId()).isEqualTo(1L);
            assertThat(airport.getIataCode()).isEqualTo("MAD");
            assertThat(airport.getName()).isEqualTo("Adolfo Suárez Madrid-Barajas");
            assertThat(airport.getCity()).isEqualTo("Madrid");
            assertThat(airport.getCountry()).isEqualTo("Spain");
        }

        @Test
        @DisplayName("should create airport without ID")
        void shouldCreateAirportWithoutId() {
            var airport = new Airport("BCN", "Barcelona-El Prat", "Barcelona", "Spain");

            assertThat(airport).isNotNull();
            assertThat(airport.getId()).isNull();
            assertThat(airport.getIataCode()).isEqualTo("BCN");
        }

        @Test
        @DisplayName("should throw exception when IATA code is null")
        void shouldThrowExceptionWhenIataCodeIsNull() {
            assertThatThrownBy(() -> new Airport(1L, null, "Airport", "City", "Country"))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("IATA code cannot be null");
        }

        @Test
        @DisplayName("should throw exception when IATA code length is invalid")
        void shouldThrowExceptionWhenIataCodeLengthIsInvalid() {
            assertThatThrownBy(() -> new Airport(1L, "MA", "Airport", "City", "Country"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("IATA code must be 3-4 characters");

            assertThatThrownBy(() -> new Airport(1L, "MADRI", "Airport", "City", "Country"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("IATA code must be 3-4 characters");
        }

        @Test
        @DisplayName("should throw exception when IATA code contains lowercase letters")
        void shouldThrowExceptionWhenIataCodeContainsLowercaseLetters() {
            assertThatThrownBy(() -> new Airport(1L, "Mad", "Airport", "City", "Country"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("IATA code must contain only uppercase letters");
        }

        @Test
        @DisplayName("should throw exception when name is null")
        void shouldThrowExceptionWhenNameIsNull() {
            assertThatThrownBy(() -> new Airport(1L, "MAD", null, "City", "Country"))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("Airport name cannot be null");
        }

        @Test
        @DisplayName("should throw exception when city is null")
        void shouldThrowExceptionWhenCityIsNull() {
            assertThatThrownBy(() -> new Airport(1L, "MAD", "Airport", null, "Country"))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("City cannot be null");
        }

        @Test
        @DisplayName("should throw exception when country is null")
        void shouldThrowExceptionWhenCountryIsNull() {
            assertThatThrownBy(() -> new Airport(1L, "MAD", "Airport", "City", null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("Country cannot be null");
        }
    }

    @Nested
    @DisplayName("Setter Tests")
    class SetterTests {

        @Test
        @DisplayName("should update IATA code with valid value")
        void shouldUpdateIataCodeWithValidValue() {
            var airport = new Airport(1L, "MAD", "Madrid", "Madrid", "Spain");
            airport.setIataCode("AGP");

            assertThat(airport.getIataCode()).isEqualTo("AGP");
        }

        @Test
        @DisplayName("should throw exception when setting invalid IATA code")
        void shouldThrowExceptionWhenSettingInvalidIataCode() {
            var airport = new Airport(1L, "MAD", "Madrid", "Madrid", "Spain");

            assertThatThrownBy(() -> airport.setIataCode("invalid"))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("should update name with valid value")
        void shouldUpdateNameWithValidValue() {
            var airport = new Airport(1L, "MAD", "Madrid", "Madrid", "Spain");
            airport.setName("New Name");

            assertThat(airport.getName()).isEqualTo("New Name");
        }

        @Test
        @DisplayName("should throw exception when setting null name")
        void shouldThrowExceptionWhenSettingNullName() {
            var airport = new Airport(1L, "MAD", "Madrid", "Madrid", "Spain");

            assertThatThrownBy(() -> airport.setName(null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("Airport name cannot be null");
        }

        @Test
        @DisplayName("should update city with valid value")
        void shouldUpdateCityWithValidValue() {
            var airport = new Airport(1L, "MAD", "Madrid", "Madrid", "Spain");
            airport.setCity("Barcelona");

            assertThat(airport.getCity()).isEqualTo("Barcelona");
        }

        @Test
        @DisplayName("should throw exception when setting null city")
        void shouldThrowExceptionWhenSettingNullCity() {
            var airport = new Airport(1L, "MAD", "Madrid", "Madrid", "Spain");

            assertThatThrownBy(() -> airport.setCity(null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("City cannot be null");
        }

        @Test
        @DisplayName("should update country with valid value")
        void shouldUpdateCountryWithValidValue() {
            var airport = new Airport(1L, "MAD", "Madrid", "Madrid", "Spain");
            airport.setCountry("Portugal");

            assertThat(airport.getCountry()).isEqualTo("Portugal");
        }

        @Test
        @DisplayName("should throw exception when setting null country")
        void shouldThrowExceptionWhenSettingNullCountry() {
            var airport = new Airport(1L, "MAD", "Madrid", "Madrid", "Spain");

            assertThatThrownBy(() -> airport.setCountry(null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("Country cannot be null");
        }
    }

    @Nested
    @DisplayName("Equality and Hash Tests")
    class EqualityAndHashTests {

        @Test
        @DisplayName("should be equal if id and IATA code are the same")
        void shouldBeEqualIfIdAndIataCodeAreSame() {
            var airport1 = new Airport(1L, "MAD", "Madrid", "Madrid", "Spain");
            var airport2 = new Airport(1L, "MAD", "Different Name", "Different City", "Different Country");

            assertThat(airport1).isEqualTo(airport2);
        }

        @Test
        @DisplayName("should not be equal if IATA code differs")
        void shouldNotBeEqualIfIataCodeDiffers() {
            var airport1 = new Airport(1L, "MAD", "Madrid", "Madrid", "Spain");
            var airport2 = new Airport(1L, "BCN", "Barcelona", "Barcelona", "Spain");

            assertThat(airport1).isNotEqualTo(airport2);
        }

        @Test
        @DisplayName("should have same hash code for equal airports")
        void shouldHaveSameHashCodeForEqualAirports() {
            var airport1 = new Airport(1L, "MAD", "Madrid", "Madrid", "Spain");
            var airport2 = new Airport(1L, "MAD", "Madrid", "Madrid", "Spain");

            assertThat(airport1).hasSameHashCodeAs(airport2);
        }
    }
}
