package com.ryuqq.setof.adapter.out.persistence.productnotice.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.out.persistence.productnotice.ProductNoticeJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.productnotice.entity.ProductNoticeEntryJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.productnotice.entity.ProductNoticeJpaEntity;
import com.ryuqq.setof.domain.productnotice.aggregate.ProductNotice;
import com.ryuqq.setof.domain.productnotice.aggregate.ProductNoticeEntry;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * ProductNoticeJpaEntityMapperTest - 상품고시 Entity-Domain 매퍼 단위 테스트.
 *
 * <p>PER-MAP-002: toEntity(Domain) + toDomain(Entity) 메서드 제공.
 *
 * <p>PER-MAP-003: 순수 변환 로직만.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Tag("unit")
@DisplayName("ProductNoticeJpaEntityMapper 단위 테스트")
class ProductNoticeJpaEntityMapperTest {

    private ProductNoticeJpaEntityMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ProductNoticeJpaEntityMapper();
    }

    // ========================================================================
    // 1. toEntity 테스트
    // ========================================================================

    @Nested
    @DisplayName("toEntity 메서드 테스트")
    class ToEntityTest {

        @Test
        @DisplayName("활성 상태 Domain을 Entity로 변환합니다")
        void toEntity_WithActiveNotice_ConvertsCorrectly() {
            // given
            ProductNotice domain =
                    com.setof.commerce.domain.productnotice.ProductNoticeFixtures.activeNotice();

            // when
            ProductNoticeJpaEntity entity = mapper.toEntity(domain);

            // then
            assertThat(entity.getId()).isEqualTo(domain.idValue());
            assertThat(entity.getProductGroupId()).isEqualTo(domain.productGroupIdValue());
            assertThat(entity.getCreatedAt()).isEqualTo(domain.createdAt());
            assertThat(entity.getUpdatedAt()).isEqualTo(domain.updatedAt());
        }

        @Test
        @DisplayName("신규 Domain을 Entity로 변환합니다 (ID가 null)")
        void toEntity_WithNewNotice_IdIsNull() {
            // given
            ProductNotice domain =
                    com.setof.commerce.domain.productnotice.ProductNoticeFixtures.newNotice();

            // when
            ProductNoticeJpaEntity entity = mapper.toEntity(domain);

            // then
            assertThat(entity.getId()).isNull();
            assertThat(entity.getProductGroupId()).isEqualTo(domain.productGroupIdValue());
        }
    }

    // ========================================================================
    // 2. toEntryEntity 테스트
    // ========================================================================

    @Nested
    @DisplayName("toEntryEntity 메서드 테스트")
    class ToEntryEntityTest {

        @Test
        @DisplayName("ProductNoticeEntry Domain을 Entity로 변환합니다")
        void toEntryEntity_WithValidEntry_ConvertsCorrectly() {
            // given
            ProductNoticeEntry entry =
                    com.setof.commerce.domain.productnotice.ProductNoticeFixtures
                            .defaultNoticeEntry();

            // when
            ProductNoticeEntryJpaEntity entity = mapper.toEntryEntity(entry);

            // then
            assertThat(entity.getFieldName()).isEqualTo(entry.fieldName());
            assertThat(entity.getFieldValue()).isEqualTo(entry.fieldValueText());
            assertThat(entity.getSortOrder()).isEqualTo(entry.sortOrder());
            assertThat(entity.getCreatedAt()).isNotNull();
            assertThat(entity.getUpdatedAt()).isNotNull();
            assertThat(entity.getDeletedAt()).isNull();
        }
    }

    // ========================================================================
    // 3. toDomain 테스트
    // ========================================================================

    @Nested
    @DisplayName("toDomain 메서드 테스트")
    class ToDomainTest {

        @Test
        @DisplayName("기본 상품고시 Entity를 Domain으로 변환합니다")
        void toDomain_WithActiveEntity_ConvertsCorrectly() {
            // given
            ProductNoticeJpaEntity entity = ProductNoticeJpaEntityFixtures.activeEntity();
            List<ProductNoticeEntryJpaEntity> entryEntities =
                    ProductNoticeJpaEntityFixtures.defaultEntryEntities();

            // when
            ProductNotice domain = mapper.toDomain(entity, entryEntities);

            // then
            assertThat(domain.idValue()).isEqualTo(entity.getId());
            assertThat(domain.productGroupIdValue()).isEqualTo(entity.getProductGroupId());
            assertThat(domain.entries()).hasSize(4);
        }

        @Test
        @DisplayName("항목이 없는 상품고시 Entity를 Domain으로 변환합니다")
        void toDomain_WithNoEntries_ConvertsCorrectly() {
            // given
            ProductNoticeJpaEntity entity = ProductNoticeJpaEntityFixtures.activeEntity();
            List<ProductNoticeEntryJpaEntity> entryEntities =
                    ProductNoticeJpaEntityFixtures.emptyEntryEntities();

            // when
            ProductNotice domain = mapper.toDomain(entity, entryEntities);

            // then
            assertThat(domain.entries()).isEmpty();
        }
    }

    // ========================================================================
    // 4. toEntryDomain 테스트
    // ========================================================================

    @Nested
    @DisplayName("toEntryDomain 메서드 테스트")
    class ToEntryDomainTest {

        @Test
        @DisplayName("ProductNoticeEntryJpaEntity를 Domain으로 올바르게 변환합니다")
        void toDomain_EntryFieldsAreMappedCorrectly() {
            // given
            ProductNoticeJpaEntity entity = ProductNoticeJpaEntityFixtures.activeEntity();
            List<ProductNoticeEntryJpaEntity> entryEntities =
                    ProductNoticeJpaEntityFixtures.defaultEntryEntities();

            // when
            ProductNotice domain = mapper.toDomain(entity, entryEntities);

            // then - 첫 번째 항목의 필드 검증
            ProductNoticeEntry firstEntry = domain.entries().get(0);
            assertThat(firstEntry.fieldName()).isEqualTo("소재");
            assertThat(firstEntry.fieldValueText()).isEqualTo("면 100%");
            assertThat(firstEntry.sortOrder()).isEqualTo(1);
        }
    }

    // ========================================================================
    // 5. 양방향 변환 테스트
    // ========================================================================

    @Nested
    @DisplayName("양방향 변환 테스트")
    class BidirectionalConversionTest {

        @Test
        @DisplayName("Entity -> Domain -> Entity 변환 시 데이터가 보존됩니다")
        void roundTrip_EntityToDomainToEntity_PreservesData() {
            // given
            ProductNoticeJpaEntity original = ProductNoticeJpaEntityFixtures.activeEntity();

            // when
            ProductNotice domain = mapper.toDomain(original, List.of());
            ProductNoticeJpaEntity converted = mapper.toEntity(domain);

            // then
            assertThat(converted.getId()).isEqualTo(original.getId());
            assertThat(converted.getProductGroupId()).isEqualTo(original.getProductGroupId());
        }
    }
}
