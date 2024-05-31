FROM maven:3.8.2 AS build
COPY . .
RUN mvn clean install -X -DskipTests

FROM openjdk:21
ADD target/aplicacaofinanceira-0.0.1-SNAPSHOT.jar aplicacaofinanceira.jar
ENTRYPOINT [ "java", "-jar","aplicacaofinanceira.jar" ]