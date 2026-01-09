package com.setof.connectly;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

/**
 * Spring Context 로드 테스트.
 *
 * <p>이 테스트는 애플리케이션의 모든 빈이 올바르게 설정되어 있는지 검증합니다.
 * 빈 누락, 순환 의존성 등의 문제가 있으면 이 테스트에서 실패합니다.
 *
 * <p>실제 AWS 서비스 연결은 Mock으로 대체됩니다.
 */
@SpringBootTest
@ActiveProfiles("test")
class LegacyWebApiApplicationTest {

    // AWS 클라이언트는 실제 연결 시도를 방지하기 위해 Mock 처리
    @MockitoBean
    private S3Client s3Client;

    @MockitoBean
    private S3Presigner s3Presigner;

    // Redis는 CI 환경에 없으므로 Mock 처리
    @MockitoBean
    private StringRedisTemplate stringRedisTemplate;

    @Test
    @DisplayName("Spring Context가 정상적으로 로드되어야 한다")
    void contextLoads() {
        // 컨텍스트 로드 성공 = 모든 빈이 정상 생성됨
        // 이 테스트가 통과하면 ImageUploadService 등 모든 빈이 제대로 설정된 것
    }
}
