FROM openjdk:17
COPY 305.jar /app.jar
CMD ["java", "-jar", "/app.jar"]
