# open jdk 17
FROM eclipse-temurin:17
WORKDIR /app
COPY ./build/libs/server-0.0.1-SNAPSHOT.jar /app/morib.jar
CMD ["java", "-Xms512m", "-Xmx1024m", "-XX:+UseG1GC", "-XX:+HeapDumpOnOutOfMemoryError", "-XX:HeapDumpPath=/app/heapdump.hprof", "-Duser.timezone=Asia/Seoul", "-jar", "morib.jar"]