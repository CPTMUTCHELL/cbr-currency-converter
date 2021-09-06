FROM flyway/flyway
RUN addgroup -g 1001 -S appuser && adduser -u 1001 -S appuser -G appuser
USER appuser
COPY auth-service/src/main/resources/userdb/*.sql /flyway/sql
ENV FLYWAY_URL=jdbc:postgresql://cbr-converter:5432/user_db
ENV FLYWAY_PASSWORD=postgres
ENV FLYWAY_USER=postgres
