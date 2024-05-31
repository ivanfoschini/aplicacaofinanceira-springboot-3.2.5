#
# Build stage
#
FROM maven:3.8.2-jdk-21 AS build
COPY . .
RUN mvn clean package -DskipTests

#
# Package stage
#
FROM openjdk:21-jdk-slim
COPY --from=build /target/aplicacaofinanceira-0.0.1-SNAPSHOT.jar aplicacaofinanceira.jar
# ENV PORT=8080
EXPOSE 8080
ENTRYPOINT ["java","-jar","aplicacaofinanceira.jar"]