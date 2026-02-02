# ============================================================
# SetOf Commerce - Scheduler Dockerfile
# ============================================================
# Outbox 패턴 기반 스케줄러:
# - SellerAuthOutbox 처리 (Identity 서비스 연동)
# - 타임아웃 Outbox 복구
# Port: 8083
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

# 모듈 build.gradle 복사
COPY domain/build.gradle domain/
COPY application/build.gradle application/
COPY adapter-in/scheduler/build.gradle adapter-in/scheduler/
COPY adapter-out/persistence-mysql/build.gradle adapter-out/persistence-mysql/
COPY adapter-out/persistence-redis/build.gradle adapter-out/persistence-redis/
COPY adapter-out/client/security-client/build.gradle adapter-out/client/security-client/
COPY adapter-out/client/authhub-client/build.gradle adapter-out/client/authhub-client/
COPY bootstrap/bootstrap-scheduler/build.gradle bootstrap/bootstrap-scheduler/

# 의존성 캐시 레이어 (소스 변경 시 재활용)
RUN chmod +x gradlew && ./gradlew dependencies --no-daemon || true

# 소스코드 복사
COPY domain/src domain/src
COPY application/src application/src
COPY adapter-in/scheduler/src adapter-in/scheduler/src
COPY adapter-out/persistence-mysql/src adapter-out/persistence-mysql/src
COPY adapter-out/persistence-redis/src adapter-out/persistence-redis/src
COPY adapter-out/client/security-client/src adapter-out/client/security-client/src
COPY adapter-out/client/authhub-client/src adapter-out/client/authhub-client/src
COPY bootstrap/bootstrap-scheduler/src bootstrap/bootstrap-scheduler/src

# 빌드 (테스트 제외)
RUN ./gradlew :bootstrap:bootstrap-scheduler:bootJar --no-daemon -x test

# ============================================================
# Stage 2: Runtime
# ============================================================
FROM eclipse-temurin:21-jre-alpine AS runtime

# 보안: non-root 사용자 생성
RUN addgroup -g 1000 appgroup && \
    adduser -u 1000 -G appgroup -D appuser

WORKDIR /app

# JAR 복사
COPY --from=builder /app/bootstrap/bootstrap-scheduler/build/libs/*.jar app.jar

# 소유권 변경
RUN chown -R appuser:appgroup /app

USER appuser

# Health check (포트 8083)
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8083/actuator/health || exit 1

# JVM 옵션 (컨테이너 최적화)
ENV JAVA_OPTS="-XX:+UseContainerSupport \
    -XX:MaxRAMPercentage=75.0 \
    -XX:InitialRAMPercentage=50.0 \
    -XX:+UseG1GC \
    -XX:+ExitOnOutOfMemoryError \
    -Djava.security.egd=file:/dev/./urandom"

EXPOSE 8083

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
