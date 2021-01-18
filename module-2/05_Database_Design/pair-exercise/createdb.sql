SELECT pg_terminate_backend(pid) FROM pg_stat_activity WHERE datname = 'animal_hospital_health';

DROP DATABASE animal_hospital_health;

CREATE DATABASE animal_hospital_health;