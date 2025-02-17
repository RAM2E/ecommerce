# Use an OpenJDK base image
FROM openjdk:17-jdk-slim

# Add a volume for logs
VOLUME /tmp

# Copy the application's JAR file into the container
ARG JAR_FILE=target/apigateway-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

# Expose the port used by the service
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "/app.jar"]
