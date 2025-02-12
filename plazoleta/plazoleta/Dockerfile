FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/*.jar plazoleta.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "plazoleta.jar"]