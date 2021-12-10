#!/bin/bash

#for db in "$@";
#do
echo "hf"
psql -U postgres -h cbr-converter -tc "SELECT 1 FROM pg_database WHERE datname = 'olol'" | grep -q 1 | psql -U postgres -h cbr-converter -c "CREATE DATABASE olol"
#done