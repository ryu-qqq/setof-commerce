package com.ryuqq.setof.domain.product.aggregate;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.CommonVoFixtures;
import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import com.ryuqq.setof.domain.product.id.ProductId;
import com.ryuqq.setof.domain.product.id.ProductOptionMappingId;
import com.ryuqq.setof.domain.productgroup.id.SellerOptionValueId;
import com.setof.commerce.domain.product.ProductFixtures;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ProductOptionMapping Entity 테스트")
class ProductOptionMappingTest {

    @Nested
    @DisplayName("forNew() - 신규 매핑 생성")
    class ForNewTest {

        @Test
        @DisplayName("신규 매핑을 생성하면 isNew()가 true이다")
        void createNewMappingIsNew() {
            // given
            ProductId productId = ProductId.forNew();
            SellerOptionValueId sellerOptionValueId = SellerOptionValueId.of(1L);

            // when
            ProductOptionMapping mapping =
                    ProductOptionMapping.forNew(productId, sellerOptionValueId);

            // then
            assertThat(mapping.id().isNew()).isTrue();
        }

        @Test
        @DisplayName("신규 매핑은 활성 상태이다")
        void createNewMappingIsActive() {
            // when
            ProductOptionMapping mapping = ProductFixtures.newProductOptionMapping();

            // then
            assertThat(mapping.isDeleted()).isFalse();
            assertThat(mapping.deletionStatus().isActive()).isTrue();
        }

        @Test
        @DisplayName("신규 매핑은 ProductId와 SellerOptionValueId를 가진다")
        void createNewMappingHasProductIdAndSellerOptionValueId() {
            // given
            ProductId productId = ProductId.forNew();
            SellerOptionValueId sellerOptionValueId = SellerOptionValueId.of(5L);

            // when
            ProductOptionMapping mapping =
                    ProductOptionMapping.forNew(productId, sellerOptionValueId);

            // then
            assertThat(mapping.productId()).isEqualTo(productId);
            assertThat(mapping.sellerOptionValueId()).isEqualTo(sellerOptionValueId);
            assertThat(mapping.sellerOptionValueIdValue()).isEqualTo(5L);
        }
    }

    @Nested
    @DisplayName("reconstitute() - 영속성 복원")
    class ReconstituteTest {

        @Test
        @DisplayName("활성 상태의 매핑을 복원한다")
        void reconstituteActiveMappingSuccessfully() {
            // when
            ProductOptionMapping mapping = ProductFixtures.activeProductOptionMapping();

            // then
            assertThat(mapping.id().value()).isEqualTo(1L);
            assertThat(mapping.productId().value()).isEqualTo(1L);
            assertThat(mapping.sellerOptionValueId().value()).isEqualTo(1L);
            assertThat(mapping.isDeleted()).isFalse();
        }

        @Test
        @DisplayName("삭제된 상태의 매핑을 복원한다")
        void reconstituteDeletedMappingSuccessfully() {
            // when
            ProductOptionMapping mapping = ProductFixtures.deletedProductOptionMapping();

            // then
            assertThat(mapping.isDeleted()).isTrue();
            assertThat(mapping.deletionStatus().isDeleted()).isTrue();
        }

        @Test
        @DisplayName("모든 필드를 지정하여 매핑을 복원한다")
        void reconstituteWithAllFields() {
            // given
            ProductOptionMappingId id = ProductOptionMappingId.of(10L);
            ProductId productId = ProductId.of(20L);
            SellerOptionValueId sellerOptionValueId = SellerOptionValueId.of(30L);
            DeletionStatus deletionStatus = DeletionStatus.active();

            // when
            ProductOptionMapping mapping =
                    ProductOptionMapping.reconstitute(
                            id, productId, sellerOptionValueId, deletionStatus);

            // then
            assertThat(mapping.id()).isEqualTo(id);
            assertThat(mapping.productId()).isEqualTo(productId);
            assertThat(mapping.sellerOptionValueId()).isEqualTo(sellerOptionValueId);
            assertThat(mapping.deletionStatus()).isEqualTo(deletionStatus);
        }
    }

    @Nested
    @DisplayName("delete() - 소프트 삭제")
    class DeleteTest {

        @Test
        @DisplayName("매핑을 소프트 삭제하면 isDeleted()가 true이다")
        void deleteMappingSuccessfully() {
            // given
            ProductOptionMapping mapping = ProductFixtures.activeProductOptionMapping();
            Instant now = CommonVoFixtures.now();

            // when
            mapping.delete(now);

            // then
            assertThat(mapping.isDeleted()).isTrue();
        }

        @Test
        @DisplayName("삭제 후 deletionStatus에 삭제 시각이 기록된다")
        void deleteRecordsDeletionTime() {
            // given
            ProductOptionMapping mapping = ProductFixtures.activeProductOptionMapping();
            Instant now = CommonVoFixtures.now();

            // when
            mapping.delete(now);

            // then
            assertThat(mapping.deletionStatus().deletedAt()).isEqualTo(now);
        }
    }

    @Nested
    @DisplayName("isDeleted() - 삭제 여부 확인")
    class IsDeletedTest {

        @Test
        @DisplayName("활성 매핑은 isDeleted()가 false이다")
        void activeIsDeletedFalse() {
            // when
            ProductOptionMapping mapping = ProductFixtures.activeProductOptionMapping();

            // then
            assertThat(mapping.isDeleted()).isFalse();
        }

        @Test
        @DisplayName("삭제된 매핑은 isDeleted()가 true이다")
        void deletedIsDeletedTrue() {
            // when
            ProductOptionMapping mapping = ProductFixtures.deletedProductOptionMapping();

            // then
            assertThat(mapping.isDeleted()).isTrue();
        }
    }

    @Nested
    @DisplayName("Accessor 메서드 테스트")
    class AccessorTest {

        @Test
        @DisplayName("idValue()는 ID의 Long 값을 반환한다")
        void idValueReturnsLong() {
            // when
            ProductOptionMapping mapping = ProductFixtures.activeProductOptionMapping();

            // then
            assertThat(mapping.idValue()).isEqualTo(1L);
        }

        @Test
        @DisplayName("productIdValue()는 ProductId의 Long 값을 반환한다")
        void productIdValueReturnsLong() {
            // when
            ProductOptionMapping mapping = ProductFixtures.activeProductOptionMapping();

            // then
            assertThat(mapping.productIdValue()).isEqualTo(1L);
        }

        @Test
        @DisplayName("sellerOptionValueIdValue()는 SellerOptionValueId의 Long 값을 반환한다")
        void sellerOptionValueIdValueReturnsLong() {
            // when
            ProductOptionMapping mapping = ProductFixtures.activeProductOptionMapping();

            // then
            assertThat(mapping.sellerOptionValueIdValue()).isEqualTo(1L);
        }

        @Test
        @DisplayName("deletionStatus()는 DeletionStatus를 반환한다")
        void deletionStatusReturns() {
            // when
            ProductOptionMapping mapping = ProductFixtures.activeProductOptionMapping();

            // then
            assertThat(mapping.deletionStatus()).isNotNull();
            assertThat(mapping.deletionStatus()).isEqualTo(DeletionStatus.active());
        }
    }
}
