package com.ryuqq.setof.domain.productdescription.aggregate;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.CommonVoFixtures;
import com.ryuqq.setof.domain.productdescription.id.ProductGroupDescriptionId;
import com.ryuqq.setof.domain.productdescription.vo.DescriptionHtml;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import com.setof.commerce.domain.productdescription.ProductDescriptionFixtures;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ProductGroupDescription Aggregate 테스트")
class ProductGroupDescriptionTest {

    @Nested
    @DisplayName("forNew() - 신규 상세설명 생성")
    class ForNewTest {

        @Test
        @DisplayName("기본 값으로 신규 상세설명을 생성한다")
        void createNewDescription() {
            // when
            var description = ProductDescriptionFixtures.newDescription();

            // then
            assertThat(description.isNew()).isTrue();
            assertThat(description.contentValue())
                    .isEqualTo(ProductDescriptionFixtures.DEFAULT_DESCRIPTION_HTML);
            assertThat(description.productGroupIdValue())
                    .isEqualTo(ProductDescriptionFixtures.DEFAULT_PRODUCT_GROUP_ID);
            assertThat(description.images()).isEmpty();
            assertThat(description.createdAt()).isNotNull();
            assertThat(description.updatedAt()).isNotNull();
        }

        @Test
        @DisplayName("특정 ProductGroupId로 상세설명을 생성한다")
        void createWithSpecificProductGroupId() {
            // given
            ProductGroupId productGroupId = ProductGroupId.of(999L);

            // when
            var description = ProductDescriptionFixtures.newDescription(productGroupId);

            // then
            assertThat(description.productGroupIdValue()).isEqualTo(999L);
        }

        @Test
        @DisplayName("컨텐츠와 함께 생성한다")
        void createWithContent() {
            // given
            DescriptionHtml content = DescriptionHtml.of("<h1>상세설명</h1>");
            Instant now = CommonVoFixtures.now();

            // when
            var description = ProductGroupDescription.forNew(ProductGroupId.of(1L), content, now);

            // then
            assertThat(description.contentValue()).isEqualTo("<h1>상세설명</h1>");
            assertThat(description.isNew()).isTrue();
        }

        @Test
        @DisplayName("신규 생성 시 isNew()는 true를 반환한다")
        void isNewReturnsTrue() {
            // when
            var description = ProductDescriptionFixtures.newDescription();

            // then
            assertThat(description.isNew()).isTrue();
        }
    }

    @Nested
    @DisplayName("reconstitute() - 영속성 복원")
    class ReconstituteTest {

        @Test
        @DisplayName("컨텐츠와 이미지를 포함하여 복원한다")
        void reconstituteWithContentAndImages() {
            // when
            var description = ProductDescriptionFixtures.activeDescription();

            // then
            assertThat(description.isNew()).isFalse();
            assertThat(description.idValue()).isEqualTo(1L);
            assertThat(description.contentValue())
                    .isEqualTo(ProductDescriptionFixtures.DEFAULT_DESCRIPTION_HTML);
            assertThat(description.images()).hasSize(3);
            assertThat(description.createdAt()).isNotNull();
            assertThat(description.updatedAt()).isNotNull();
        }

        @Test
        @DisplayName("특정 ID로 복원한다")
        void reconstituteWithSpecificId() {
            // when
            var description = ProductDescriptionFixtures.activeDescription(42L);

            // then
            assertThat(description.idValue()).isEqualTo(42L);
        }

        @Test
        @DisplayName("빈 컨텐츠로 복원한다")
        void reconstituteEmpty() {
            // when
            var description = ProductDescriptionFixtures.emptyDescription();

            // then
            assertThat(description.isNew()).isFalse();
            assertThat(description.contentValue()).isNull();
            assertThat(description.images()).isEmpty();
        }
    }

    @Nested
    @DisplayName("updateContent() - 컨텐츠 수정")
    class UpdateContentTest {

        @Test
        @DisplayName("컨텐츠를 수정하면 내용이 변경된다")
        void updateContentChangesValue() {
            // given
            var description = ProductDescriptionFixtures.activeDescription();
            DescriptionHtml newContent = DescriptionHtml.of("<p>수정된 상세설명</p>");
            Instant now = CommonVoFixtures.now();

            // when
            description.updateContent(newContent, now);

            // then
            assertThat(description.contentValue()).isEqualTo("<p>수정된 상세설명</p>");
        }

        @Test
        @DisplayName("컨텐츠 수정 시 updatedAt이 변경된다")
        void updateContentChangesUpdatedAt() {
            // given
            var description = ProductDescriptionFixtures.activeDescription();
            Instant originalUpdatedAt = description.updatedAt();
            Instant now = Instant.now().plusSeconds(3600);

            // when
            description.updateContent(DescriptionHtml.of("<p>새 내용</p>"), now);

            // then
            assertThat(description.updatedAt()).isEqualTo(now);
            assertThat(description.updatedAt()).isNotEqualTo(originalUpdatedAt);
        }
    }

    @Nested
    @DisplayName("replaceImages() - 이미지 교체")
    class ReplaceImagesTest {

