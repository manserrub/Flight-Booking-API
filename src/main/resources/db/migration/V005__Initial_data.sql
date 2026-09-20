-- Insert sample airports
INSERT INTO airport (iata_code, name, city, country) VALUES
  ('MAD', 'Adolfo Suárez Madrid-Barajas', 'Madrid', 'Spain'),
  ('BCN', 'Josep Tarradellas Barcelona-El Prat', 'Barcelona', 'Spain'),
  ('LHR', 'London Heathrow', 'London', 'United Kingdom'),
  ('CDG', 'Charles de Gaulle', 'Paris', 'France'),
  ('JFK', 'John F. Kennedy', 'New York', 'USA')
ON CONFLICT (iata_code) DO NOTHING;

-- Insert sample flights
INSERT INTO flights (flight_number, departure_airport_id, arrival_airport_id, departure_time, arrival_time, price, available_seats, status)
SELECT 'IB6001', (SELECT id FROM airport WHERE iata_code = 'MAD'), (SELECT id FROM airport WHERE iata_code = 'BCN'), 
  CURRENT_TIMESTAMP + INTERVAL '2 days' + INTERVAL '08:00', CURRENT_TIMESTAMP + INTERVAL '2 days' + INTERVAL '09:30', 150.00, 120, 'SCHEDULED'
WHERE NOT EXISTS (SELECT 1 FROM flights WHERE flight_number = 'IB6001')
UNION ALL
SELECT 'IB6002', (SELECT id FROM airport WHERE iata_code = 'BCN'), (SELECT id FROM airport WHERE iata_code = 'MAD'), 
  CURRENT_TIMESTAMP + INTERVAL '2 days' + INTERVAL '10:00', CURRENT_TIMESTAMP + INTERVAL '2 days' + INTERVAL '11:30', 150.00, 120, 'SCHEDULED'
WHERE NOT EXISTS (SELECT 1 FROM flights WHERE flight_number = 'IB6002')
UNION ALL
SELECT 'BA2001', (SELECT id FROM airport WHERE iata_code = 'LHR'), (SELECT id FROM airport WHERE iata_code = 'CDG'), 
  CURRENT_TIMESTAMP + INTERVAL '1 day' + INTERVAL '09:00', CURRENT_TIMESTAMP + INTERVAL '1 day' + INTERVAL '11:00', 200.00, 150, 'SCHEDULED'
WHERE NOT EXISTS (SELECT 1 FROM flights WHERE flight_number = 'BA2001')
UNION ALL
SELECT 'AF3001', (SELECT id FROM airport WHERE iata_code = 'CDG'), (SELECT id FROM airport WHERE iata_code = 'JFK'), 
  CURRENT_TIMESTAMP + INTERVAL '3 days' + INTERVAL '11:00', CURRENT_TIMESTAMP + INTERVAL '3 days' + INTERVAL '19:30', 800.00, 200, 'SCHEDULED'
WHERE NOT EXISTS (SELECT 1 FROM flights WHERE flight_number = 'AF3001');

-- Insert sample passengers
INSERT INTO passengers (first_name, last_name, email, phone_number, passport_number, date_of_birth)
VALUES
  ('John', 'Doe', 'john.doe@example.com', '+34612345678', 'AB123456', '1990-05-15'),
  ('Jane', 'Smith', 'jane.smith@example.com', '+34687654321', 'CD789012', '1985-08-22'),
  ('Carlos', 'García', 'carlos.garcia@example.com', '+34645123789', 'EF345678', '1992-03-10'),
  ('María', 'López', 'maria.lopez@example.com', '+34678912345', 'GH901234', '1988-11-25')
ON CONFLICT (email) DO NOTHING;
