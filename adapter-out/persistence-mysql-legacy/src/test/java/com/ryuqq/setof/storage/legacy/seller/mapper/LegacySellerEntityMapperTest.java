package com.ryuqq.setof.storage.legacy.seller.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.seller.aggregate.Seller;
import com.ryuqq.setof.storage.legacy.seller.LegacySellerEntityFixtures;
import com.ryuqq.setof.storage.legacy.seller.entity.LegacySellerEntity;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * LegacySellerEntityMapper 단위 테스트.
 *
 * <p>레거시 Entity → 새 Domain 변환 로직을 검증합니다.
 */
@DisplayName("레거시 셀러 Entity-Domain 매퍼 테스트")
@Tag("unit")
@ExtendWith(MockitoExtension.class)
class LegacySellerEntityMapperTest {

    @InjectMocks private LegacySellerEntityMapper mapper;

    @Nested
    @DisplayName("toDomain 메서드 테스트")
    class ToDomainTest {

        @Test
        @DisplayName("정상적인 Entity를 Domain으로 변환합니다")
        void shouldMapEntityToDomain() {
            // given
            LegacySellerEntity entity =
                    LegacySellerEntityFixtures.builder()
                            .id(1L)
                            .sellerName("테스트 셀러")
                            .sellerLogoUrl("https://example.com/logo.png")
                            .sellerDescription("테스트 셀러 설명")
                            .commissionRate(10.0)
                            .createdAt(LocalDateTime.of(2026, 1, 1, 10, 0))
                            .updatedAt(LocalDateTime.of(2026, 1, 2, 12, 0))
                            .build();

            // when
            Seller domain = mapper.toDomain(entity);

            // then
            assertThat(domain.idValue()).isEqualTo(1L);
            assertThat(domain.sellerNameValue()).isEqualTo("테스트 셀러");
            assertThat(domain.displayNameValue())
                    .isEqualTo("테스트 셀러"); // displayName은 sellerName으로 대체
            assertThat(domain.logoUrlValue()).isEqualTo("https://example.com/logo.png");
            assertThat(domain.descriptionValue()).isEqualTo("테스트 셀러 설명");
            assertThat(domain.isActive()).isTrue(); // 레거시에는 active 컬럼 없음, 기본값 true
            assertThat(domain.deletedAt()).isNull(); // 레거시에는 deletedAt 컬럼 없음
            assertThat(domain.authTenantId()).isNull(); // 레거시에는 authTenantId 컬럼 없음
            assertThat(domain.authOrganizationId()).isNull(); // 레거시에는 authOrganizationId 컬럼 없음
        }

        @Test
        @DisplayName("null createdAt은 현재 시간으로 대체됩니다")
        void shouldReplaceNullCreatedAtWithNow() {
            // given
            LegacySellerEntity entity =
                    LegacySellerEntityFixtures.builder()
                            .id(1L)
                            .sellerName("테스트 셀러")
                            .createdAt(null)
                            .updatedAt(LocalDateTime.of(2026, 1, 2, 12, 0))
                            .build();

            // when
            Instant before = Instant.now();
            Seller domain = mapper.toDomain(entity);
            Instant after = Instant.now();

            // then
            assertThat(domain.createdAt()).isBetween(before, after);
        }

        @Test
        @DisplayName("null updatedAt은 현재 시간으로 대체됩니다")
        void shouldReplaceNullUpdatedAtWithNow() {
            // given
            LegacySellerEntity entity =
                    LegacySellerEntityFixtures.builder()
                            .id(1L)
                            .sellerName("테스트 셀러")
                            .createdAt(LocalDateTime.of(2026, 1, 1, 10, 0))
                            .updatedAt(null)
                            .build();

            // when
            Instant before = Instant.now();
            Seller domain = mapper.toDomain(entity);
            Instant after = Instant.now();

            // then
            assertThat(domain.updatedAt()).isBetween(before, after);
        }

        @Test
        @DisplayName("null sellerDescription은 그대로 null로 변환됩니다")
        void shouldMapNullDescriptionAsNull() {
            // given
            LegacySellerEntity entity =
                    LegacySellerEntityFixtures.builder()
                            .id(1L)
                            .sellerName("테스트 셀러")
                            .sellerDescription(null)
                            .build();

            // when
            Seller domain = mapper.toDomain(entity);

            // then
            assertThat(domain.descriptionValue()).isNull();
        }

        @Test
        @DisplayName("null sellerLogoUrl은 그대로 null로 변환됩니다")
        void shouldMapNullLogoUrlAsNull() {
            // given
            LegacySellerEntity entity =
                    LegacySellerEntityFixtures.builder()
                            .id(1L)
                            .sellerName("테스트 셀러")
                            .sellerLogoUrl(null)
                            .build();

            // when
            Seller domain = mapper.toDomain(entity);

            // then
            assertThat(domain.logoUrlValue()).isNull();
        }

        @Test
        @DisplayName("LocalDateTime이 Instant로 올바르게 변환됩니다")
        void shouldConvertLocalDateTimeToInstant() {
            // given
            LocalDateTime createdAt = LocalDateTime.of(2026, 1, 1, 10, 0);
            LegacySellerEntity entity =
                    LegacySellerEntityFixtures.builder()
                            .id(1L)
                            .sellerName("테스트 셀러")
                            .createdAt(createdAt)
                            .updatedAt(createdAt)
                            .build();

            // when
            Seller domain = mapper.toDomain(entity);

            // then
            Instant expected = createdAt.atZone(ZoneId.systemDefault()).toInstant();
            assertThat(domain.createdAt()).isEqualTo(expected);
            assertThat(domain.updatedAt()).isEqualTo(expected);
        }
    }
}
