package com.ryuqq.setof.storage.legacy.faq.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.faq.aggregate.Faq;
import com.ryuqq.setof.domain.faq.vo.FaqType;
import com.ryuqq.setof.storage.legacy.faq.LegacyFaqEntityFixtures;
import com.ryuqq.setof.storage.legacy.faq.entity.LegacyFaqEntity;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * LegacyFaqEntityMapper 단위 테스트.
 *
 * <p>레거시 Entity → Domain 변환 로직을 검증합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@DisplayName("LegacyFaqEntityMapper 단위 테스트")
@ExtendWith(MockitoExtension.class)
class LegacyFaqEntityMapperTest {

    private LegacyFaqEntityMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new LegacyFaqEntityMapper();
    }

    @Nested
    @DisplayName("toDomain 메서드 테스트")
    class ToDomainTest {

        @Test
        @DisplayName("Entity를 Domain으로 정상 변환한다")
        void shouldConvertEntityToDomain() {
            // given
            LegacyFaqEntity entity =
                    LegacyFaqEntityFixtures.builder()
                            .id(1L)
                            .faqType(com.ryuqq.setof.domain.legacy.faq.FaqType.MEMBER_LOGIN)
                            .title("회원가입 방법")
                            .contents("회원가입 절차 안내")
                            .displayOrder(10)
                            .topDisplayOrder(5)
                            .insertDate(LocalDateTime.of(2026, 1, 1, 10, 0))
                            .updateDate(LocalDateTime.of(2026, 1, 2, 15, 30))
                            .build();

            // when
            Faq result = mapper.toDomain(entity);

            // then
            assertThat(result).isNotNull();
            assertThat(result.idValue()).isEqualTo(1L);
            assertThat(result.faqType()).isEqualTo(FaqType.MEMBER_LOGIN);
            assertThat(result.titleValue()).isEqualTo("회원가입 방법");
            assertThat(result.contentsValue()).isEqualTo("회원가입 절차 안내");
            assertThat(result.displayOrderValue()).isEqualTo(10);
            assertThat(result.topDisplayOrder()).isEqualTo(5);
            assertThat(result.createdAt())
                    .isEqualTo(
                            LocalDateTime.of(2026, 1, 1, 10, 0)
                                    .atZone(ZoneId.systemDefault())
                                    .toInstant());
            assertThat(result.updatedAt())
                    .isEqualTo(
                            LocalDateTime.of(2026, 1, 2, 15, 30)
                                    .atZone(ZoneId.systemDefault())
                                    .toInstant());
        }

        @Test
        @DisplayName("레거시 FaqType을 신규 FaqType으로 변환한다")
        void shouldConvertLegacyFaqTypeToNewFaqType() {
            // given
            LegacyFaqEntity entity =
                    LegacyFaqEntityFixtures.builder()
                            .faqType(com.ryuqq.setof.domain.legacy.faq.FaqType.TOP)
                            .build();

            // when
            Faq result = mapper.toDomain(entity);

            // then
            assertThat(result.faqType()).isEqualTo(FaqType.TOP);
            assertThat(result.isTop()).isTrue();
        }

        @Test
        @DisplayName("displayOrder가 null이면 0으로 변환한다")
        void shouldConvertNullDisplayOrderToZero() {
            // given
            LegacyFaqEntity entity = LegacyFaqEntityFixtures.builder().displayOrder(null).build();

            // when
            Faq result = mapper.toDomain(entity);

            // then
            assertThat(result.displayOrderValue()).isEqualTo(0);
        }

        @Test
        @DisplayName("insertDate가 null이면 현재 시간으로 설정한다")
        void shouldUseCurrentTimeWhenInsertDateIsNull() {
            // given
            LegacyFaqEntity entity = LegacyFaqEntityFixtures.builder().insertDate(null).build();

            // when
            Faq result = mapper.toDomain(entity);

            // then
            assertThat(result.createdAt()).isNotNull();
        }

        @Test
        @DisplayName("updateDate가 null이면 현재 시간으로 설정한다")
        void shouldUseCurrentTimeWhenUpdateDateIsNull() {
            // given
            LegacyFaqEntity entity = LegacyFaqEntityFixtures.builder().updateDate(null).build();

            // when
            Faq result = mapper.toDomain(entity);

            // then
            assertThat(result.updatedAt()).isNotNull();
        }

        @Test
        @DisplayName("TOP 유형이 아닌 경우 일반 FAQ로 변환한다")
        void shouldConvertNonTopFaqType() {
            // given
            LegacyFaqEntity entity =
                    LegacyFaqEntityFixtures.builder()
                            .faqType(com.ryuqq.setof.domain.legacy.faq.FaqType.SHIPPING)
                            .build();

            // when
            Faq result = mapper.toDomain(entity);

            // then
            assertThat(result.faqType()).isEqualTo(FaqType.SHIPPING);
            assertThat(result.isTop()).isFalse();
        }
    }
}
