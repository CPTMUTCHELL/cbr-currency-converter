#!/bin/bash

#for db in "$@";
#do
psql -U postgres -h cbr-converter -tc "SELECT 1 FROM pg_database WHERE datname = 'ololosh'" | grep -q 1 | psql -U postgres -c "CREATE DATABASE ololosh"
#done