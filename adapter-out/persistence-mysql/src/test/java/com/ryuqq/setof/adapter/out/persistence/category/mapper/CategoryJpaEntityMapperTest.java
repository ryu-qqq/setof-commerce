package com.ryuqq.setof.adapter.out.persistence.category.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.out.persistence.category.entity.CategoryJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.common.MapperTestSupport;
import com.ryuqq.setof.domain.category.CategoryFixture;
import com.ryuqq.setof.domain.category.aggregate.Category;
import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * CategoryJpaEntityMapper 단위 테스트
 *
 * <p>Category Domain ↔ CategoryJpaEntity 간의 변환 로직을 검증합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@DisplayName("CategoryJpaEntityMapper 단위 테스트")
class CategoryJpaEntityMapperTest extends MapperTestSupport {

    private CategoryJpaEntityMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new CategoryJpaEntityMapper();
    }

    @Nested
    @DisplayName("toEntity 메서드")
    class ToEntity {

        @Test
        @DisplayName("성공 - 최상위 Category를 Entity로 변환한다")
        void toEntity_rootCategory_success() {
            // Given
            Category category = CategoryFixture.createRoot();

            // When
            CategoryJpaEntity entity = mapper.toEntity(category);

            // Then
            assertThat(entity).isNotNull();
            assertThat(entity.getId()).isEqualTo(category.getIdValue());
            assertThat(entity.getCode()).isEqualTo(category.getCodeValue());
            assertThat(entity.getNameKo()).isEqualTo(category.getNameKoValue());
            assertThat(entity.getParentId()).isNull();
            assertThat(entity.getDepth()).isEqualTo(category.getDepthValue());
            assertThat(entity.getPath()).isEqualTo(category.getPathValue());
            assertThat(entity.getSortOrder()).isEqualTo(category.getSortOrder());
            assertThat(entity.isLeaf()).isEqualTo(category.isLeaf());
            assertThat(entity.getStatus()).isEqualTo(category.getStatusValue());
            assertThat(entity.getCreatedAt()).isEqualTo(category.getCreatedAt());
            assertThat(entity.getUpdatedAt()).isEqualTo(category.getUpdatedAt());
        }

        @Test
        @DisplayName("성공 - 중분류 Category를 Entity로 변환한다")
        void toEntity_middleCategory_success() {
            // Given
            Category category = CategoryFixture.createMiddle();

            // When
            CategoryJpaEntity entity = mapper.toEntity(category);

            // Then
            assertThat(entity).isNotNull();
            assertThat(entity.getParentId()).isNotNull();
            assertThat(entity.getDepth()).isEqualTo(1);
            assertThat(entity.isLeaf()).isFalse();
        }

        @Test
        @DisplayName("성공 - 소분류(리프) Category를 Entity로 변환한다")
        void toEntity_leafCategory_success() {
            // Given
            Category category = CategoryFixture.createSmall();

            // When
            CategoryJpaEntity entity = mapper.toEntity(category);

            // Then
            assertThat(entity).isNotNull();
            assertThat(entity.getDepth()).isEqualTo(2);
            assertThat(entity.isLeaf()).isTrue();
        }

        @Test
        @DisplayName("성공 - 비활성 Category를 Entity로 변환한다")
        void toEntity_inactiveCategory_success() {
            // Given
            Category inactiveCategory = CategoryFixture.createInactive();

            // When
            CategoryJpaEntity entity = mapper.toEntity(inactiveCategory);

            // Then
            assertThat(entity).isNotNull();
            assertThat(entity.getStatus()).isEqualTo("INACTIVE");
            assertThat(entity.getId()).isEqualTo(inactiveCategory.getIdValue());
        }

        @Test
        @DisplayName("성공 - 커스텀 Category를 Entity로 변환한다")
        void toEntity_customCategory_success() {
            // Given
            Category customCategory =
                    CategoryFixture.createCustom(
                            100L, 1L, "CUSTOM", "커스텀카테고리", "/1/100/", 1, 5, false, true);

            // When
            CategoryJpaEntity entity = mapper.toEntity(customCategory);

            // Then
            assertThat(entity.getId()).isEqualTo(100L);
            assertThat(entity.getParentId()).isEqualTo(1L);
            assertThat(entity.getCode()).isEqualTo("CUSTOM");
            assertThat(entity.getNameKo()).isEqualTo("커스텀카테고리");
            assertThat(entity.getPath()).isEqualTo("/1/100/");
            assertThat(entity.getDepth()).isEqualTo(1);
            assertThat(entity.getSortOrder()).isEqualTo(5);
            assertThat(entity.isLeaf()).isFalse();
            assertThat(entity.getStatus()).isEqualTo("ACTIVE");
        }
    }

    @Nested
    @DisplayName("toDomain 메서드")
    class ToDomain {

        @Test
        @DisplayName("성공 - Entity를 Category 도메인으로 변환한다")
        void toDomain_success() {
            // Given
            Instant now = Instant.now();
            CategoryJpaEntity entity =
                    CategoryJpaEntity.of(
                            1L, "FASHION", "패션", null, 0, "/1/", 1, false, "ACTIVE", now, now);

            // When
            Category domain = mapper.toDomain(entity);

            // Then
            assertThat(domain).isNotNull();
            assertThat(domain.getIdValue()).isEqualTo(entity.getId());
            assertThat(domain.getCodeValue()).isEqualTo(entity.getCode());
            assertThat(domain.getNameKoValue()).isEqualTo(entity.getNameKo());
            assertThat(domain.getParentId()).isNull();
            assertThat(domain.getDepthValue()).isEqualTo(entity.getDepth());
            assertThat(domain.getPathValue()).isEqualTo(entity.getPath());
            assertThat(domain.getSortOrder()).isEqualTo(entity.getSortOrder());
            assertThat(domain.isLeaf()).isEqualTo(entity.isLeaf());
            assertThat(domain.getStatusValue()).isEqualTo(entity.getStatus());
            assertThat(domain.getCreatedAt()).isEqualTo(entity.getCreatedAt());
            assertThat(domain.getUpdatedAt()).isEqualTo(entity.getUpdatedAt());
        }

        @Test
        @DisplayName("성공 - 부모가 있는 Entity를 도메인으로 변환한다")
        void toDomain_withParent_success() {
            // Given
            Instant now = Instant.now();
            CategoryJpaEntity entity =
                    CategoryJpaEntity.of(
                            5L, "CLOTHING", "의류", 1L, 1, "/1/5/", 1, false, "ACTIVE", now, now);

            // When
            Category domain = mapper.toDomain(entity);

            // Then
            assertThat(domain).isNotNull();
            assertThat(domain.getParentId()).isEqualTo(1L);
            assertThat(domain.getDepthValue()).isEqualTo(1);
        }

        @Test
        @DisplayName("성공 - 리프 노드 Entity를 도메인으로 변환한다")
        void toDomain_leafNode_success() {
            // Given
            Instant now = Instant.now();
            CategoryJpaEntity entity =
                    CategoryJpaEntity.of(
                            23L, "TOPS", "상의", 5L, 2, "/1/5/23/", 1, true, "ACTIVE", now, now);

            // When
            Category domain = mapper.toDomain(entity);

            // Then
            assertThat(domain).isNotNull();
            assertThat(domain.isLeaf()).isTrue();
            assertThat(domain.getDepthValue()).isEqualTo(2);
        }

        @Test
        @DisplayName("성공 - 비활성 Entity를 도메인으로 변환한다")
        void toDomain_inactiveEntity_success() {
            // Given
            Instant now = Instant.now();
            CategoryJpaEntity inactiveEntity =
                    CategoryJpaEntity.of(
                            99L,
                            "INACTIVE",
                            "비활성카테고리",
                            null,
                            0,
                            "/99/",
                            99,
                            true,
                            "INACTIVE",
                            now,
                            now);

            // When
            Category domain = mapper.toDomain(inactiveEntity);

            // Then
            assertThat(domain).isNotNull();
            assertThat(domain.getStatusValue()).isEqualTo("INACTIVE");
            assertThat(domain.getIdValue()).isEqualTo(99L);
        }
    }

    @Nested
    @DisplayName("양방향 변환 검증")
    class RoundTrip {

        @Test
        @DisplayName("성공 - Domain -> Entity -> Domain 변환 시 데이터가 보존된다")
        void roundTrip_domainToEntityToDomain_preservesData() {
            // Given
            Category original = CategoryFixture.createRoot();

            // When
            CategoryJpaEntity entity = mapper.toEntity(original);
            Category converted = mapper.toDomain(entity);

            // Then
            assertThat(converted.getIdValue()).isEqualTo(original.getIdValue());
            assertThat(converted.getCodeValue()).isEqualTo(original.getCodeValue());
            assertThat(converted.getNameKoValue()).isEqualTo(original.getNameKoValue());
            assertThat(converted.getParentId()).isEqualTo(original.getParentId());
            assertThat(converted.getDepthValue()).isEqualTo(original.getDepthValue());
            assertThat(converted.getPathValue()).isEqualTo(original.getPathValue());
            assertThat(converted.getSortOrder()).isEqualTo(original.getSortOrder());
            assertThat(converted.isLeaf()).isEqualTo(original.isLeaf());
            assertThat(converted.getStatusValue()).isEqualTo(original.getStatusValue());
            assertThat(converted.getCreatedAt()).isEqualTo(original.getCreatedAt());
            assertThat(converted.getUpdatedAt()).isEqualTo(original.getUpdatedAt());
        }

        @Test
        @DisplayName("성공 - Entity -> Domain -> Entity 변환 시 데이터가 보존된다")
        void roundTrip_entityToDomainToEntity_preservesData() {
            // Given
            Instant now = Instant.now();
            CategoryJpaEntity original =
                    CategoryJpaEntity.of(
                            5L, "CLOTHING", "의류", 1L, 1, "/1/5/", 1, false, "ACTIVE", now, now);

            // When
            Category domain = mapper.toDomain(original);
            CategoryJpaEntity converted = mapper.toEntity(domain);

            // Then
            assertThat(converted.getId()).isEqualTo(original.getId());
            assertThat(converted.getCode()).isEqualTo(original.getCode());
            assertThat(converted.getNameKo()).isEqualTo(original.getNameKo());
            assertThat(converted.getParentId()).isEqualTo(original.getParentId());
            assertThat(converted.getDepth()).isEqualTo(original.getDepth());
            assertThat(converted.getPath()).isEqualTo(original.getPath());
            assertThat(converted.getSortOrder()).isEqualTo(original.getSortOrder());
            assertThat(converted.isLeaf()).isEqualTo(original.isLeaf());
            assertThat(converted.getStatus()).isEqualTo(original.getStatus());
            assertThat(converted.getCreatedAt()).isEqualTo(original.getCreatedAt());
            assertThat(converted.getUpdatedAt()).isEqualTo(original.getUpdatedAt());
        }

        @Test
        @DisplayName("성공 - 계층 구조 Category 목록의 양방향 변환 시 데이터가 보존된다")
        void roundTrip_hierarchyListConversion_preservesData() {
            // Given
            var categories = CategoryFixture.createHierarchy();

            // When & Then
            for (Category original : categories) {
                CategoryJpaEntity entity = mapper.toEntity(original);
                Category converted = mapper.toDomain(entity);

                assertThat(converted.getIdValue()).isEqualTo(original.getIdValue());
                assertThat(converted.getCodeValue()).isEqualTo(original.getCodeValue());
                assertThat(converted.getParentId()).isEqualTo(original.getParentId());
                assertThat(converted.getDepthValue()).isEqualTo(original.getDepthValue());
            }
        }

        @Test
        @DisplayName("성공 - 여러 Category 목록의 양방향 변환 시 데이터가 보존된다")
        void roundTrip_listConversion_preservesData() {
            // Given
            var categories = CategoryFixture.createList();

            // When & Then
            for (Category original : categories) {
                CategoryJpaEntity entity = mapper.toEntity(original);
                Category converted = mapper.toDomain(entity);

                assertThat(converted.getIdValue()).isEqualTo(original.getIdValue());
                assertThat(converted.getCodeValue()).isEqualTo(original.getCodeValue());
            }
        }
    }
}
