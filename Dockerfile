#FROM openjdk:21-slim
#WORKDIR /app
#COPY target/weather-viewer.jar app.jar
#EXPOSE 8080
#ENTRYPOINT ["java", "-jar", "app.jar"]