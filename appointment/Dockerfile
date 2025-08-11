FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/appointment-0.0.1-SNAPSHOT.jar appointment.jar
ENTRYPOINT ["java", "-jar", "appointment.jar"]
EXPOSE 8081


#FROM openjdk:17-jdk-slim
#WORKDIR /app
#COPY pom.xml .
#COPY src ./src
#RUN apt-get update && apt-get install -y maven netcat
#RUN mvn package -DskipTests
#COPY entrypoint.sh .
#RUN chmod +x entrypoint.sh
#EXPOSE 8080
##ENTRYPOINT ["./entrypoint.sh"]
#CMD ["java", "-jar", "target/clinicKafka-0.0.1-SNAPSHOT.jar"]