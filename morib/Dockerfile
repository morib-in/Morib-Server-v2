# open jdk 17
FROM eclipse-temurin:17
WORKDIR /app
COPY ./build/libs/*.jar /app/morib.jar
CMD ["java", "-Duser.timezone=Asia/Seoul", "-jar", "morib.jar"]