FROM postgres
COPY postgres/create-multiple-postgresql-databases.sh .
USER postgres

ENTRYPOINT ["./create-multiple-postgresql-databases.sh"]
