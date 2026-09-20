CREATE TABLE IF NOT EXISTS flights (
  id BIGSERIAL PRIMARY KEY,
  flight_number VARCHAR(10) UNIQUE NOT NULL,
  departure_airport_id BIGINT NOT NULL,
  arrival_airport_id BIGINT NOT NULL,
  departure_time TIMESTAMP NOT NULL,
  arrival_time TIMESTAMP NOT NULL,
  price NUMERIC(10, 2) NOT NULL CHECK (price > 0),
  available_seats INTEGER NOT NULL CHECK (available_seats >= 0),
  status VARCHAR(20) NOT NULL DEFAULT 'SCHEDULED',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (departure_airport_id) REFERENCES airport(id),
  FOREIGN KEY (arrival_airport_id) REFERENCES airport(id)
);

CREATE INDEX idx_flight_departure_airport ON flights(departure_airport_id);
CREATE INDEX idx_flight_arrival_airport ON flights(arrival_airport_id);
CREATE INDEX idx_flight_status ON flights(status);
