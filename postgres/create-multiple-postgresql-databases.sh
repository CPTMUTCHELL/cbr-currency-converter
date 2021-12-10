#!/bin/bash

#for db in "$@";
#do
echo "hf"
psql -U postgres -h cbr-converter -tc "CREATE DATABASE ololosh"
#done