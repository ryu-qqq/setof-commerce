package com.ryuqq.setof.adapter.out.persistence.contentpage.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.out.persistence.contentpage.ContentPageJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.contentpage.entity.ContentPageJpaEntity;
import com.ryuqq.setof.domain.contentpage.aggregate.ContentPage;
import com.setof.commerce.domain.contentpage.ContentPageFixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * ContentPageJpaEntityMapperTest - 콘텐츠 페이지 Entity-Domain 매퍼 단위 테스트.
 *
 * <p>PER-MAP-002: toEntity(Domain) + toDomain(Entity) 메서드 제공.
 *
 * <p>PER-MAP-003: 순수 변환 로직만.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@DisplayName("ContentPageJpaEntityMapper 단위 테스트")
class ContentPageJpaEntityMapperTest {

    private ContentPageJpaEntityMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ContentPageJpaEntityMapper();
    }

    @Nested
    @DisplayName("toEntity 메서드 테스트")
    class ToEntityTest {

        @Test
        @DisplayName("활성 상태 Domain을 Entity로 변환합니다")
        void toEntity_WithActiveContentPage_ConvertsCorrectly() {
            ContentPage domain = ContentPageFixtures.activeContentPage();
            ContentPageJpaEntity entity = mapper.toEntity(domain);
            assertThat(entity.getId()).isEqualTo(domain.idValue());
            assertThat(entity.getTitle()).isEqualTo(domain.title());
            assertThat(entity.getMemo()).isEqualTo(domain.memo());
            assertThat(entity.getImageUrl()).isEqualTo(domain.imageUrl());
            assertThat(entity.getDisplayStartAt()).isEqualTo(domain.displayPeriod().startDate());
            assertThat(entity.getDisplayEndAt()).isEqualTo(domain.displayPeriod().endDate());
            assertThat(entity.isActive()).isTrue();
            assertThat(entity.getDeletedAt()).isNull();
        }

        @Test
        @DisplayName("비활성 상태 Domain을 Entity로 변환합니다")
        void toEntity_WithInactiveContentPage_ConvertsCorrectly() {
            ContentPage domain = ContentPageFixtures.inactiveContentPage();
            ContentPageJpaEntity entity = mapper.toEntity(domain);
            assertThat(entity.isActive()).isFalse();
            assertThat(entity.getDeletedAt()).isNull();
        }

        @Test
        @DisplayName("삭제된 상태 Domain을 Entity로 변환합니다")
        void toEntity_WithDeletedContentPage_ConvertsCorrectly() {
            ContentPage domain = ContentPageFixtures.deletedContentPage();
            ContentPageJpaEntity entity = mapper.toEntity(domain);
            assertThat(entity.getDeletedAt()).isNotNull();
        }

        @Test
        @DisplayName("새로운 Domain을 Entity로 변환합니다")
        void toEntity_WithNewContentPage_ConvertsCorrectly() {
            ContentPage domain = ContentPageFixtures.newContentPage();
            ContentPageJpaEntity entity = mapper.toEntity(domain);
            assertThat(entity.getId()).isNull();
            assertThat(entity.getTitle()).isEqualTo(domain.title());
        }
    }

    @Nested
    @DisplayName("toDomain 메서드 테스트")
    class ToDomainTest {

        @Test
        @DisplayName("활성 상태 Entity를 Domain으로 변환합니다")
        void toDomain_WithActiveEntity_ConvertsCorrectly() {
            ContentPageJpaEntity entity = ContentPageJpaEntityFixtures.activeEntity();
            ContentPage domain = mapper.toDomain(entity);
            assertThat(domain.idValue()).isEqualTo(entity.getId());
            assertThat(domain.title()).isEqualTo(entity.getTitle());
            assertThat(domain.memo()).isEqualTo(entity.getMemo());
            assertThat(domain.imageUrl()).isEqualTo(entity.getImageUrl());
            assertThat(domain.displayPeriod().startDate()).isEqualTo(entity.getDisplayStartAt());
            assertThat(domain.displayPeriod().endDate()).isEqualTo(entity.getDisplayEndAt());
            assertThat(domain.isActive()).isTrue();
            assertThat(domain.isDeleted()).isFalse();
        }

        @Test
        @DisplayName("비활성 상태 Entity를 Domain으로 변환합니다")
        void toDomain_WithInactiveEntity_ConvertsCorrectly() {
            ContentPageJpaEntity entity = ContentPageJpaEntityFixtures.inactiveEntity();
            ContentPage domain = mapper.toDomain(entity);
            assertThat(domain.isActive()).isFalse();
            assertThat(domain.isDeleted()).isFalse();
        }

        @Test
        @DisplayName("삭제된 상태 Entity를 Domain으로 변환합니다")
        void toDomain_WithDeletedEntity_ConvertsCorrectly() {
            ContentPageJpaEntity entity = ContentPageJpaEntityFixtures.deletedEntity();
            ContentPage domain = mapper.toDomain(entity);
            assertThat(domain.isDeleted()).isTrue();
            assertThat(domain.deletionStatus().deletedAt()).isNotNull();
        }

        @Test
        @DisplayName("memo가 null인 Entity를 Domain으로 변환합니다")
        void toDomain_WithNullMemoEntity_ConvertsCorrectly() {
            ContentPageJpaEntity entity = ContentPageJpaEntityFixtures.entityWithoutMemo();
            ContentPage domain = mapper.toDomain(entity);
            assertThat(domain.memo()).isNull();
        }
    }

    @Nested
    @DisplayName("양방향 변환 테스트")
    class BidirectionalConversionTest {

        @Test
        @DisplayName("Domain -> Entity -> Domain 변환 시 데이터가 보존됩니다")
        void roundTrip_DomainToEntityToDomain_PreservesData() {
            ContentPage original = ContentPageFixtures.activeContentPage();
            ContentPageJpaEntity entity = mapper.toEntity(original);
            ContentPage converted = mapper.toDomain(entity);
            assertThat(converted.idValue()).isEqualTo(original.idValue());
            assertThat(converted.title()).isEqualTo(original.title());
            assertThat(converted.memo()).isEqualTo(original.memo());
            assertThat(converted.imageUrl()).isEqualTo(original.imageUrl());
            assertThat(converted.displayPeriod().startDate())
                    .isEqualTo(original.displayPeriod().startDate());
            assertThat(converted.displayPeriod().endDate())
                    .isEqualTo(original.displayPeriod().endDate());
            assertThat(converted.isActive()).isEqualTo(original.isActive());
        }

        @Test
        @DisplayName("Entity -> Domain -> Entity 변환 시 데이터가 보존됩니다")
        void roundTrip_EntityToDomainToEntity_PreservesData() {
            ContentPageJpaEntity original = ContentPageJpaEntityFixtures.activeEntity();
            ContentPage domain = mapper.toDomain(original);
            ContentPageJpaEntity converted = mapper.toEntity(domain);
            assertThat(converted.getId()).isEqualTo(original.getId());
            assertThat(converted.getTitle()).isEqualTo(original.getTitle());
            assertThat(converted.getMemo()).isEqualTo(original.getMemo());
            assertThat(converted.getImageUrl()).isEqualTo(original.getImageUrl());
            assertThat(converted.getDisplayStartAt()).isEqualTo(original.getDisplayStartAt());
            assertThat(converted.getDisplayEndAt()).isEqualTo(original.getDisplayEndAt());
            assertThat(converted.isActive()).isEqualTo(original.isActive());
        }
    }
}
