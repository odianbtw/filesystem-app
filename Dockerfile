FROM gradle:8.8-jdk21 AS build

WORKDIR /home/gradle/project

COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./

RUN ./gradlew dependencies --no-daemon

COPY src src
RUN ./gradlew bootJar --no-daemon

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /home/gradle/project/build/libs/*.jar app.jar

EXPOSE 8080

ENV JAVA_OPTS="\
 -XX:+UseG1GC \
 -XX:MaxRAMPercentage=75.0 \
 -XX:+ExitOnOutOfMemoryError \
 -Djava.security.egd=file:/dev/urandom"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]