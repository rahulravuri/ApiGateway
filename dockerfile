# Use the official Maven image to build the app
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY ./ApiGateway/pom.xml .
COPY ./ApiGateway/src ./src
RUN mvn clean package -DskipTests
FROM openjdk:17-jdk-alpine
WORKDIR /app
ARG CACHEBUST=134
COPY ./target/ApiGateway-0.0.1-SNAPSHOT.jar app.jar
ENV SPRING_DATASOURCE_URL=r2dbc:mysql://host.docker.internal:3306/users?serverTimezone=Asia/Kolkata
ENV SPRING_DATASOURCE_USERNAME=root
ENV SPRING_DATASOURCE_PASSWORD=Himalayan@2024R
ENV SPRING_PROFILES_ACTIVE=docker
ENV SPRING_AUTH_URL=http://auth:8080
ENV server_port=8080
ENV SPRING_BOOKMYSHOW_URL=http://bookmyshow:8080
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]