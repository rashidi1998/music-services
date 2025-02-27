# Start with a base image that includes Java 17 for building the application
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app

# Copy Gradle files for wrapper and settings from the expected locations
COPY gradlew settings.gradle ./
COPY gradle ./gradle

# Prepare the application-specific directory
WORKDIR /app/resource-service

# Copy service-specific Gradle build file and source code
COPY resource-service/build.gradle ./
COPY resource-service/src ./src

# Ensure gradlew is executable and run Gradle tasks
RUN chmod +x ../gradlew && \
    ../gradlew dependencies --no-daemon && \
    ../gradlew assemble --no-daemon -x test

# Runtime stage uses a smaller JRE base image for efficiency
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copy the built application jar from the build stage to the runtime container
COPY --from=build /app/resource-service/build/libs/*.jar app.jar

# Expose port 8080 for the application
EXPOSE 8080

# Command to run the application
CMD ["java", "-jar", "app.jar"]
