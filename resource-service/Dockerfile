FROM eclipse-temurin:17-jdk AS build
WORKDIR /app

COPY gradlew settings.gradle ./
COPY gradle ./gradle

WORKDIR /app/resource-service

COPY resource-service/build.gradle ./
COPY resource-service/src ./src

RUN chmod +x ../gradlew && \
    ../gradlew dependencies --no-daemon && \
    ../gradlew assemble --no-daemon -x test

FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=build /app/resource-service/build/libs/*.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
