package com.ryuqq.setof.adapter.out.persistence.displaycomponent.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ryuqq.setof.adapter.out.persistence.displaycomponent.DisplayComponentJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.displaycomponent.entity.DisplayComponentJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.displaycomponent.entity.DisplayTabJpaEntity;
import com.ryuqq.setof.domain.contentpage.aggregate.DisplayComponent;
import com.ryuqq.setof.domain.contentpage.vo.ComponentSpec;
import com.ryuqq.setof.domain.contentpage.vo.ComponentType;
import com.setof.commerce.domain.contentpage.ContentPageFixtures;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * DisplayComponentJpaEntityMapperTest - 디스플레이 컴포넌트 Entity-Domain 매퍼 단위 테스트.
 *
 * <p>PER-MAP-002: toEntity(Domain) + toDomain(Entity) 메서드 제공.
 *
 * <p>PER-MAP-003: 순수 변환 로직만.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@DisplayName("DisplayComponentJpaEntityMapper 단위 테스트")
class DisplayComponentJpaEntityMapperTest {

    private DisplayComponentJpaEntityMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new DisplayComponentJpaEntityMapper(new ObjectMapper());
    }

    @Nested
    @DisplayName("toEntity 메서드 테스트")
    class ToEntityTest {

        @Test
        @DisplayName("PRODUCT 타입 Domain을 Entity로 변환합니다")
        void toEntity_WithProductComponent_ConvertsCorrectly() {
            DisplayComponent domain =
                    ContentPageFixtures.productComponent(
                            1L, com.ryuqq.setof.domain.contentpage.vo.OrderType.NONE, 10);
            DisplayComponentJpaEntity entity = mapper.toEntity(domain);
            assertThat(entity.getId()).isEqualTo(domain.idValue());
            assertThat(entity.getContentPageId()).isEqualTo(domain.contentPageId());
            assertThat(entity.getName()).isEqualTo(domain.name());
            assertThat(entity.getComponentType()).isEqualTo(domain.componentType().name());
            assertThat(entity.getDisplayOrder()).isEqualTo(domain.displayOrder());
            assertThat(entity.isActive()).isTrue();
            assertThat(entity.getDeletedAt()).isNull();
        }

        @Test
        @DisplayName("TEXT 타입 Domain을 Entity로 변환합니다")
        void toEntity_WithTextComponent_ConvertsCorrectly() {
            DisplayComponent domain = ContentPageFixtures.textComponent(2L);
            DisplayComponentJpaEntity entity = mapper.toEntity(domain);
            assertThat(entity.getComponentType()).isEqualTo(ComponentType.TEXT.name());
            assertThat(entity.getSpecData()).isNotNull();
        }

        @Test
        @DisplayName("삭제된 Domain을 Entity로 변환합니다")
        void toEntity_WithDeletedComponent_ConvertsCorrectly() {
            DisplayComponent domain =
                    ContentPageFixtures.productComponent(
                            1L, com.ryuqq.setof.domain.contentpage.vo.OrderType.NONE, 10);
            domain.remove(java.time.Instant.now());
            DisplayComponentJpaEntity entity = mapper.toEntity(domain);
            assertThat(entity.getDeletedAt()).isNotNull();
        }
    }

    @Nested
    @DisplayName("toDomain 메서드 테스트")
    class ToDomainTest {

        @Test
        @DisplayName("PRODUCT 타입 Entity를 Domain으로 변환합니다")
        void toDomain_WithProductEntity_ConvertsCorrectly() {
            DisplayComponentJpaEntity entity =
                    DisplayComponentJpaEntityFixtures.activeProductEntity();
            DisplayComponent domain = mapper.toDomain(entity, List.of());
            assertThat(domain.idValue()).isEqualTo(entity.getId());
            assertThat(domain.contentPageId()).isEqualTo(entity.getContentPageId());
            assertThat(domain.name()).isEqualTo(entity.getName());
            assertThat(domain.componentType()).isEqualTo(ComponentType.PRODUCT);
            assertThat(domain.displayOrder()).isEqualTo(entity.getDisplayOrder());
            assertThat(domain.isActive()).isTrue();
            assertThat(domain.isDeleted()).isFalse();
        }

        @Test
        @DisplayName("TEXT 타입 Entity를 Domain으로 변환합니다")
        void toDomain_WithTextEntity_ConvertsCorrectly() {
            DisplayComponentJpaEntity entity = DisplayComponentJpaEntityFixtures.textEntity();
            DisplayComponent domain = mapper.toDomain(entity, List.of());
            assertThat(domain.componentType()).isEqualTo(ComponentType.TEXT);
            assertThat(domain.componentSpec()).isInstanceOf(ComponentSpec.TextSpec.class);
        }

        @Test
        @DisplayName("TAB 타입 Entity를 탭과 함께 Domain으로 변환합니다")
        void toDomain_WithTabEntityAndTabs_ConvertsCorrectly() {
            DisplayComponentJpaEntity entity = DisplayComponentJpaEntityFixtures.tabEntity();
            DisplayTabJpaEntity tab1 =
                    DisplayComponentJpaEntityFixtures.activeTabEntity(1L, entity.getId(), "탭1", 1);
            DisplayTabJpaEntity tab2 =
                    DisplayComponentJpaEntityFixtures.activeTabEntity(2L, entity.getId(), "탭2", 2);
            DisplayComponent domain = mapper.toDomain(entity, List.of(tab1, tab2));
            assertThat(domain.componentType()).isEqualTo(ComponentType.TAB);
            ComponentSpec.TabSpec tabSpec = (ComponentSpec.TabSpec) domain.componentSpec();
            assertThat(tabSpec.tabs()).hasSize(2);
        }

        @Test
        @DisplayName("비활성 Entity를 Domain으로 변환합니다")
        void toDomain_WithInactiveEntity_ConvertsCorrectly() {
            DisplayComponentJpaEntity entity = DisplayComponentJpaEntityFixtures.inactiveEntity();
            DisplayComponent domain = mapper.toDomain(entity, List.of());
            assertThat(domain.isActive()).isFalse();
            assertThat(domain.isDeleted()).isFalse();
        }

        @Test
        @DisplayName("삭제된 Entity를 Domain으로 변환합니다")
        void toDomain_WithDeletedEntity_ConvertsCorrectly() {
            DisplayComponentJpaEntity entity = DisplayComponentJpaEntityFixtures.deletedEntity();
            DisplayComponent domain = mapper.toDomain(entity, List.of());
            assertThat(domain.isDeleted()).isTrue();
            assertThat(domain.deletionStatus().deletedAt()).isNotNull();
        }
    }

    @Nested
    @DisplayName("양방향 변환 테스트")
    class BidirectionalConversionTest {

        @Test
        @DisplayName("PRODUCT Domain -> Entity -> Domain 변환 시 핵심 데이터가 보존됩니다")
        void roundTrip_ProductDomainToEntityToDomain_PreservesData() {
            DisplayComponent original =
                    ContentPageFixtures.productComponent(
                            1L, com.ryuqq.setof.domain.contentpage.vo.OrderType.NONE, 10);
            DisplayComponentJpaEntity entity = mapper.toEntity(original);
            DisplayComponent converted = mapper.toDomain(entity, List.of());
            assertThat(converted.idValue()).isEqualTo(original.idValue());
            assertThat(converted.contentPageId()).isEqualTo(original.contentPageId());
            assertThat(converted.name()).isEqualTo(original.name());
            assertThat(converted.componentType()).isEqualTo(original.componentType());
            assertThat(converted.displayOrder()).isEqualTo(original.displayOrder());
            assertThat(converted.isActive()).isEqualTo(original.isActive());
        }

        @Test
        @DisplayName("Entity -> Domain -> Entity 변환 시 핵심 데이터가 보존됩니다")
        void roundTrip_EntityToDomainToEntity_PreservesData() {
            DisplayComponentJpaEntity original =
                    DisplayComponentJpaEntityFixtures.activeProductEntity();
            DisplayComponent domain = mapper.toDomain(original, List.of());
            DisplayComponentJpaEntity converted = mapper.toEntity(domain);
            assertThat(converted.getId()).isEqualTo(original.getId());
            assertThat(converted.getContentPageId()).isEqualTo(original.getContentPageId());
            assertThat(converted.getName()).isEqualTo(original.getName());
            assertThat(converted.getComponentType()).isEqualTo(original.getComponentType());
            assertThat(converted.getDisplayOrder()).isEqualTo(original.getDisplayOrder());
            assertThat(converted.isActive()).isEqualTo(original.isActive());
        }
    }
}
