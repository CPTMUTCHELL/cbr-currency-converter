#!/bin/bash

for db in "$@";
do
psql -U postgres -tc "SELECT 1 FROM pg_database WHERE datname = $db" | grep -q 1 | psql -U postgres -c "CREATE DATABASE $db"
done