FROM eclipse-temurin:21-jre-alpine
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8085
USER appuser
ENTRYPOINT ["java", "-jar", "app.jar"]
