FROM maven:3.9-eclipse-temurin-21 AS build   # start with a machine that has Maven + Java 21
WORKDIR /app                                  # work inside the /app folder
COPY pom.xml .                                # copy your dependency list
RUN mvn -q dependency:go-offline              # download all libraries (cached for faster rebuilds)
COPY src ./src                                # copy your source code
RUN mvn -q clean package -DskipTests          # build the app into a .jar file

FROM eclipse-temurin:21-jre                   # start fresh with a small machine that only has Java 21
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar   # take only the finished .jar from step one
EXPOSE 8080                                   # the app listens on port 8080
ENTRYPOINT ["java", "-jar", "app.jar"]        # the command that starts your app