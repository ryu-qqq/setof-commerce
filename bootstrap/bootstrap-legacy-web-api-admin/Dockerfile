# ============================================================
# SetOf Commerce - Legacy Admin API Dockerfile
# ============================================================
# Strangler Fig Pattern: Partner Admin server migration to ECS
# Pre-built JAR copy for faster builds
# Base: Eclipse Temurin JRE 21 (Alpine)
# Port: 8089 (Legacy Admin server port)
# ============================================================
# IMPORTANT: Run before docker build:
#   ./gradlew :bootstrap:bootstrap-legacy-web-api-admin:bootJar -x test
# ============================================================

FROM eclipse-temurin:21-jre-alpine AS runtime

# 보안: non-root 사용자 생성
RUN addgroup -g 1000 appgroup && \
    adduser -u 1000 -G appgroup -D appuser

WORKDIR /app

# 로컬에서 빌드한 JAR 복사
COPY bootstrap/bootstrap-legacy-web-api-admin/build/libs/*.jar app.jar

# 소유권 변경
RUN chown -R appuser:appgroup /app

USER appuser

# Health check (포트 8089)
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8089/actuator/health || exit 1

# JVM 옵션 (컨테이너 최적화)
ENV JAVA_OPTS="-XX:+UseContainerSupport \
    -XX:MaxRAMPercentage=75.0 \
    -XX:InitialRAMPercentage=50.0 \
    -XX:+UseG1GC \
    -XX:+ExitOnOutOfMemoryError \
    -Djava.security.egd=file:/dev/./urandom"

EXPOSE 8089

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
