CREATE TABLE appointments(
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(50) NOT NULL,
  last_name VARCHAR(50) NOT NULL,
  rut VARCHAR(12) NOT NULL,
  phone VARCHAR(12) NOT NULL,
  email VARCHAR(100) NOT NULL,
  is_available VARCHAR(30) NOT NULL,
  date DATE NOT NULL CHECK (date >= CURRENT_DATE), -- Fecha debe ser mayor o igual a la fecha actual
  time TIME NOT NULL CHECK (time >= '00:00:00' AND time <= '23:59:59'), -- Hora debe estar entre 00:00:00 y 23:59:59
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  service_types_id BIGINT,
  user_id BIGINT,
  pet_id BIGINT,
  FOREIGN KEY (service_types_id) REFERENCES service_types(id),
  FOREIGN KEY (user_id) REFERENCES users(id),
  FOREIGN KEY (pet_id) REFERENCES pets(id),

  CONSTRAINT valid_name CHECK (name ~ '^[A-Za-záéíóúüñÁÉÍÓÚÜÑ]+$'),
  CONSTRAINT valid_last_name CHECK (last_name ~ '^[A-Za-záéíóúüñÁÉÍÓÚÜÑ]+$'),
  CONSTRAINT valid_email CHECK (email ~ '^[a-zA-Z0-9._%+-]+@gmail\.com$'),
  CONSTRAINT valid_phone CHECK (phone ~ '^\+569\d{8}$'),
  CONSTRAINT valid_rut CHECK (rut ~ '^\d{2}\.\d{3}\.\d{3}-[\dk]$')
);