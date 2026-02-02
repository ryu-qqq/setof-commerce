package com.ryuqq.setof.domain.brand.aggregate;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.brand.BrandFixtures;
import com.ryuqq.setof.domain.common.CommonVoFixtures;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("Brand Aggregate 테스트")
class BrandTest {

    @Nested
    @DisplayName("forNew() - 신규 브랜드 생성")
    class ForNewTest {

        @Test
        @DisplayName("신규 브랜드를 생성한다")
        void createNewBrand() {
            // when
            var brand = BrandFixtures.newBrand();

            // then
            assertThat(brand.isNew()).isTrue();
            assertThat(brand.brandNameValue()).isEqualTo(BrandFixtures.DEFAULT_BRAND_NAME);
            assertThat(brand.brandIconImageUrlValue()).isEqualTo(BrandFixtures.DEFAULT_ICON_URL);
            assertThat(brand.displayNameValue()).isEqualTo(BrandFixtures.DEFAULT_DISPLAY_NAME);
            assertThat(brand.displayOrderValue()).isEqualTo(BrandFixtures.DEFAULT_DISPLAY_ORDER);
            assertThat(brand.isDisplayed()).isTrue();
            assertThat(brand.isDeleted()).isFalse();
        }

        @Test
        @DisplayName("커스텀 값으로 브랜드를 생성한다")
        void createWithCustomValues() {
            // when
            var brand = BrandFixtures.newBrand("커스텀브랜드", "커스텀 표시명");

            // then
            assertThat(brand.brandNameValue()).isEqualTo("커스텀브랜드");
            assertThat(brand.displayNameValue()).isEqualTo("커스텀 표시명");
        }
    }

    @Nested
    @DisplayName("reconstitute() - 영속성 복원")
    class ReconstituteTest {

        @Test
        @DisplayName("활성 상태의 브랜드를 복원한다")
        void reconstituteActiveBrand() {
            // when
            var brand = BrandFixtures.activeBrand();

            // then
            assertThat(brand.isNew()).isFalse();
            assertThat(brand.idValue()).isEqualTo(1L);
            assertThat(brand.isDisplayed()).isTrue();
            assertThat(brand.isDeleted()).isFalse();
            assertThat(brand.deletedAt()).isNull();
        }

        @Test
        @DisplayName("삭제된 브랜드를 복원한다")
        void reconstituteDeletedBrand() {
            // when
            var brand = BrandFixtures.deletedBrand();

            // then
            assertThat(brand.isDeleted()).isTrue();
            assertThat(brand.deletedAt()).isNotNull();
        }
    }

    @Nested
    @DisplayName("update() - 브랜드 수정")
    class UpdateTest {

        @Test
        @DisplayName("브랜드 정보를 수정한다")
        void updateBrand() {
            // given
            var brand = BrandFixtures.activeBrand();
            var updateData = BrandFixtures.brandUpdateData(5, false);
            Instant now = CommonVoFixtures.now();

            // when
            brand.update(updateData, now);

            // then
            assertThat(brand.displayOrderValue()).isEqualTo(5);
            assertThat(brand.isDisplayed()).isFalse();
            assertThat(brand.updatedAt()).isEqualTo(now);
        }
    }

    @Nested
    @DisplayName("show() / hide() - 표시 상태 변경")
    class DisplayTest {

        @Test
        @DisplayName("비표시 상태의 브랜드를 표시한다")
        void showBrand() {
            // given
            var brand = BrandFixtures.inactiveBrand();
            Instant now = CommonVoFixtures.now();

            // when
            brand.show(now);

            // then
            assertThat(brand.isDisplayed()).isTrue();
            assertThat(brand.updatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("표시 상태의 브랜드를 비표시한다")
        void hideBrand() {
            // given
            var brand = BrandFixtures.activeBrand();
            Instant now = CommonVoFixtures.now();

            // when
            brand.hide(now);

            // then
            assertThat(brand.isDisplayed()).isFalse();
            assertThat(brand.updatedAt()).isEqualTo(now);
        }
    }

    @Nested
    @DisplayName("delete() / restore() - 삭제 및 복원")
    class DeletionTest {

        @Test
        @DisplayName("브랜드를 삭제(Soft Delete)한다")
        void deleteBrand() {
            // given
            var brand = BrandFixtures.activeBrand();
            Instant now = CommonVoFixtures.now();

            // when
            brand.delete(now);

            // then
            assertThat(brand.isDeleted()).isTrue();
            assertThat(brand.deletedAt()).isEqualTo(now);
            assertThat(brand.updatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("삭제된 브랜드를 복원한다")
        void restoreBrand() {
            // given
            var brand = BrandFixtures.deletedBrand();
            Instant now = CommonVoFixtures.now();

            // when
            brand.restore(now);

            // then
            assertThat(brand.isDeleted()).isFalse();
            assertThat(brand.deletedAt()).isNull();
            assertThat(brand.updatedAt()).isEqualTo(now);
        }
    }

    @Nested
    @DisplayName("Getter 메서드 테스트")
    class GetterTest {

        @Test
        @DisplayName("id()는 BrandId를 반환한다")
        void returnsId() {
            var brand = BrandFixtures.activeBrand();
            assertThat(brand.id()).isNotNull();
            assertThat(brand.idValue()).isEqualTo(1L);
        }

        @Test
        @DisplayName("brandName()은 BrandName을 반환한다")
        void returnsBrandName() {
            var brand = BrandFixtures.activeBrand();
            assertThat(brand.brandName()).isNotNull();
            assertThat(brand.brandNameValue()).isEqualTo(BrandFixtures.DEFAULT_BRAND_NAME);
        }

        @Test
        @DisplayName("displayOrder()는 DisplayOrder를 반환한다")
        void returnsDisplayOrder() {
            var brand = BrandFixtures.activeBrand();
            assertThat(brand.displayOrder()).isNotNull();
            assertThat(brand.displayOrderValue()).isEqualTo(BrandFixtures.DEFAULT_DISPLAY_ORDER);
        }

        @Test
        @DisplayName("deletionStatus()는 DeletionStatus를 반환한다")
        void returnsDeletionStatus() {
            var brand = BrandFixtures.activeBrand();
            assertThat(brand.deletionStatus()).isNotNull();
            assertThat(brand.deletionStatus().isDeleted()).isFalse();
        }

        @Test
        @DisplayName("createdAt()은 생성 시각을 반환한다")
        void returnsCreatedAt() {
            var brand = BrandFixtures.activeBrand();
            assertThat(brand.createdAt()).isNotNull();
        }
    }
}
