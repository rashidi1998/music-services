# ---- Stage 1: Build ----
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app
COPY gradlew settings.gradle ./
COPY gradle ./gradle

WORKDIR /app/eureka-service

COPY eureka-service/build.gradle ./
COPY eureka-service/src ./src

RUN chmod +x ../gradlew && \
    ../gradlew dependencies --no-daemon && \
    ../gradlew assemble --no-daemon -x test

# ---- Stage 2: Run ----
FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=build /app/eureka-service/build/libs/*.jar eureka-server.jar

EXPOSE 8761

CMD ["java", "-jar", "eureka-server.jar"]
