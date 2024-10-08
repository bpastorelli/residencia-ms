FROM ubuntu:latest
ENV DEBIAN_FRONTEND=noninteractive
RUN apt-get update && apt-get install -y tzdata

FROM adoptopenjdk/openjdk:17-alpine
RUN addgroup -S spring && adduser -S spring -G spring  
MAINTAINER Bruno Pastorelli
COPY target/residencia-ms.jar residencia-ms.jar
ENTRYPOINT ["java","-jar","/residencia-ms.jar"]
EXPOSE 9091