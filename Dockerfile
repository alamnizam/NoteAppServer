# Use an official OpenJDK image as a base
FROM openjdk:11-jre-slim

# Set working directory
WORKDIR /app

# Copy the fat JAR file into the Docker image
COPY build/libs/noteServer-1.0.jar /app/noteServer.jar

# Expose the port your app will run on
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "/app/noteServer.jar"]
