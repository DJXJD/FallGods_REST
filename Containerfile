FROM eclipse-temurin:17-jdk-alpine
ENV spring.profiles.active prod
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]