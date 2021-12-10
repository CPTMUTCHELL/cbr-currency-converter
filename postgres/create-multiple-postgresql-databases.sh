#!/bin/bash

#for db in "$@";
#do
echo "hf"
psql -U postgres -h cbr-converter -tc "SELECT 1 FROM pg_database WHERE datname = 'ololosh'"
#done