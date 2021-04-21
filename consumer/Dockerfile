#
# Build Docker
#
#FROM maven:3.6.0-jdk-11-slim AS maven_build
#WORKDIR /app
#COPY ./pom.xml ./pom.xml
#RUN mvn -B dependency:go-offline
#COPY ./src ./src
#RUN mvn package

#
# Run
#
FROM openjdk:11-jre-slim
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]