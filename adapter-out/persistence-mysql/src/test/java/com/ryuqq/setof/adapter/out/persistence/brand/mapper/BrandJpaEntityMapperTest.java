package com.ryuqq.setof.adapter.out.persistence.brand.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.out.persistence.brand.entity.BrandJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.common.MapperTestSupport;
import com.ryuqq.setof.domain.brand.BrandFixture;
import com.ryuqq.setof.domain.brand.aggregate.Brand;
import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * BrandJpaEntityMapper 단위 테스트
 *
 * <p>Brand Domain ↔ BrandJpaEntity 간의 변환 로직을 검증합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@DisplayName("BrandJpaEntityMapper 단위 테스트")
class BrandJpaEntityMapperTest extends MapperTestSupport {

    private BrandJpaEntityMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new BrandJpaEntityMapper();
    }

    @Nested
    @DisplayName("toEntity 메서드")
    class ToEntity {

        @Test
        @DisplayName("성공 - Brand 도메인을 Entity로 변환한다")
        void toEntity_success() {
            // Given
            Brand brand = BrandFixture.create();

            // When
            BrandJpaEntity entity = mapper.toEntity(brand);

            // Then
            assertThat(entity).isNotNull();
            assertThat(entity.getId()).isEqualTo(brand.getIdValue());
            assertThat(entity.getCode()).isEqualTo(brand.getCodeValue());
            assertThat(entity.getNameKo()).isEqualTo(brand.getNameKoValue());
            assertThat(entity.getNameEn()).isEqualTo(brand.getNameEnValue());
            assertThat(entity.getLogoUrl()).isEqualTo(brand.getLogoUrlValue());
            assertThat(entity.getStatus()).isEqualTo(brand.getStatusValue());
            assertThat(entity.getCreatedAt()).isEqualTo(brand.getCreatedAt());
            assertThat(entity.getUpdatedAt()).isEqualTo(brand.getUpdatedAt());
        }

        @Test
        @DisplayName("성공 - 비활성 Brand를 Entity로 변환한다")
        void toEntity_inactiveBrand_success() {
            // Given
            Brand inactiveBrand = BrandFixture.createInactive();

            // When
            BrandJpaEntity entity = mapper.toEntity(inactiveBrand);

            // Then
            assertThat(entity).isNotNull();
            assertThat(entity.getStatus()).isEqualTo("INACTIVE");
            assertThat(entity.getId()).isEqualTo(inactiveBrand.getIdValue());
        }

        @Test
        @DisplayName("성공 - 영문명 없는 Brand를 Entity로 변환한다")
        void toEntity_withoutEnglishName_success() {
            // Given
            Brand brand = BrandFixture.createWithoutEnglishName();

            // When
            BrandJpaEntity entity = mapper.toEntity(brand);

            // Then
            assertThat(entity).isNotNull();
            assertThat(entity.getNameEn()).isNullOrEmpty();
            assertThat(entity.getNameKo()).isNotEmpty();
        }

        @Test
        @DisplayName("성공 - 로고 없는 Brand를 Entity로 변환한다")
        void toEntity_withoutLogo_success() {
            // Given
            Brand brand = BrandFixture.createWithoutLogo();

            // When
            BrandJpaEntity entity = mapper.toEntity(brand);

            // Then
            assertThat(entity).isNotNull();
            assertThat(entity.getLogoUrl()).isNullOrEmpty();
        }

        @Test
        @DisplayName("성공 - 커스텀 Brand를 Entity로 변환한다")
        void toEntity_customBrand_success() {
            // Given
            Brand customBrand =
                    BrandFixture.createCustom(100L, "CUSTOM", "커스텀브랜드", "CustomBrand", null, true);

            // When
            BrandJpaEntity entity = mapper.toEntity(customBrand);

            // Then
            assertThat(entity.getId()).isEqualTo(100L);
            assertThat(entity.getCode()).isEqualTo("CUSTOM");
            assertThat(entity.getNameKo()).isEqualTo("커스텀브랜드");
            assertThat(entity.getNameEn()).isEqualTo("CustomBrand");
            assertThat(entity.getStatus()).isEqualTo("ACTIVE");
        }
    }

    @Nested
    @DisplayName("toDomain 메서드")
    class ToDomain {

        @Test
        @DisplayName("성공 - Entity를 Brand 도메인으로 변환한다")
        void toDomain_success() {
            // Given
            Instant now = Instant.now();
            BrandJpaEntity entity =
                    BrandJpaEntity.of(
                            1L,
                            "NIKE",
                            "나이키",
                            "Nike",
                            "https://cdn.example.com/nike.png",
                            "ACTIVE",
                            now,
                            now);

            // When
            Brand domain = mapper.toDomain(entity);

            // Then
            assertThat(domain).isNotNull();
            assertThat(domain.getIdValue()).isEqualTo(entity.getId());
            assertThat(domain.getCodeValue()).isEqualTo(entity.getCode());
            assertThat(domain.getNameKoValue()).isEqualTo(entity.getNameKo());
            assertThat(domain.getNameEnValue()).isEqualTo(entity.getNameEn());
            assertThat(domain.getLogoUrlValue()).isEqualTo(entity.getLogoUrl());
            assertThat(domain.getStatusValue()).isEqualTo(entity.getStatus());
            assertThat(domain.getCreatedAt()).isEqualTo(entity.getCreatedAt());
            assertThat(domain.getUpdatedAt()).isEqualTo(entity.getUpdatedAt());
        }

        @Test
        @DisplayName("성공 - 비활성 Entity를 도메인으로 변환한다")
        void toDomain_inactiveEntity_success() {
            // Given
            Instant now = Instant.now();
            BrandJpaEntity inactiveEntity =
                    BrandJpaEntity.of(
                            99L,
                            "INACTIVE_BRAND",
                            "비활성브랜드",
                            "InactiveBrand",
                            null,
                            "INACTIVE",
                            now,
                            now);

            // When
            Brand domain = mapper.toDomain(inactiveEntity);

            // Then
            assertThat(domain).isNotNull();
            assertThat(domain.getStatusValue()).isEqualTo("INACTIVE");
            assertThat(domain.getIdValue()).isEqualTo(99L);
        }

        @Test
        @DisplayName("성공 - 선택 필드가 null인 Entity를 도메인으로 변환한다")
        void toDomain_withNullOptionalFields_success() {
            // Given
            Instant now = Instant.now();
            BrandJpaEntity entity =
                    BrandJpaEntity.of(1L, "TEST", "테스트", null, null, "ACTIVE", now, now);

            // When
            Brand domain = mapper.toDomain(entity);

            // Then
            assertThat(domain).isNotNull();
            assertThat(domain.getIdValue()).isEqualTo(1L);
            assertThat(domain.getCodeValue()).isEqualTo("TEST");
            assertThat(domain.getNameKoValue()).isEqualTo("테스트");
        }
    }

    @Nested
    @DisplayName("양방향 변환 검증")
    class RoundTrip {

        @Test
        @DisplayName("성공 - Domain -> Entity -> Domain 변환 시 데이터가 보존된다")
        void roundTrip_domainToEntityToDomain_preservesData() {
            // Given
            Brand original = BrandFixture.create();

            // When
            BrandJpaEntity entity = mapper.toEntity(original);
            Brand converted = mapper.toDomain(entity);

            // Then
            assertThat(converted.getIdValue()).isEqualTo(original.getIdValue());
            assertThat(converted.getCodeValue()).isEqualTo(original.getCodeValue());
            assertThat(converted.getNameKoValue()).isEqualTo(original.getNameKoValue());
            assertThat(converted.getNameEnValue()).isEqualTo(original.getNameEnValue());
            assertThat(converted.getLogoUrlValue()).isEqualTo(original.getLogoUrlValue());
            assertThat(converted.getStatusValue()).isEqualTo(original.getStatusValue());
            assertThat(converted.getCreatedAt()).isEqualTo(original.getCreatedAt());
            assertThat(converted.getUpdatedAt()).isEqualTo(original.getUpdatedAt());
        }

        @Test
        @DisplayName("성공 - Entity -> Domain -> Entity 변환 시 데이터가 보존된다")
        void roundTrip_entityToDomainToEntity_preservesData() {
            // Given
            Instant now = Instant.now();
            BrandJpaEntity original =
                    BrandJpaEntity.of(
                            5L,
                            "ADIDAS",
                            "아디다스",
                            "Adidas",
                            "https://cdn.example.com/adidas.png",
                            "ACTIVE",
                            now,
                            now);

            // When
            Brand domain = mapper.toDomain(original);
            BrandJpaEntity converted = mapper.toEntity(domain);

            // Then
            assertThat(converted.getId()).isEqualTo(original.getId());
            assertThat(converted.getCode()).isEqualTo(original.getCode());
            assertThat(converted.getNameKo()).isEqualTo(original.getNameKo());
            assertThat(converted.getNameEn()).isEqualTo(original.getNameEn());
            assertThat(converted.getLogoUrl()).isEqualTo(original.getLogoUrl());
            assertThat(converted.getStatus()).isEqualTo(original.getStatus());
            assertThat(converted.getCreatedAt()).isEqualTo(original.getCreatedAt());
            assertThat(converted.getUpdatedAt()).isEqualTo(original.getUpdatedAt());
        }

        @Test
        @DisplayName("성공 - 여러 Brand 목록의 양방향 변환 시 데이터가 보존된다")
        void roundTrip_listConversion_preservesData() {
            // Given
            var brands = BrandFixture.createList();

            // When & Then
            for (Brand original : brands) {
                BrandJpaEntity entity = mapper.toEntity(original);
                Brand converted = mapper.toDomain(entity);

                assertThat(converted.getIdValue()).isEqualTo(original.getIdValue());
                assertThat(converted.getCodeValue()).isEqualTo(original.getCodeValue());
            }
        }
    }
}
