# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Define the path for the JAR file
ARG JAR_FILE=target/eurekaserver-0.0.1-SNAPSHOT.jar

# Copy the JAR file into the container at /app directory
COPY ${JAR_FILE} eureka-server.jar

# Expose port 8761 (Eureka Server default port)
EXPOSE 8761

# Run the JAR file when the container starts
ENTRYPOINT ["java", "-jar", "eureka-server.jar"]
