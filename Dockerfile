FROM adoptopenjdk/openjdk11
EXPOSE 8080
COPY target/CloudService-0.0.1-SNAPSHOT.jar CloudService.jar
COPY src/main/resources/cloud src/main/resources/cloud
CMD ["java", "-jar", "/CloudService.jar"]