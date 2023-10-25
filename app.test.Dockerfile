FROM openjdk:11-jre-slim
VOLUME /tmp
COPY build/libs/*.jar app.jar
ENTRYPOINT ["java","-Dspring.profiles.active=test","-jar","app.jar"]
