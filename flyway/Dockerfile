FROM flyway/flyway
ENV FLYWAY_URL=jdbc:postgresql://db:5432/test_db
ENV FLYWAY_PASSWORD=postgres
ENV FLYWAY_USER=postgres
CMD ["-connectRetries=60","migrate"]
