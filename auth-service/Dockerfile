FROM maven:3.8.1-openjdk-15 AS build

COPY src /usr/auth/app
COPY pom.xml /usr/auth
COPY ../entity/src /usr/entity/app
COPY ../entity/pom.xml /usr/entity

RUN --mount=type=cache,target=/root/.m2 mvn -f /usr/src/app/pom.xml clean package
FROM openjdk:15-alpine
RUN apk add --no-cache bash
COPY --from=build /usr/src/app/target/converter-0.0.1-SNAPSHOT.jar /usr/app/converter-0.0.1-SNAPSHOT.jar
COPY ./wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh