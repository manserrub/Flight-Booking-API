CREATE TABLE IF NOT EXISTS passengers (
  id BIGSERIAL PRIMARY KEY,
  first_name VARCHAR(100) NOT NULL,
  last_name VARCHAR(100) NOT NULL,
  email VARCHAR(255) UNIQUE NOT NULL,
  phone_number VARCHAR(20) NOT NULL,
  passport_number VARCHAR(30) UNIQUE NOT NULL,
  date_of_birth DATE NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE UNIQUE INDEX idx_passenger_email ON passengers(LOWER(email));
CREATE UNIQUE INDEX idx_passenger_passport ON passengers(LOWER(passport_number));
CREATE INDEX idx_passenger_name ON passengers(first_name, last_name);
