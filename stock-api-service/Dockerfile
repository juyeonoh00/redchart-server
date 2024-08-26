# Build stage
FROM gradle:7.6-jdk17 AS build
WORKDIR /app
COPY . .
RUN chmod +x ./gradlew
RUN ./gradlew :post-service:build -x test --no-daemon

# Run stage
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/post-service/build/libs/*.jar app.jar
COPY --from=build /app/post-service/src/main/resources/application.yml /app/

ENV MODE=${MODE:-prod}
ENV SPRING_PROFILES_ACTIVE=${SPRING_PROFILES_ACTIVE:-prod}
ENV DB_NAME=${DB_NAME:-post-service-mysql}
ENV DB_PASSWORD_VAR=${DB_PASSWORD_VAR:-1234}

CMD java -jar -Dspring.profiles.active=$SPRING_PROFILES_ACTIVE app.jar