        @Test
        @DisplayName("이미지 목록을 교체한다")
        void replaceImagesSuccessfully() {
            // given
            var description = ProductDescriptionFixtures.activeDescription();
            List<DescriptionImage> newImages =
                    List.of(
                            ProductDescriptionFixtures.descriptionImage(0),
                            ProductDescriptionFixtures.descriptionImage(1));
            Instant now = CommonVoFixtures.now();

            // when
            description.replaceImages(newImages, now);

            // then
            assertThat(description.images()).hasSize(2);
            assertThat(description.updatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("null 이미지 목록으로 교체하면 빈 목록이 된다")
        void replaceWithNullClearsImages() {
            // given
            var description = ProductDescriptionFixtures.activeDescription();
            assertThat(description.images()).isNotEmpty();
            Instant now = CommonVoFixtures.now();

            // when
            description.replaceImages(null, now);

            // then
            assertThat(description.images()).isEmpty();
            assertThat(description.updatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("빈 목록으로 교체하면 이미지가 모두 제거된다")
        void replaceWithEmptyListClearsImages() {
            // given
            var description = ProductDescriptionFixtures.activeDescription();
            Instant now = CommonVoFixtures.now();

            // when
            description.replaceImages(List.of(), now);

            // then
            assertThat(description.images()).isEmpty();
        }
    }

    @Nested
    @DisplayName("isEmpty() - 비어있는지 확인")
    class IsEmptyTest {

        @Test
        @DisplayName("컨텐츠와 이미지가 모두 없으면 true를 반환한다")
        void emptyWhenNoContentAndNoImages() {
            // given
            var description = ProductDescriptionFixtures.emptyDescription();

            // then
            assertThat(description.isEmpty()).isTrue();
        }

        @Test
        @DisplayName("컨텐츠가 있으면 false를 반환한다")
        void notEmptyWhenContentExists() {
            // given
            var description =
                    ProductGroupDescription.reconstitute(
                            ProductGroupDescriptionId.of(1L),
                            ProductGroupId.of(1L),
                            DescriptionHtml.of("<p>내용</p>"),
                            List.of(),
                            CommonVoFixtures.yesterday(),
                            CommonVoFixtures.yesterday());

            // then
            assertThat(description.isEmpty()).isFalse();
        }

        @Test
        @DisplayName("이미지만 있으면 false를 반환한다")
        void notEmptyWhenImagesOnly() {
            // given
            var description =
                    ProductGroupDescription.reconstitute(
                            ProductGroupDescriptionId.of(1L),
                            ProductGroupId.of(1L),
                            DescriptionHtml.empty(),
                            ProductDescriptionFixtures.defaultImages(),
                            CommonVoFixtures.yesterday(),
                            CommonVoFixtures.yesterday());

            // then
            assertThat(description.isEmpty()).isFalse();
        }
    }

    @Nested
    @DisplayName("hasImages() - 이미지 보유 여부")
    class HasImagesTest {

        @Test
        @DisplayName("이미지가 있으면 true를 반환한다")
        void hasImagesReturnsTrue() {
            // given
            var description = ProductDescriptionFixtures.activeDescription();

            // then
            assertThat(description.hasImages()).isTrue();
        }

        @Test
        @DisplayName("이미지가 없으면 false를 반환한다")
        void hasImagesReturnsFalse() {
            // given
            var description = ProductDescriptionFixtures.newDescription();

            // then
            assertThat(description.hasImages()).isFalse();
        }
    }

    @Nested
    @DisplayName("Accessor 메서드 테스트")
    class AccessorTest {

        @Test
        @DisplayName("id()는 ProductGroupDescriptionId를 반환한다")
        void returnsId() {
            // given
            var description = ProductDescriptionFixtures.activeDescription();

            // then
            assertThat(description.id()).isNotNull();
            assertThat(description.idValue()).isEqualTo(1L);
        }

        @Test
        @DisplayName("productGroupId()는 ProductGroupId를 반환한다")
        void returnsProductGroupId() {
            // given
            var description = ProductDescriptionFixtures.activeDescription();

            // then
            assertThat(description.productGroupId()).isNotNull();
            assertThat(description.productGroupIdValue())
                    .isEqualTo(ProductDescriptionFixtures.DEFAULT_PRODUCT_GROUP_ID);
        }

        @Test
        @DisplayName("content()는 DescriptionHtml을 반환한다")
        void returnsContent() {
            // given
            var description = ProductDescriptionFixtures.activeDescription();

            // then
            assertThat(description.content()).isNotNull();
            assertThat(description.contentValue())
                    .isEqualTo(ProductDescriptionFixtures.DEFAULT_DESCRIPTION_HTML);
        }

        @Test
        @DisplayName("contentValue()는 null 컨텐츠일 때 null을 반환한다")
        void contentValueReturnsNullWhenContentIsNull() {
            // given
            var description =
                    ProductGroupDescription.reconstitute(
                            ProductGroupDescriptionId.of(1L),
                            ProductGroupId.of(1L),
                            null,
                            List.of(),
                            CommonVoFixtures.yesterday(),
                            CommonVoFixtures.yesterday());

            // then
            assertThat(description.contentValue()).isNull();
        }

        @Test
        @DisplayName("images()는 불변 리스트를 반환한다")
        void returnsUnmodifiableImages() {
            // given
            var description = ProductDescriptionFixtures.activeDescription();

            // then
            assertThat(description.images()).hasSize(3);
            org.junit.jupiter.api.Assertions.assertThrows(
                    UnsupportedOperationException.class,
                    () -> description.images().add(ProductDescriptionFixtures.descriptionImage()));
        }

        @Test
        @DisplayName("createdAt()은 생성 시각을 반환한다")
        void returnsCreatedAt() {
            // given
            var description = ProductDescriptionFixtures.activeDescription();

            // then
            assertThat(description.createdAt()).isNotNull();
        }

        @Test
        @DisplayName("updatedAt()은 수정 시각을 반환한다")
        void returnsUpdatedAt() {
            // given
            var description = ProductDescriptionFixtures.activeDescription();

            // then
            assertThat(description.updatedAt()).isNotNull();
        }
    }
}
