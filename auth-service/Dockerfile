
FROM maven:3.8.1-openjdk-15 AS build

COPY src /usr/src/app/src
COPY pom.xml /usr/src/app

RUN --mount=type=cache,target=/root/.m2 mvn -f /usr/src/app/pom.xml clean package
FROM openjdk:15-alpine
RUN apk add --no-cache bash
COPY --from=build /usr/src/app/target/converter-0.0.1-SNAPSHOT.jar /usr/app/converter-0.0.1-SNAPSHOT.jar
COPY ./wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh