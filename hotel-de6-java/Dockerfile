FROM maven:3.9.9-eclipse-temurin-21-alpine AS build
WORKDIR /src
COPY pom.xml .
COPY src ./src
RUN mvn -B -DskipTests package
FROM eclipse-temurin:21-jre-alpine
RUN addgroup -S hotel && adduser -S hotel -G hotel
WORKDIR /app
COPY --from=build /src/target/hotel-de6-1.0.0.jar app.jar
USER hotel
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
