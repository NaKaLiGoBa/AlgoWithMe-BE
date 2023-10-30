FROM krmp-d2hub-idock.9rum.cc/goorm/gradle:7.3.1-jdk17 AS build

WORKDIR /usr/src/app

COPY . .

# gradle 빌드 시 proxy 설정을 gradle.properties에 추가
RUN echo "systemProp.http.proxyHost=krmp-proxy.9rum.cc\nsystemProp.http.proxyPort=3128\nsystemProp.https.proxyHost=krmp-proxy.9rum.cc\nsystemProp.https.proxyPort=3128" > /root/.gradle/gradle.properties && \
    ./gradlew clean bootJar

FROM openjdk:11-jre-slim

COPY --from=build /usr/src/app/build/libs/*.jar ./app.jar

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=test", "-Dhttp.proxyHost=http://krmp-proxy.9rum.cc", "-Dhttp.proxyPort=3128", "app.jar"]
