DROP TABLE IF EXISTS users_roles;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS roles;
DROP TABLE IF EXISTS suburbs_postcodes;
DROP TABLE IF EXISTS suburbs;
DROP TABLE IF EXISTS postcodes;

CREATE TABLE roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE users_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (role_id) REFERENCES roles(id)
);

CREATE TABLE suburbs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE postcodes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(10) NOT NULL
);

CREATE TABLE suburbs_postcodes (
    suburb_id BIGINT NOT NULL,
    postcode_id BIGINT NOT NULL,
    FOREIGN KEY (suburb_id) REFERENCES suburbs(id),
    FOREIGN KEY (postcode_id) REFERENCES postcodes(id),
    PRIMARY KEY (suburb_id, postcode_id)
);
