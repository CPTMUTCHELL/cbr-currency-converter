FROM liquibase/liquibase
RUN mkdir -p /liquibase/changelog
VOLUME /src/main/resources/db/changelog.xml:/liquibase/changelog

ENV LIQUIBASE_USERNAME: postgres
ENV LIQUIBASE_PASSWORD: postgres
ENV LIQUIBASE_DATABASE: postgres
ENV LIQUIBASE_DRIVER: org.postgresql.Driver
ENV LIQUIBASE_URL: "jdbc:postgresql://mypg:5432/postgres"
ENV LIQUIBASE_CHANGELOGFILE: /liquibase/changelog/changelog.xml
CMD ["update"]