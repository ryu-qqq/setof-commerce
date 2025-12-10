package com.ryuqq.setof.application.auth.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import com.ryuqq.setof.domain.auth.aggregate.RefreshToken;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("RefreshTokenFactory")
class RefreshTokenFactoryTest {

    private static final String MEMBER_ID = "01936ddc-8d37-7c6e-8ad6-18c76adc9dfa";
    private static final String TOKEN_VALUE = "refresh-token-123";
    private static final long EXPIRES_IN_SECONDS = 604800L;
    private static final Instant FIXED_NOW = Instant.parse("2025-01-01T00:00:00Z");

    @Mock private ClockHolder clockHolder;

    private RefreshTokenFactory refreshTokenFactory;

    @BeforeEach
    void setUp() {
        refreshTokenFactory = new RefreshTokenFactory(clockHolder);
    }

    @Nested
    @DisplayName("create")
    class CreateTest {

        @Test
        @DisplayName("RefreshToken Aggregate 생성 성공")
        void shouldCreateRefreshTokenAggregate() {
            // Given
            Clock fixedClock = Clock.fixed(FIXED_NOW, ZoneId.of("UTC"));
            when(clockHolder.getClock()).thenReturn(fixedClock);

            // When
            RefreshToken result =
                    refreshTokenFactory.create(MEMBER_ID, TOKEN_VALUE, EXPIRES_IN_SECONDS);

            // Then
            assertNotNull(result);
            assertEquals(MEMBER_ID, result.getMemberId());
            assertEquals(TOKEN_VALUE, result.getTokenValue());
            assertEquals(EXPIRES_IN_SECONDS, result.getExpiresInSeconds());
        }

        @Test
        @DisplayName("ClockHolder에서 시간 주입 받아 생성")
        void shouldUseClockHolderForTimeInjection() {
            // Given
            Instant specificTime = Instant.parse("2025-06-15T12:30:00Z");
            Clock specificClock = Clock.fixed(specificTime, ZoneId.of("UTC"));
            when(clockHolder.getClock()).thenReturn(specificClock);

            // When
            RefreshToken result =
                    refreshTokenFactory.create(MEMBER_ID, TOKEN_VALUE, EXPIRES_IN_SECONDS);

            // Then
            assertNotNull(result);
        }
    }
}
