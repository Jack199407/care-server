# ---------- Build Stage ----------
FROM maven:3.8.6-eclipse-temurin-17 AS builder

# Set working directory for build
WORKDIR /app

# Copy full multi-module source code
COPY . .

# Build only the care-bundle module, along with its dependencies, skipping tests
RUN mvn clean package -pl care-bundle -am -Dmaven.test.skip=true

# ---------- Runtime Stage ----------
FROM eclipse-temurin:17-jre

# Set working directory for runtime
WORKDIR /app

# Define environment variables (can be overridden at runtime)
ENV JAVA_OPTS="-Xms512m -Xmx1024m"
ENV APP_PORT=8080
ENV CONFIG_DIR=/app/config
ENV LOG_DIR=/app/logs

# Create folders for config and logs
RUN mkdir -p $CONFIG_DIR $LOG_DIR

# Copy the built JAR from builder stage
COPY --from=builder /app/care-bundle/target/*.jar app.jar

# Expose application port
EXPOSE ${APP_PORT}

# Copy and make executable the entrypoint script
COPY entrypoint.sh /app/entrypoint.sh
RUN chmod +x /app/entrypoint.sh

# Run app via the entrypoint script
ENTRYPOINT ["/app/entrypoint.sh"]
