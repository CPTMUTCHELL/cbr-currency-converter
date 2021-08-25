FROM flyway/flyway
COPY *.sql /flyway/sql
ENV FLYWAY_URL=jdbc:postgresql://user-db:5432/user_db
ENV FLYWAY_PASSWORD=postgres
ENV FLYWAY_USER=postgres
ENTRYPOINT ["flyway"]