FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/patient-0.0.1-SNAPSHOT.jar patient.jar
ENTRYPOINT ["java", "-jar", "patient.jar"]
