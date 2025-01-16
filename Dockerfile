#FROM gradle:7-jdk11 AS build
#COPY --chown=gradle:gradle . /home/gradle/src
#WORKDIR /home/gradle/src
#RUN gradle

FROM openjdk:11
EXPOSE 8080
WORKDIR /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/noteServer-all.jar
ENTRYPOINT ["java", "-jar", "/app/noteServer-all.jar"]