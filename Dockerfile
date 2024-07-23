FROM --platform=linux/arm64 amazoncorretto:17

COPY backend/build/libs/chongdae-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]
