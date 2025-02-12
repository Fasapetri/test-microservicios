FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/*.jar pedidos.jar

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "pedidos.jar"]