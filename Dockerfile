FROM maven:3.9.9-amazoncorretto-21 AS build
COPY src /app/src
COPY pom.xml /app

WORKDIR /app
RUN mvn clean package -f /app/pom.xml -Dmaven.test.skip=true


FROM amazoncorretto:21-alpine AS jre-build
COPY --from=build /app/target/*.jar app.jar
RUN jar xf app.jar
RUN jdeps --ignore-missing-deps -q  \
    --recursive  \
    --multi-release 21  \
    --print-module-deps  \
    --class-path 'BOOT-INF/lib/*'  \
    app.jar > deps.info
RUN jlink \
    --verbose \
    --add-modules $(cat deps.info),jdk.crypto.ec \
    --no-header-files \
    --no-man-pages \
    --output /customjre

FROM alpine:3.18
COPY --from=jre-build /customjre /opt/jre
ENV JAVA_HOME=/opt/jre
ENV PATH="$PATH:$JAVA_HOME/bin"
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
