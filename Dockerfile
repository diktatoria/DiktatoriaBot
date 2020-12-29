FROM openjdk:latest

COPY target/Bot.jar /usr/src/Bot.jar

CMD java -jar /usr/src/Bot.jar