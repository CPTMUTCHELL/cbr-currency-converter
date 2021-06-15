FROM flyway/flyway
COPY ../src/main/resources/db /flyway/sql
ENV FLYWAY_URL=jdbc:postgresql://db:5432/converter_db
ENV FLYWAY_PASSWORD=postgres
ENV FLYWAY_USER=postgres
CMD ["-connectRetries=60","migrate"]
