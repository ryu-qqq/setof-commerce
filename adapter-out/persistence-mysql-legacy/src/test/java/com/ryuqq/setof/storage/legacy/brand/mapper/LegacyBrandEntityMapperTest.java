package com.ryuqq.setof.storage.legacy.brand.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.brand.aggregate.Brand;
import com.ryuqq.setof.storage.legacy.brand.LegacyBrandEntityFixtures;
import com.ryuqq.setof.storage.legacy.brand.entity.LegacyBrandEntity;
import com.ryuqq.setof.storage.legacy.brand.entity.LegacyBrandEntity.MainDisplayNameType;
import com.ryuqq.setof.storage.legacy.common.Yn;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * LegacyBrandEntityMapper 단위 테스트.
 *
 * <p>레거시 Entity → Domain 변환 로직 검증.
 */
@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("LegacyBrandEntityMapper 테스트")
class LegacyBrandEntityMapperTest {

    private final LegacyBrandEntityMapper mapper = new LegacyBrandEntityMapper();

    @Nested
    @DisplayName("toDomain 메서드 테스트")
    class ToDomainTest {

        @Test
        @DisplayName("정상 변환 - KOREAN 타입일 때 한글 표시명 사용")
        void shouldConvertEntityToDomainWithKoreanDisplayName() {
            // given
            LegacyBrandEntity entity =
                    LegacyBrandEntityFixtures.builder()
                            .id(1L)
                            .brandName("나이키")
                            .brandIconImageUrl("https://example.com/nike-icon.png")
                            .displayEnglishName("Nike")
                            .displayKoreanName("나이키 코리아")
                            .mainDisplayType(MainDisplayNameType.KOREAN)
                            .displayOrder(5)
                            .displayYn(Yn.Y)
                            .insertDate(LocalDateTime.of(2024, 1, 15, 10, 30))
                            .updateDate(LocalDateTime.of(2024, 2, 20, 14, 45))
                            .build();

            // when
            Brand brand = mapper.toDomain(entity);

            // then
            assertThat(brand.id().value()).isEqualTo(1L);
            assertThat(brand.brandName().value()).isEqualTo("나이키");
            assertThat(brand.brandIconImageUrl().value())
                    .isEqualTo("https://example.com/nike-icon.png");
            assertThat(brand.displayName().value()).isEqualTo("나이키 코리아");
            assertThat(brand.displayOrder().value()).isEqualTo(5);
            assertThat(brand.displayed()).isTrue();

            Instant expectedCreatedAt =
                    LocalDateTime.of(2024, 1, 15, 10, 30)
                            .atZone(ZoneId.systemDefault())
                            .toInstant();
            Instant expectedUpdatedAt =
                    LocalDateTime.of(2024, 2, 20, 14, 45)
                            .atZone(ZoneId.systemDefault())
                            .toInstant();
            assertThat(brand.createdAt()).isEqualTo(expectedCreatedAt);
            assertThat(brand.updatedAt()).isEqualTo(expectedUpdatedAt);
        }

        @Test
        @DisplayName("ENGLISH 타입일 때 영문 표시명 사용")
        void shouldUseEnglishDisplayNameWhenTypeIsEnglish() {
            // given
            LegacyBrandEntity entity =
                    LegacyBrandEntityFixtures.builder()
                            .displayEnglishName("Adidas")
                            .displayKoreanName("아디다스 코리아")
                            .mainDisplayType(MainDisplayNameType.ENGLISH)
                            .build();

            // when
            Brand brand = mapper.toDomain(entity);

            // then
            assertThat(brand.displayName().value()).isEqualTo("Adidas");
        }

        @Test
        @DisplayName("KOREAN 타입이지만 한글 표시명이 null일 때 영문 표시명 폴백")
        void shouldFallbackToEnglishWhenKoreanNameIsNull() {
            // given
            LegacyBrandEntity entity =
                    LegacyBrandEntityFixtures.builder()
                            .displayEnglishName("Puma")
                            .displayKoreanName(null)
                            .mainDisplayType(MainDisplayNameType.KOREAN)
                            .build();

            // when
            Brand brand = mapper.toDomain(entity);

            // then
            assertThat(brand.displayName().value()).isEqualTo("Puma");
        }

        @Test
        @DisplayName("ENGLISH 타입이지만 영문 표시명이 null일 때 한글 표시명 폴백")
        void shouldFallbackToKoreanWhenEnglishNameIsNull() {
            // given
            LegacyBrandEntity entity =
                    LegacyBrandEntityFixtures.builder()
                            .displayEnglishName(null)
                            .displayKoreanName("뉴발란스 코리아")
                            .mainDisplayType(MainDisplayNameType.ENGLISH)
                            .build();

            // when
            Brand brand = mapper.toDomain(entity);

            // then
            assertThat(brand.displayName().value()).isEqualTo("뉴발란스 코리아");
        }

        @Test
        @DisplayName("displayed 필드는 displayYn.toBoolean() 결과 사용")
        void shouldConvertDisplayYnToBoolean() {
            // given
            LegacyBrandEntity displayedEntity =
                    LegacyBrandEntityFixtures.builder().displayYn(Yn.Y).build();

            LegacyBrandEntity notDisplayedEntity =
                    LegacyBrandEntityFixtures.builder().displayYn(Yn.N).build();

            // when
            Brand displayedBrand = mapper.toDomain(displayedEntity);
            Brand notDisplayedBrand = mapper.toDomain(notDisplayedEntity);

            // then
            assertThat(displayedBrand.displayed()).isTrue();
            assertThat(notDisplayedBrand.displayed()).isFalse();
        }

        @Test
        @DisplayName("insertDate가 null일 때 현재 시간으로 대체")
        void shouldUseCurrentTimeWhenInsertDateIsNull() {
            // given
            Instant beforeTest = Instant.now();
            LegacyBrandEntity entity =
                    LegacyBrandEntityFixtures.builder().insertDate(null).updateDate(null).build();

            // when
            Brand brand = mapper.toDomain(entity);

            // then
            Instant afterTest = Instant.now();
            assertThat(brand.createdAt()).isBetween(beforeTest, afterTest);
            assertThat(brand.updatedAt()).isBetween(beforeTest, afterTest);
        }
    }
}
