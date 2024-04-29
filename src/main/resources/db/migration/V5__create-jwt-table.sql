CREATE TABLE jwt_token (
    id SERIAL PRIMARY KEY,
    token VARCHAR(2048),
    expiration TIMESTAMP,
    is_valid BOOLEAN,
    user_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
