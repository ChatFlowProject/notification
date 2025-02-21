FROM openjdk:17-jdk-slim
EXPOSE 8088
ADD ./build/libs/noti.jar /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]


