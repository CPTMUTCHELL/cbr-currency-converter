FROM maven:3.8.1-openjdk-15 AS build
COPY src /usr/src/app/src
COPY pom.xml /usr/src/app
RUN mvn -f /usr/src/app/pom.xml clean package

FROM openjdk:15-alpine
#ADD target/demo-0.0.1-SNAPSHOT.jar demo-0.0.1-SNAPSHOT.jar
COPY --from=build /usr/src/app/target/converter-0.0.1-SNAPSHOT.jar /usr/app/converter-0.0.1-SNAPSHOT.jar
ENTRYPOINT  ["java","-jar","/usr/app/converter-0.0.1-SNAPSHOT.jar"]