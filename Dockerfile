# Use a base image with OpenJDK 21 installed
FROM openjdk:21-jdk-slim

ARG JAR_FILE=build/libs/productDetailsService*SNAPSHOT.jar

# Copy the Spring Boot JAR file into the container
COPY build/libs/productDetailsService*SNAPSHOT.jar app.jar

# Expose the port that the application will run on
EXPOSE 8010

# Define the command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]