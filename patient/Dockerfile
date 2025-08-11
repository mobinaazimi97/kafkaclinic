#FROM openjdk:17-jdk-slim
#WORKDIR /app
#COPY pom.xml .
#COPY src ./src
#RUN apt-get update && apt-get install -y maven
#RUN mvn package -DskipTests
#EXPOSE 8080
#CMD ["java", "-jar", "target/patient-0.0.1-SNAPSHOT.jar"]
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/patient-0.0.1-SNAPSHOT.jar patient.jar
ENTRYPOINT ["java", "-jar", "patient.jar"]
#EXPOSE 8080



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