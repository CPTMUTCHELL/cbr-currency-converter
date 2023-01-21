#!/bin/sh

# wait for postgres to fully start (or initContainer)
sleep 20
for db in "$@";
do

psql -tc "SELECT 1 FROM pg_database WHERE datname = '$db'" | grep -q 1 || psql -c "CREATE DATABASE $db"
done