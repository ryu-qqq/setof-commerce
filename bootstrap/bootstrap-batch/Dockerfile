# ============================================================
# SetOf Commerce - Legacy Batch Dockerfile
# ============================================================
# Spring Batch jobs for legacy system integration
# - Sellic 외부몰 주문 동기화
# - 배송 조회 (SweetTracker API)
# - 알림톡 발송
# - 주문 상태 업데이트
# Port: 8090
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

# 배치 모듈 build.gradle 복사
COPY bootstrap/bootstrap-batch/build.gradle bootstrap/bootstrap-batch/

# 의존성 캐시 레이어 (소스 변경 시 재활용)
RUN chmod +x gradlew && ./gradlew dependencies --no-daemon || true

# 배치 소스코드 복사
COPY bootstrap/bootstrap-batch/src bootstrap/bootstrap-batch/src

# 빌드 (테스트 제외)
RUN ./gradlew :bootstrap:bootstrap-batch:bootJar --no-daemon -x test

# ============================================================
# Stage 2: Runtime
# ============================================================
FROM eclipse-temurin:21-jre-alpine AS runtime

# 보안: non-root 사용자 생성
RUN addgroup -g 1000 appgroup && \
    adduser -u 1000 -G appgroup -D appuser

WORKDIR /app

# JAR 복사
COPY --from=builder /app/bootstrap/bootstrap-batch/build/libs/*.jar app.jar

# 소유권 변경
RUN chown -R appuser:appgroup /app

USER appuser

# Health check (포트 8090)
HEALTHCHECK --interval=30s --timeout=10s --start-period=120s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8090/actuator/health || exit 1

# JVM 옵션 (배치 작업 최적화)
ENV JAVA_OPTS="-XX:+UseContainerSupport \
    -XX:MaxRAMPercentage=75.0 \
    -XX:InitialRAMPercentage=50.0 \
    -XX:+UseG1GC \
    -XX:+ExitOnOutOfMemoryError \
    -Djava.security.egd=file:/dev/./urandom \
    -Dspring.batch.job.enabled=false"

EXPOSE 8090

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
