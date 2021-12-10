#!/bin/bash



psql -U postgres -h cbr-converter -tc "SELECT 1 FROM pg_database WHERE datname = 'oo'" \
| grep -q 1 | psql -U postgres -h cbr-converter -c "CREATE DATABASE oo"
