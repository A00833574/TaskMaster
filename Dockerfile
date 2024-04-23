# Fase de construcción
FROM openjdk:21-rc-jdk-oraclelinux8 as build
WORKDIR /app
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src
RUN ./mvnw package -DskipTests

# Fase de ejecución
FROM openjdk:21-rc-jdk-oraclelinux8
WORKDIR /app
COPY --from=build /app/target/todochat-0.0.1-SNAPSHOT.jar /app/todochat.jar
ENTRYPOINT ["java", "-jar", "todochat.jar"]
