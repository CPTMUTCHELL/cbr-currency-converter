FROM flyway/flyway
COPY convert-service/src/main/resources/converterdb/*.sql /flyway/sql
ENV FLYWAY_URL=jdbc:postgresql://cbr-converter:5432/converter_db
ENV FLYWAY_PASSWORD=postgres
ENV FLYWAY_USER=postgres
