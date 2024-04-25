# Fase de construcción
FROM openjdk:21-rc-jdk-oraclelinux8 as build

RUN microdnf install -y wget && \
    wget https://apache.osuosl.org/maven/maven-3/3.9.6/binaries/apache-maven-3.9.6-bin.tar.gz && \
    tar -xzf apache-maven-3.9.6-bin.tar.gz -C /opt && \
    rm apache-maven-3.9.6-bin.tar.gz

ENV MAVEN_HOME=/opt/apache-maven-3.9.6
ENV PATH=$MAVEN_HOME/bin:$PATH

WORKDIR /app
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src src
#RUN ./mvnw package -DskipTests
RUN mvn package -DskipTests




# Fase de ejecución
FROM openjdk:21-rc-jdk-oraclelinux8
WORKDIR /app
COPY wallet /app/wallet
COPY --from=build /app/target/todochat-0.0.1-SNAPSHOT.jar /app/todochat.jar
ENTRYPOINT ["java", "-jar", "todochat.jar"]

# docker build -t todochat .
# docker run -p 8080:8080 todochat
