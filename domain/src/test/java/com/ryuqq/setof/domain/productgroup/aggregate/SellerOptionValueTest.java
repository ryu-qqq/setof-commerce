package com.ryuqq.setof.domain.productgroup.aggregate;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.CommonVoFixtures;
import com.ryuqq.setof.domain.productgroup.ProductGroupFixtures;
import com.ryuqq.setof.domain.productgroup.id.SellerOptionGroupId;
import com.ryuqq.setof.domain.productgroup.id.SellerOptionValueId;
import com.ryuqq.setof.domain.productgroup.vo.OptionValueName;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("SellerOptionValue Entity 테스트")
class SellerOptionValueTest {

    @Nested
    @DisplayName("forNew() - 신규 옵션 값 생성")
    class ForNewTest {

        @Test
        @DisplayName("신규 옵션 값을 생성한다")
        void createNewSellerOptionValue() {
            // given
            SellerOptionGroupId groupId = ProductGroupFixtures.defaultSellerOptionGroupId();
            OptionValueName name = ProductGroupFixtures.defaultOptionValueName();

            // when
            SellerOptionValue value = SellerOptionValue.forNew(groupId, name, 1);

            // then
            assertThat(value).isNotNull();
            assertThat(value.id().isNew()).isTrue();
            assertThat(value.sellerOptionGroupId()).isEqualTo(groupId);
            assertThat(value.optionValueName()).isEqualTo(name);
            assertThat(value.sortOrder()).isEqualTo(1);
            assertThat(value.isDeleted()).isFalse();
        }

        @Test
        @DisplayName("신규 생성 시 삭제되지 않은 활성 상태이다")
        void initialStateIsActive() {
            // when
            SellerOptionValue value = ProductGroupFixtures.newSellerOptionValue();

            // then
            assertThat(value.isDeleted()).isFalse();
        }
    }

    @Nested
    @DisplayName("reconstitute() - 영속성 복원")
    class ReconstituteTest {

        @Test
        @DisplayName("활성 상태의 옵션 값을 복원한다")
        void reconstituteActiveSellerOptionValue() {
            // when
            SellerOptionValue value = ProductGroupFixtures.activeSellerOptionValue();

            // then
            assertThat(value.id()).isEqualTo(ProductGroupFixtures.defaultSellerOptionValueId());
            assertThat(value.idValue())
                    .isEqualTo(ProductGroupFixtures.defaultSellerOptionValueId().value());
            assertThat(value.isDeleted()).isFalse();
        }

        @Test
        @DisplayName("삭제된 상태의 옵션 값을 복원한다")
        void reconstituteDeletedSellerOptionValue() {
            // when
            SellerOptionValue value = ProductGroupFixtures.deletedSellerOptionValue();

            // then
            assertThat(value.isDeleted()).isTrue();
        }
    }

    @Nested
    @DisplayName("updateName() - 옵션 값 이름 수정")
    class UpdateNameTest {

        @Test
        @DisplayName("옵션 값 이름을 수정한다")
        void updateOptionValueName() {
            // given
            SellerOptionValue value = ProductGroupFixtures.activeSellerOptionValue();
            OptionValueName newName = OptionValueName.of("빨강");

            // when
            value.updateName(newName);

            // then
            assertThat(value.optionValueName()).isEqualTo(newName);
            assertThat(value.optionValueNameValue()).isEqualTo("빨강");
        }
    }

    @Nested
    @DisplayName("updateSortOrder() - 정렬 순서 변경")
    class UpdateSortOrderTest {

        @Test
        @DisplayName("정렬 순서를 변경한다")
        void updateSortOrder() {
            // given
            SellerOptionValue value = ProductGroupFixtures.activeSellerOptionValue();

            // when
            value.updateSortOrder(5);

            // then
            assertThat(value.sortOrder()).isEqualTo(5);
        }
    }

    @Nested
    @DisplayName("delete() - Soft Delete 처리")
    class DeleteTest {

        @Test
        @DisplayName("옵션 값을 soft delete 처리한다")
        void deleteSellerOptionValue() {
            // given
            SellerOptionValue value = ProductGroupFixtures.activeSellerOptionValue();
            Instant now = CommonVoFixtures.now();

            // when
            value.delete(now);

            // then
            assertThat(value.isDeleted()).isTrue();
            assertThat(value.deletionStatus().isDeleted()).isTrue();
        }
    }

    @Nested
    @DisplayName("Accessor Methods 테스트")
    class AccessorMethodTest {

        @Test
        @DisplayName("id()는 SellerOptionValueId를 반환한다")
        void returnsId() {
            SellerOptionValue value = ProductGroupFixtures.activeSellerOptionValue();
            assertThat(value.id()).isNotNull();
            assertThat(value.id()).isInstanceOf(SellerOptionValueId.class);
        }

        @Test
        @DisplayName("idValue()는 Long 값을 반환한다")
        void returnsIdValue() {
            SellerOptionValue value = ProductGroupFixtures.activeSellerOptionValue();
            assertThat(value.idValue()).isEqualTo(100L);
        }

        @Test
        @DisplayName("sellerOptionGroupId()는 그룹 ID를 반환한다")
        void returnsSellerOptionGroupId() {
            SellerOptionValue value = ProductGroupFixtures.activeSellerOptionValue();
            assertThat(value.sellerOptionGroupId()).isNotNull();
            assertThat(value.sellerOptionGroupIdValue()).isEqualTo(10L);
        }

        @Test
        @DisplayName("optionValueName()은 OptionValueName을 반환한다")
        void returnsOptionValueName() {
            SellerOptionValue value = ProductGroupFixtures.activeSellerOptionValue();
            assertThat(value.optionValueName()).isNotNull();
            assertThat(value.optionValueNameValue())
                    .isEqualTo(ProductGroupFixtures.DEFAULT_OPTION_VALUE_NAME);
        }

        @Test
        @DisplayName("sortOrder()는 정렬 순서를 반환한다")
        void returnsSortOrder() {
            SellerOptionValue value = ProductGroupFixtures.activeSellerOptionValue();
            assertThat(value.sortOrder()).isEqualTo(1);
        }

        @Test
        @DisplayName("deletionStatus()는 삭제 상태를 반환한다")
        void returnsDeletionStatus() {
            SellerOptionValue value = ProductGroupFixtures.activeSellerOptionValue();
            assertThat(value.deletionStatus()).isNotNull();
            assertThat(value.deletionStatus().isActive()).isTrue();
        }
    }
}
