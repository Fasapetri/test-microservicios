FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/*.jar users.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "users.jar"]