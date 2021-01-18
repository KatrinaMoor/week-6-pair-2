--1.Drop all tables
DROP TABLE IF EXISTS owner;      
DROP TABLE IF EXISTS pet;
DROP TABLE IF EXISTS med_visit;


--2. Create tables

CREATE TABLE owner
(
        client_id SERIAL NOT NULL PRIMARY KEY
        , first_name VARCHAR(50) NOT NULL
        , last_name VARCHAR(50) NOT NULL
        , address VARCHAR(50) NOT NULL
        , city VARCHAR(20) NOT NULL
        , state CHAR(2) NULL
        , zip VARCHAR(10) NOT NULL
);

CREATE TABLE pet
(
        pet_id SERIAL NOT NULL PRIMARY KEY
        , pet_name VARCHAR(50) NOT NULL
        , pet_type VARCHAR(50) NOT NULL
        , pet_age SMALLINT NOT NULL
        , client_id SERIAL NOT NULL--FK
);

CREATE TABLE med_visit
(
        pet_id SERIAL NOT NULL --fk
        , visit_date DATE NOT NULL
        , procedure_id SERIAL NOT NULL
        , procedure_name VARCHAR(50) NOT NULL
        , procedure_amount DECIMAL(6,2) NOT NULL
);

--3. Add Relationships

ALTER TABLE pet --which table are we altering
ADD CONSTRAINT fk_owner_pet -- name of constraint
FOREIGN KEY (client_id) -- which column in the sale table
REFERENCES owner (client_id); -- point to other table and column


ALTER TABLE med_visit --which table are we altering
ADD CONSTRAINT fk_pet_med_visit -- name of constraint
FOREIGN KEY (pet_id) -- which column in the sale table
REFERENCES pet (pet_id); -- point to other table and column
