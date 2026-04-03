# Step 1: Build stage using Maven and JDK 21
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
# Copy only the pom first to cache dependencies (faster builds)
COPY pom.xml .
RUN mvn dependency:go-offline
# Copy the source code and build the jar
COPY src ./src
RUN mvn clean package -DskipTests

# Step 2: Runtime stage
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
# Copy the jar from the build stage
COPY --from=build /app/target/*.jar app.jar
# Expose the standard Spring Boot port
EXPOSE 8080
# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]