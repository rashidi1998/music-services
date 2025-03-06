FROM eclipse-temurin:17-jdk AS build
WORKDIR /app

COPY gradlew settings.gradle ./
COPY gradle ./gradle

WORKDIR /app/song-service

COPY song-service/build.gradle ./
COPY song-service/src ./src

RUN chmod +x ../gradlew && \
    ../gradlew dependencies --no-daemon && \
    ../gradlew assemble --no-daemon -x test

FROM eclipse-temurin:17-jre
WORKDIR /app
RUN mkdir -p /logs

COPY --from=build /app/song-service/build/libs/*.jar app.jar

EXPOSE 8081

CMD ["java", "-jar", "app.jar", "--logging.file=/logs/song-service.log"]
