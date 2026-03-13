package com.ryuqq.setof.adapter.out.persistence.navigation.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.out.persistence.navigation.NavigationMenuJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.navigation.entity.NavigationMenuJpaEntity;
import com.ryuqq.setof.domain.navigation.aggregate.NavigationMenu;
import com.setof.commerce.domain.navigation.NavigationFixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * NavigationMenuJpaEntityMapperTest - 네비게이션 메뉴 Entity-Domain 매퍼 단위 테스트.
 *
 * <p>PER-MAP-002: toEntity(Domain) + toDomain(Entity) 메서드 제공.
 *
 * <p>PER-MAP-003: 순수 변환 로직만.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@DisplayName("NavigationMenuJpaEntityMapper 단위 테스트")
class NavigationMenuJpaEntityMapperTest {

    private NavigationMenuJpaEntityMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new NavigationMenuJpaEntityMapper();
    }

    @Nested
    @DisplayName("toEntity 메서드 테스트")
    class ToEntityTest {

        @Test
        @DisplayName("활성 상태 Domain을 Entity로 변환합니다")
        void toEntity_WithActiveNavigationMenu_ConvertsCorrectly() {
            NavigationMenu domain = NavigationFixtures.activeNavigationMenu();
            NavigationMenuJpaEntity entity = mapper.toEntity(domain);
            assertThat(entity.getId()).isEqualTo(domain.idValue());
            assertThat(entity.getTitle()).isEqualTo(domain.title());
            assertThat(entity.getLinkUrl()).isEqualTo(domain.linkUrl());
            assertThat(entity.getDisplayOrder()).isEqualTo(domain.displayOrder());
            assertThat(entity.getDisplayStartAt()).isEqualTo(domain.displayPeriod().startDate());
            assertThat(entity.getDisplayEndAt()).isEqualTo(domain.displayPeriod().endDate());
            assertThat(entity.isActive()).isTrue();
            assertThat(entity.getDeletedAt()).isNull();
        }

        @Test
        @DisplayName("비활성 상태 Domain을 Entity로 변환합니다")
        void toEntity_WithInactiveNavigationMenu_ConvertsCorrectly() {
            NavigationMenu domain = NavigationFixtures.inactiveNavigationMenu();
            NavigationMenuJpaEntity entity = mapper.toEntity(domain);
            assertThat(entity.isActive()).isFalse();
            assertThat(entity.getDeletedAt()).isNull();
        }

        @Test
        @DisplayName("삭제된 상태 Domain을 Entity로 변환합니다")
        void toEntity_WithDeletedNavigationMenu_ConvertsCorrectly() {
            NavigationMenu domain = NavigationFixtures.deletedNavigationMenu();
            NavigationMenuJpaEntity entity = mapper.toEntity(domain);
            assertThat(entity.getDeletedAt()).isNotNull();
        }

        @Test
        @DisplayName("새로운 Domain을 Entity로 변환합니다")
        void toEntity_WithNewNavigationMenu_ConvertsCorrectly() {
            NavigationMenu domain = NavigationFixtures.newNavigationMenu();
            NavigationMenuJpaEntity entity = mapper.toEntity(domain);
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
            NavigationMenuJpaEntity entity = NavigationMenuJpaEntityFixtures.activeEntity();
            NavigationMenu domain = mapper.toDomain(entity);
            assertThat(domain.idValue()).isEqualTo(entity.getId());
            assertThat(domain.title()).isEqualTo(entity.getTitle());
            assertThat(domain.linkUrl()).isEqualTo(entity.getLinkUrl());
            assertThat(domain.displayOrder()).isEqualTo(entity.getDisplayOrder());
            assertThat(domain.displayPeriod().startDate()).isEqualTo(entity.getDisplayStartAt());
            assertThat(domain.displayPeriod().endDate()).isEqualTo(entity.getDisplayEndAt());
            assertThat(domain.isActive()).isTrue();
            assertThat(domain.isDeleted()).isFalse();
        }

        @Test
        @DisplayName("비활성 상태 Entity를 Domain으로 변환합니다")
        void toDomain_WithInactiveEntity_ConvertsCorrectly() {
            NavigationMenuJpaEntity entity = NavigationMenuJpaEntityFixtures.inactiveEntity();
            NavigationMenu domain = mapper.toDomain(entity);
            assertThat(domain.isActive()).isFalse();
            assertThat(domain.isDeleted()).isFalse();
        }

        @Test
        @DisplayName("삭제된 상태 Entity를 Domain으로 변환합니다")
        void toDomain_WithDeletedEntity_ConvertsCorrectly() {
            NavigationMenuJpaEntity entity = NavigationMenuJpaEntityFixtures.deletedEntity();
            NavigationMenu domain = mapper.toDomain(entity);
            assertThat(domain.isDeleted()).isTrue();
            assertThat(domain.deletionStatus().deletedAt()).isNotNull();
        }
    }

    @Nested
    @DisplayName("양방향 변환 테스트")
    class BidirectionalConversionTest {

        @Test
        @DisplayName("Domain -> Entity -> Domain 변환 시 데이터가 보존됩니다")
        void roundTrip_DomainToEntityToDomain_PreservesData() {
            NavigationMenu original = NavigationFixtures.activeNavigationMenu();
            NavigationMenuJpaEntity entity = mapper.toEntity(original);
            NavigationMenu converted = mapper.toDomain(entity);
            assertThat(converted.idValue()).isEqualTo(original.idValue());
            assertThat(converted.title()).isEqualTo(original.title());
            assertThat(converted.linkUrl()).isEqualTo(original.linkUrl());
            assertThat(converted.displayOrder()).isEqualTo(original.displayOrder());
            assertThat(converted.displayPeriod().startDate())
                    .isEqualTo(original.displayPeriod().startDate());
            assertThat(converted.displayPeriod().endDate())
                    .isEqualTo(original.displayPeriod().endDate());
            assertThat(converted.isActive()).isEqualTo(original.isActive());
        }

        @Test
        @DisplayName("Entity -> Domain -> Entity 변환 시 데이터가 보존됩니다")
        void roundTrip_EntityToDomainToEntity_PreservesData() {
            NavigationMenuJpaEntity original = NavigationMenuJpaEntityFixtures.activeEntity();
            NavigationMenu domain = mapper.toDomain(original);
            NavigationMenuJpaEntity converted = mapper.toEntity(domain);
            assertThat(converted.getId()).isEqualTo(original.getId());
            assertThat(converted.getTitle()).isEqualTo(original.getTitle());
            assertThat(converted.getLinkUrl()).isEqualTo(original.getLinkUrl());
            assertThat(converted.getDisplayOrder()).isEqualTo(original.getDisplayOrder());
            assertThat(converted.getDisplayStartAt()).isEqualTo(original.getDisplayStartAt());
            assertThat(converted.getDisplayEndAt()).isEqualTo(original.getDisplayEndAt());
            assertThat(converted.isActive()).isEqualTo(original.isActive());
        }
    }
}
