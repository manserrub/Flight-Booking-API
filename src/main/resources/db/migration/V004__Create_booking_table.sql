CREATE TABLE IF NOT EXISTS bookings (
  id BIGSERIAL PRIMARY KEY,
  booking_reference VARCHAR(20) UNIQUE NOT NULL,
  flight_id BIGINT NOT NULL,
  passenger_id BIGINT NOT NULL,
  seat_number VARCHAR(10) NOT NULL,
  booking_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (flight_id) REFERENCES flights(id),
  FOREIGN KEY (passenger_id) REFERENCES passengers(id),
  UNIQUE(flight_id, seat_number)
);

CREATE INDEX idx_booking_reference ON bookings(booking_reference);
CREATE INDEX idx_booking_flight ON bookings(flight_id);
CREATE INDEX idx_booking_passenger ON bookings(passenger_id);
CREATE INDEX idx_booking_status ON bookings(status);
