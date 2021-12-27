CREATE TABLE IF NOT EXISTS staff (
    id SERIAL NOT NULL PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    role_id INTEGER NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);