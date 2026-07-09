FROM eclipse-temurin:21-jre-alpine
RUN addgroup -S appgroup && adduser -S appuser -G appgroup && apk add --no-cache curl
WORKDIR /app
COPY target/seguridad-service-1.0.0.jar app.jar
EXPOSE 8086
USER appuser
HEALTHCHECK --interval=30s --timeout=5s --retries=3 CMD curl -f http://localhost:8086/actuator/health || exit 1
ENTRYPOINT ["java", "-jar", "app.jar"]
