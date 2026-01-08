
FROM maven:3.8-amazoncorretto-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests


FROM amazoncorretto:17-al2-jdk

WORKDIR /apl/
COPY --from=build /app/target/*.jar micro.jar



EXPOSE 8081
ENTRYPOINT java $JAVA_OPTIONS -jar /apl/micro.jar --spring.servlet.multipart.location=/apl/tmp -Dlog4j2.formatMsgNoLookups=true $JAR_OPTIONS