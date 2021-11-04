FROM flyway/flyway
COPY history-service/src/main/resources/historydb/*.sql /flyway/sql/
ENV FLYWAY_URL=jdbc:postgresql://cbr-converter:5432/history_db
ENV FLYWAY_PASSWORD=postgres
ENV FLYWAY_USER=postgres
