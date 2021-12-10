FROM maven:3.8.1-openjdk-15 AS build
COPY pom.xml /usr
COPY auth-service/src/ /usr/auth-service/src
COPY auth-service/pom.xml /usr/auth-service
COPY entity/src/ /usr/entity/src
COPY entity/pom.xml /usr/entity
COPY convert-service/src/ /usr/convert-service/src
COPY convert-service/pom.xml /usr/convert-service
COPY history-service/src/ /usr/history-service/src
COPY history-service/pom.xml /usr/history-service

RUN --mount=type=cache,target=/root/.m2 mvn -f /usr/pom.xml clean package
FROM openjdk:15-alpine
RUN apk add --no-cache bash
COPY --from=build /usr/auth-service/target/auth-service-0.0.1-SNAPSHOT.jar /usr/auth/auth-service-0.0.1-SNAPSHOT.jar
CMD ["java","-jar","/usr/auth/auth-service-0.0.1-SNAPSHOT.jar","--spring.profiles.active=prod"]