FROM openjdk:8-jdk-alpine
MAINTAINER dalibor.jacko@gmail.com
COPY ./target target
ENTRYPOINT ["java","-jar","tinyurl-1.0-SNAPSHOT.jar"]