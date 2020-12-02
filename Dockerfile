FROM maven:3.5.2-jdk-8 AS build
COPY src /usr/src/app/src
COPY pom.xml /usr/src/app
RUN mvn -f /usr/src/app/pom.xml clean install package

FROM openjdk:8
COPY --from=build /usr/src/app/target/vendas.jar /usr/app/vendas.jar
EXPOSE 8080
ADD /target/vendas.jar vendas.jar
ENTRYPOINT ["java","-jar","/usr/app/vendas.jar"]
