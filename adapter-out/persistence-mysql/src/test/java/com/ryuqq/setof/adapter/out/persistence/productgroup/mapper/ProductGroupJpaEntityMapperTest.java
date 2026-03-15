package com.ryuqq.setof.adapter.out.persistence.productgroup.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.out.persistence.productgroup.ProductGroupJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.productgroup.entity.ProductGroupJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.productgroup.entity.SellerOptionGroupJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.productgroup.entity.SellerOptionValueJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.productgroupimage.ProductGroupImageJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.productgroupimage.entity.ProductGroupImageJpaEntity;
import com.ryuqq.setof.domain.productgroup.ProductGroupFixtures;
import com.ryuqq.setof.domain.productgroup.aggregate.ProductGroup;
import com.ryuqq.setof.domain.productgroup.aggregate.SellerOptionGroup;
import com.ryuqq.setof.domain.productgroup.aggregate.SellerOptionValue;
import com.ryuqq.setof.domain.productgroup.vo.ProductGroupStatus;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * ProductGroupJpaEntityMapperTest - 상품그룹 Entity-Domain 매퍼 단위 테스트.
 *
 * <p>PER-MAP-002: toEntity(Domain) + toDomain(Entity) 메서드 제공.
 *
 * <p>PER-MAP-003: 순수 변환 로직만.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Tag("unit")
@DisplayName("ProductGroupJpaEntityMapper 단위 테스트")
class ProductGroupJpaEntityMapperTest {

    private ProductGroupJpaEntityMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ProductGroupJpaEntityMapper();
    }

    // ========================================================================
    // 1. toEntity 테스트
    // ========================================================================

    @Nested
    @DisplayName("toEntity 메서드 테스트")
    class ToEntityTest {

        @Test
        @DisplayName("ACTIVE 상태 Domain을 Entity로 변환합니다")
        void toEntity_WithActiveProductGroup_ConvertsCorrectly() {
            // given
            ProductGroup domain = ProductGroupFixtures.activeProductGroup();

            // when
            ProductGroupJpaEntity entity = mapper.toEntity(domain);

            // then
            assertThat(entity.getId()).isEqualTo(domain.idValue());
            assertThat(entity.getSellerId()).isEqualTo(domain.sellerIdValue());
            assertThat(entity.getBrandId()).isEqualTo(domain.brandIdValue());
            assertThat(entity.getCategoryId()).isEqualTo(domain.categoryIdValue());
            assertThat(entity.getShippingPolicyId()).isEqualTo(domain.shippingPolicyIdValue());
            assertThat(entity.getRefundPolicyId()).isEqualTo(domain.refundPolicyIdValue());
            assertThat(entity.getProductGroupName()).isEqualTo(domain.productGroupNameValue());
            assertThat(entity.getOptionType()).isEqualTo(domain.optionType().name());
            assertThat(entity.getRegularPrice()).isEqualTo(domain.regularPriceValue());
            assertThat(entity.getCurrentPrice()).isEqualTo(domain.currentPriceValue());
            assertThat(entity.getStatus()).isEqualTo(domain.status().name());
            assertThat(entity.getDeletedAt()).isNull();
        }

        @Test
        @DisplayName("DELETED 상태 Domain을 Entity로 변환합니다")
        void toEntity_WithDeletedProductGroup_ConvertsCorrectly() {
            // given
            ProductGroup domain = ProductGroupFixtures.deletedProductGroup();

            // when
            ProductGroupJpaEntity entity = mapper.toEntity(domain);

            // then
            assertThat(entity.getStatus()).isEqualTo(ProductGroupStatus.DELETED.name());
        }

        @Test
        @DisplayName("신규 Domain을 Entity로 변환합니다 (deletedAt null)")
        void toEntity_WithNewProductGroup_DeletedAtIsNull() {
            // given
            ProductGroup domain = ProductGroupFixtures.newProductGroup();

            // when
            ProductGroupJpaEntity entity = mapper.toEntity(domain);

            // then
            assertThat(entity.getId()).isNull();
            assertThat(entity.getDeletedAt()).isNull();
        }
    }

    // ========================================================================
    // 2. toOptionGroupEntity 테스트
    // ========================================================================

    @Nested
    @DisplayName("toOptionGroupEntity 메서드 테스트")
    class ToOptionGroupEntityTest {

        @Test
        @DisplayName("활성 SellerOptionGroup Domain을 Entity로 변환합니다")
        void toOptionGroupEntity_WithActiveGroup_ConvertsCorrectly() {
            // given
            SellerOptionGroup group = ProductGroupFixtures.activeSellerOptionGroup();

            // when
            SellerOptionGroupJpaEntity entity = mapper.toOptionGroupEntity(group);

            // then
            assertThat(entity.getId()).isEqualTo(group.idValue());
            assertThat(entity.getProductGroupId()).isEqualTo(group.productGroupIdValue());
            assertThat(entity.getOptionGroupName()).isEqualTo(group.optionGroupNameValue());
            assertThat(entity.getSortOrder()).isEqualTo(group.sortOrder());
            assertThat(entity.isDeleted()).isFalse();
            assertThat(entity.getDeletedAt()).isNull();
        }

        @Test
        @DisplayName("삭제된 SellerOptionGroup Domain을 Entity로 변환합니다")
        void toOptionGroupEntity_WithDeletedGroup_ConvertsCorrectly() {
            // given
            SellerOptionGroup group = ProductGroupFixtures.deletedSellerOptionGroup();

            // when
            SellerOptionGroupJpaEntity entity = mapper.toOptionGroupEntity(group);

            // then
            assertThat(entity.isDeleted()).isTrue();
            assertThat(entity.getDeletedAt()).isNotNull();
        }
    }

    // ========================================================================
    // 3. toOptionValueEntity 테스트
    // ========================================================================

    @Nested
    @DisplayName("toOptionValueEntity 메서드 테스트")
    class ToOptionValueEntityTest {

        @Test
        @DisplayName("활성 SellerOptionValue Domain을 Entity로 변환합니다")
        void toOptionValueEntity_WithActiveValue_ConvertsCorrectly() {
            // given
            SellerOptionValue value = ProductGroupFixtures.activeSellerOptionValue();

            // when
            SellerOptionValueJpaEntity entity = mapper.toOptionValueEntity(value);

            // then
            assertThat(entity.getId()).isEqualTo(value.idValue());
            assertThat(entity.getSellerOptionGroupId()).isEqualTo(value.sellerOptionGroupIdValue());
            assertThat(entity.getOptionValueName()).isEqualTo(value.optionValueNameValue());
            assertThat(entity.getSortOrder()).isEqualTo(value.sortOrder());
            assertThat(entity.isDeleted()).isFalse();
            assertThat(entity.getDeletedAt()).isNull();
        }

        @Test
        @DisplayName("override groupId를 지정하여 Entity로 변환합니다")
        void toOptionValueEntity_WithOverrideGroupId_ConvertsCorrectly() {
            // given
            SellerOptionValue value = ProductGroupFixtures.activeSellerOptionValue();
            Long overrideGroupId = 999L;

            // when
            SellerOptionValueJpaEntity entity = mapper.toOptionValueEntity(value, overrideGroupId);

            // then
            assertThat(entity.getSellerOptionGroupId()).isEqualTo(overrideGroupId);
            assertThat(entity.getOptionValueName()).isEqualTo(value.optionValueNameValue());
        }

        @Test
        @DisplayName("삭제된 SellerOptionValue Domain을 Entity로 변환합니다")
        void toOptionValueEntity_WithDeletedValue_ConvertsCorrectly() {
            // given
            SellerOptionValue value = ProductGroupFixtures.deletedSellerOptionValue();

            // when
            SellerOptionValueJpaEntity entity = mapper.toOptionValueEntity(value);

            // then
            assertThat(entity.isDeleted()).isTrue();
            assertThat(entity.getDeletedAt()).isNotNull();
        }
    }

    // ========================================================================
    // 4. toDomain 테스트
    // ========================================================================

    @Nested
    @DisplayName("toDomain 메서드 테스트")
    class ToDomainTest {

        @Test
        @DisplayName("ACTIVE 상태 Entity를 Domain으로 변환합니다")
        void toDomain_WithActiveEntity_ConvertsCorrectly() {
            // given
            ProductGroupJpaEntity entity = ProductGroupJpaEntityFixtures.activeEntity();
            List<ProductGroupImageJpaEntity> images =
                    ProductGroupImageJpaEntityFixtures.defaultEntities();
            List<SellerOptionGroupJpaEntity> groups =
                    ProductGroupJpaEntityFixtures.defaultOptionGroupEntities();
            List<SellerOptionValueJpaEntity> values =
                    ProductGroupJpaEntityFixtures.defaultOptionValueEntities();

            // when
            ProductGroup domain = mapper.toDomain(entity, images, groups, values);

            // then
            assertThat(domain.idValue()).isEqualTo(entity.getId());
            assertThat(domain.sellerIdValue()).isEqualTo(entity.getSellerId());
            assertThat(domain.brandIdValue()).isEqualTo(entity.getBrandId());
            assertThat(domain.categoryIdValue()).isEqualTo(entity.getCategoryId());
            assertThat(domain.shippingPolicyIdValue()).isEqualTo(entity.getShippingPolicyId());
            assertThat(domain.refundPolicyIdValue()).isEqualTo(entity.getRefundPolicyId());
            assertThat(domain.productGroupNameValue()).isEqualTo(entity.getProductGroupName());
            assertThat(domain.optionType().name()).isEqualTo(entity.getOptionType());
            assertThat(domain.regularPriceValue()).isEqualTo(entity.getRegularPrice());
            assertThat(domain.currentPriceValue()).isEqualTo(entity.getCurrentPrice());
            assertThat(domain.status().name()).isEqualTo(entity.getStatus());
        }

        @Test
        @DisplayName("이미지와 옵션 그룹이 없는 Entity를 Domain으로 변환합니다")
        void toDomain_WithNoImagesAndGroups_ConvertsCorrectly() {
            // given
            ProductGroupJpaEntity entity = ProductGroupJpaEntityFixtures.activeEntity();

            // when
            ProductGroup domain = mapper.toDomain(entity, List.of(), List.of(), List.of());

            // then
            assertThat(domain.images()).isEmpty();
            assertThat(domain.sellerOptionGroups()).isEmpty();
        }

        @Test
        @DisplayName("옵션 값이 그룹에 올바르게 매핑됩니다")
        void toDomain_WithOptionGroupsAndValues_MapsCorrectly() {
            // given
            ProductGroupJpaEntity entity = ProductGroupJpaEntityFixtures.activeEntity();
            List<SellerOptionGroupJpaEntity> groups =
                    ProductGroupJpaEntityFixtures.defaultOptionGroupEntities();
            List<SellerOptionValueJpaEntity> values =
                    ProductGroupJpaEntityFixtures.defaultOptionValueEntities();

            // when
            ProductGroup domain = mapper.toDomain(entity, List.of(), groups, values);

            // then
            assertThat(domain.sellerOptionGroups()).hasSize(1);
            assertThat(domain.sellerOptionGroups().get(0).optionValues()).hasSize(1);
        }
    }

    // ========================================================================
    // 5. toOptionGroupDomain / toOptionValueDomain 테스트
    // ========================================================================

    @Nested
    @DisplayName("toOptionGroupDomain 메서드 테스트")
    class ToOptionGroupDomainTest {

        @Test
        @DisplayName("활성 SellerOptionGroupJpaEntity를 Domain으로 변환합니다")
        void toOptionGroupDomain_WithActiveEntity_ConvertsCorrectly() {
            // given
            SellerOptionGroupJpaEntity entity =
                    ProductGroupJpaEntityFixtures.activeOptionGroupEntity();
            List<SellerOptionValue> values = List.of();

            // when
            SellerOptionGroup domain = mapper.toOptionGroupDomain(entity, values);

            // then
            assertThat(domain.idValue()).isEqualTo(entity.getId());
            assertThat(domain.productGroupIdValue()).isEqualTo(entity.getProductGroupId());
            assertThat(domain.optionGroupNameValue()).isEqualTo(entity.getOptionGroupName());
            assertThat(domain.sortOrder()).isEqualTo(entity.getSortOrder());
            assertThat(domain.deletionStatus().isDeleted()).isFalse();
        }

        @Test
        @DisplayName("삭제된 SellerOptionGroupJpaEntity를 Domain으로 변환합니다")
        void toOptionGroupDomain_WithDeletedEntity_ConvertsCorrectly() {
            // given
            SellerOptionGroupJpaEntity entity =
                    ProductGroupJpaEntityFixtures.deletedOptionGroupEntity();
            List<SellerOptionValue> values = List.of();

            // when
            SellerOptionGroup domain = mapper.toOptionGroupDomain(entity, values);

            // then
            assertThat(domain.deletionStatus().isDeleted()).isTrue();
        }
    }

    @Nested
    @DisplayName("toOptionValueDomain 메서드 테스트")
    class ToOptionValueDomainTest {

        @Test
        @DisplayName("활성 SellerOptionValueJpaEntity를 Domain으로 변환합니다")
        void toOptionValueDomain_WithActiveEntity_ConvertsCorrectly() {
            // given
            SellerOptionValueJpaEntity entity =
                    ProductGroupJpaEntityFixtures.activeOptionValueEntity();

            // when
            SellerOptionValue domain = mapper.toOptionValueDomain(entity);

            // then
            assertThat(domain.idValue()).isEqualTo(entity.getId());
            assertThat(domain.sellerOptionGroupIdValue())
                    .isEqualTo(entity.getSellerOptionGroupId());
            assertThat(domain.optionValueNameValue()).isEqualTo(entity.getOptionValueName());
            assertThat(domain.sortOrder()).isEqualTo(entity.getSortOrder());
            assertThat(domain.deletionStatus().isDeleted()).isFalse();
        }

        @Test
        @DisplayName("삭제된 SellerOptionValueJpaEntity를 Domain으로 변환합니다")
        void toOptionValueDomain_WithDeletedEntity_ConvertsCorrectly() {
            // given
            SellerOptionValueJpaEntity entity =
                    ProductGroupJpaEntityFixtures.deletedOptionValueEntity();

            // when
            SellerOptionValue domain = mapper.toOptionValueDomain(entity);

            // then
            assertThat(domain.deletionStatus().isDeleted()).isTrue();
            assertThat(domain.deletionStatus().deletedAt()).isNotNull();
        }
    }

    // ========================================================================
    // 6. 양방향 변환 테스트
    // ========================================================================

    @Nested
    @DisplayName("양방향 변환 테스트")
    class BidirectionalConversionTest {

        @Test
        @DisplayName("Entity -> Domain -> Entity 변환 시 데이터가 보존됩니다")
        void roundTrip_EntityToDomainToEntity_PreservesData() {
            // given
            ProductGroupJpaEntity original = ProductGroupJpaEntityFixtures.activeEntity();

            // when
            ProductGroup domain = mapper.toDomain(original, List.of(), List.of(), List.of());
            ProductGroupJpaEntity converted = mapper.toEntity(domain);

            // then
            assertThat(converted.getId()).isEqualTo(original.getId());
            assertThat(converted.getSellerId()).isEqualTo(original.getSellerId());
            assertThat(converted.getBrandId()).isEqualTo(original.getBrandId());
            assertThat(converted.getCategoryId()).isEqualTo(original.getCategoryId());
            assertThat(converted.getProductGroupName()).isEqualTo(original.getProductGroupName());
            assertThat(converted.getOptionType()).isEqualTo(original.getOptionType());
            assertThat(converted.getRegularPrice()).isEqualTo(original.getRegularPrice());
            assertThat(converted.getCurrentPrice()).isEqualTo(original.getCurrentPrice());
            assertThat(converted.getStatus()).isEqualTo(original.getStatus());
        }
    }
}
