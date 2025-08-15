FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/appointment-0.0.1-SNAPSHOT.jar appointment.jar
ENTRYPOINT ["java", "-jar", "appointment.jar"]
EXPOSE 8081