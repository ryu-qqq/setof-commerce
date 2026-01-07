# ========================================
# Bootstrap: Migration Application
# ========================================
# Multi-stage build for optimized image size
# Base: Eclipse Temurin 21 Alpine
# ========================================

# ========================================
# Stage 1: Builder
# ========================================
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

# Copy Gradle wrapper and build files
COPY gradlew ./
COPY gradle ./gradle
COPY build.gradle settings.gradle gradle.properties ./
COPY gradle/libs.versions.toml ./gradle/

# Copy all modules (필요한 의존성 포함)
COPY domain ./domain
COPY application ./application
COPY adapter-out ./adapter-out
COPY bootstrap/bootstrap-migration ./bootstrap/bootstrap-migration

# Make gradlew executable
RUN chmod +x ./gradlew

# Build the application
RUN ./gradlew :bootstrap:bootstrap-migration:bootJar -x test --no-daemon

# ========================================
# Stage 2: Runtime
# ========================================
FROM eclipse-temurin:21-jre-alpine AS runtime

WORKDIR /app

# Create non-root user for security
RUN addgroup -g 1000 appgroup && \
    adduser -u 1000 -G appgroup -D appuser

# Copy the built jar from builder stage
COPY --from=builder /app/bootstrap/bootstrap-migration/build/libs/setof-commerce-migration.jar app.jar

# Change ownership to non-root user
RUN chown -R appuser:appgroup /app

# Switch to non-root user
USER appuser

# Health check configuration
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8082/actuator/health || exit 1

# JVM optimization for containerized environment
ENV JAVA_OPTS="-XX:+UseContainerSupport \
    -XX:MaxRAMPercentage=75.0 \
    -XX:InitialRAMPercentage=50.0 \
    -XX:+UseG1GC \
    -XX:+HeapDumpOnOutOfMemoryError \
    -XX:HeapDumpPath=/tmp \
    -Djava.security.egd=file:/dev/./urandom \
    -Dfile.encoding=UTF-8"

# Expose port
EXPOSE 8082

# Start the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
