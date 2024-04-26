CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(12) UNIQUE NOT NULL,
    rut VARCHAR(12) UNIQUE NOT NULL,
    password VARCHAR(2000) NOT NULL,
    role VARCHAR(100),

    CONSTRAINT valid_name CHECK (name ~ '^[A-Za-záéíóúüñÁÉÍÓÚÜÑ]+$'),
    CONSTRAINT valid_last_name CHECK (last_name ~ '^[A-Za-záéíóúüñÁÉÍÓÚÜÑ]+$'),
    CONSTRAINT valid_email CHECK (email ~ '^[a-zA-Z0-9._%+-]+@gmail\.com$'),
    CONSTRAINT valid_phone CHECK (phone ~ '^\+569\d{8}$'),
    CONSTRAINT valid_rut CHECK (rut ~ '^\d{2}\.\d{3}\.\d{3}-[\dk]$'),
    CONSTRAINT valid_password CHECK (password ~ '^(?=.*[A-Z])(?=.*[0-9]).+$')
     --'^\+569\d{9}$'),
    --CONSTRAINT valid_name CHECK (name ~ '^[A-Za-záéíóúüñÁÉÍÓÚÜÑ]+$'),
    --CONSTRAINT valid_last_name CHECK (last_name ~ '^[A-Za-záéíóúüñÁÉÍÓÚÜÑ]+$'),
    --CONSTRAINT valid_email CHECK (email ~ '^[a-zA-Z0-9._%+-]+@gmail\.com$'),
    --CONSTRAINT valid_phone CHECK (phone ~ '^[0-9]{9}$'),
    --CONSTRAINT valid_rut CHECK (rut ~ '^\\d{2}\\.\\d{3}\\.\\d{3}-[\\dk]$'),
    --CONSTRAINT valid_password CHECK (password ~ '^(?=.*[A-Z])(?=.*[0-9]).+$')
);
