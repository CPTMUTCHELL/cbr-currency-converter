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

#RUN mvn -f /usr/pom.xml clean package
FROM bellsoft/liberica-openjre-alpine:15.0.1-9
RUN apk add --no-cache bash
RUN addgroup -g 1001 -S appuser && adduser -u 1001 -S appuser -G appuser
USER appuser
COPY --from=build /usr/convert-service/target/convert-service.jar /usr/convert/convert-service.jar
CMD ["java","-jar","/usr/convert/convert-service.jar","--spring.profiles.active=prod"]