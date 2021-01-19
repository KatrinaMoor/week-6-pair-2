export PGPASSWORD='postgres1'

psql -U postgres -f "./create_projects_database.sql" &&
psql -U postgres -d projects -f "./projects_schema.sql" 