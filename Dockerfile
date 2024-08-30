FROM openjdk:17-jdk-alpine3.13
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
