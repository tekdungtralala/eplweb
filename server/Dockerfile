FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY target/*.war app.war
COPY entrypoint.sh entrypoint.sh
ENTRYPOINT ["./entrypoint.sh"]