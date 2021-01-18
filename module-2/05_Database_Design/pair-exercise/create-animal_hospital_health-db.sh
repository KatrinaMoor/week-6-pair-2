#!/bin/bash
export PGPASSWORD='postgres1'

psql -U postgres -f "./createdb.sql" &&
psql -U postgres -d animal_hospital_health -f "./animalhospitalhealth.sql"