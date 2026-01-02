# ============================================================
# SetOf Commerce - Legacy Web API Dockerfile
# ============================================================
# Strangler Fig Pattern: Legacy server migration to ECS
# Multi-stage build for optimized image size
# Base: Eclipse Temurin JDK 21 (Alpine)
# Port: 8088 (Legacy server port)
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

# 레거시 모듈 build.gradle 복사
COPY bootstrap/bootstrap-legacy-web-api/build.gradle bootstrap/bootstrap-legacy-web-api/

# 의존성 캐시 레이어 (소스 변경 시 재활용)
RUN chmod +x gradlew && ./gradlew dependencies --no-daemon || true

# 레거시 소스코드 복사
COPY bootstrap/bootstrap-legacy-web-api/src bootstrap/bootstrap-legacy-web-api/src

# 빌드 (테스트 제외)
RUN ./gradlew :bootstrap:bootstrap-legacy-web-api:bootJar --no-daemon -x test

# ============================================================
# Stage 2: Runtime
# ============================================================
FROM eclipse-temurin:21-jre-alpine AS runtime

# 보안: non-root 사용자 생성
RUN addgroup -g 1000 appgroup && \
    adduser -u 1000 -G appgroup -D appuser

WORKDIR /app

# JAR 복사 (Spring Boot 기본 JAR 이름 형식)
COPY --from=builder /app/bootstrap/bootstrap-legacy-web-api/build/libs/*.jar app.jar

# 소유권 변경
RUN chown -R appuser:appgroup /app

USER appuser

# Health check (포트 8088)
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8088/actuator/health || exit 1

# JVM 옵션 (컨테이너 최적화)
ENV JAVA_OPTS="-XX:+UseContainerSupport \
    -XX:MaxRAMPercentage=75.0 \
    -XX:InitialRAMPercentage=50.0 \
    -XX:+UseG1GC \
    -XX:+ExitOnOutOfMemoryError \
    -Djava.security.egd=file:/dev/./urandom"

EXPOSE 8088

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
