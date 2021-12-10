#!/bin/bash

for db in "$@";
do

psql -U postgres -h cbr-converter -c "SELECT 'CREATE DATABASE $db' WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = '$db')\gexec"
done