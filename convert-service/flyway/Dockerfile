FROM flyway/flyway
COPY convert-service/src/main/resources/convertdb/*.sql /flyway/sql/
ENV FLYWAY_URL=jdbc:postgresql://cbr-converter:5432/convert_db
