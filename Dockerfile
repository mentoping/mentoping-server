FROM alpine/java:21.0.4

WORKDIR /app
COPY build/libs/mentain.jar /app/mentain.jar

ENTRYPOINT ["java", "-jar", "mentain.jar"]
EXPOSE 8089