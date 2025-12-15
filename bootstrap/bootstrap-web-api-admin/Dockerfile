# ============================================================
# SetOf Commerce - Web API Admin Dockerfile
# ============================================================
# Multi-stage build for optimized image size
# Base: Eclipse Temurin JDK 21 (Alpine)
# Port: 8081 (Admin API)
# ============================================================

# ============================================================
# Stage 1: Build
# ============================================================
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

# Gradle wrapper 복사
COPY gradlew .
COPY gradle gradle
COPY settings.gradle .
COPY build.gradle .
COPY gradle/libs.versions.toml gradle/

# 모듈별 build.gradle 복사
COPY domain/build.gradle domain/
COPY application/build.gradle application/
COPY adapter-in/rest-api-admin/build.gradle adapter-in/rest-api-admin/
COPY adapter-out/persistence-mysql/build.gradle adapter-out/persistence-mysql/
COPY adapter-out/persistence-redis/build.gradle adapter-out/persistence-redis/
COPY adapter-out/security/build.gradle adapter-out/security/
COPY bootstrap/bootstrap-web-api-admin/build.gradle bootstrap/bootstrap-web-api-admin/

# 의존성 캐시 레이어 (소스 변경 시 재활용)
RUN chmod +x gradlew && ./gradlew dependencies --no-daemon || true

# 소스코드 복사
COPY domain/src domain/src
COPY application/src application/src
COPY adapter-in/rest-api-admin/src adapter-in/rest-api-admin/src
COPY adapter-out/persistence-mysql/src adapter-out/persistence-mysql/src
COPY adapter-out/persistence-redis/src adapter-out/persistence-redis/src
COPY adapter-out/security/src adapter-out/security/src
COPY bootstrap/bootstrap-web-api-admin/src bootstrap/bootstrap-web-api-admin/src

# 빌드 (테스트 제외)
RUN ./gradlew :bootstrap:bootstrap-web-api-admin:bootJar --no-daemon -x test

# ============================================================
# Stage 2: Runtime
# ============================================================
FROM eclipse-temurin:21-jre-alpine AS runtime

# 보안: non-root 사용자 생성
RUN addgroup -g 1000 appgroup && \
    adduser -u 1000 -G appgroup -D appuser

WORKDIR /app

# JAR 복사
COPY --from=builder /app/bootstrap/bootstrap-web-api-admin/build/libs/setof-commerce-web-api-admin.jar app.jar

# 소유권 변경
RUN chown -R appuser:appgroup /app

USER appuser

# Health check (Port 8081)
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8081/actuator/health || exit 1

# JVM 옵션 (컨테이너 최적화)
ENV JAVA_OPTS="-XX:+UseContainerSupport \
    -XX:MaxRAMPercentage=75.0 \
    -XX:InitialRAMPercentage=50.0 \
    -XX:+UseG1GC \
    -XX:+ExitOnOutOfMemoryError \
    -Djava.security.egd=file:/dev/./urandom"

EXPOSE 8081

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
