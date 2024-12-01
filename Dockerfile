FROM --platform=linux/amd64 amazoncorretto:17

ENV TZ=Asia/Seoul

COPY backend/build/libs/chongdae-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]
