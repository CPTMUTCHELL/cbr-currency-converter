FROM postgres
USER postgres
COPY postgres/create-multiple-postgresql-databases.sh .
RUN chmod 505 create-multiple-postgresql-databases.sh
ENTRYPOINT ["./create-multiple-postgresql-databases.sh"]
