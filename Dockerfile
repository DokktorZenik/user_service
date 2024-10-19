FROM openjdk:17-alpine
WORKDIR /app
EXPOSE 8083
COPY ./target/user-service-0.0.1-SNAPSHOT.jar /app/user-service.jar
CMD ["java", "-jar", "user-service.jar"]