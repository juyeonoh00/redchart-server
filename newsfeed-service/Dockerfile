FROM openjdk:17-jdk-slim

WORKDIR /app

COPY build/libs/*.jar /app/newsfeed-service.jar

ENTRYPOINT ["java", "-jar", "/app/newsfeed-service.jar"]