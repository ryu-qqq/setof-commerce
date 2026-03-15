package com.ryuqq.setof.domain.imagevariant.vo;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.CommonVoFixtures;
import com.ryuqq.setof.domain.imagevariant.ImageVariantFixtures;
import com.ryuqq.setof.domain.imagevariant.aggregate.ImageVariant;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ImageVariants Collection VO 테스트")
class ImageVariantsTest {

    @Nested
    @DisplayName("of() - 생성 테스트")
    class OfCreationTest {

        @Test
        @DisplayName("빈 리스트로 ImageVariants를 생성한다")
        void createWithEmptyList() {
            // when
            ImageVariants variants = ImageVariants.of(List.of());

            // then
            assertThat(variants.isEmpty()).isTrue();
            assertThat(variants.size()).isZero();
        }

        @Test
        @DisplayName("null로 생성하면 빈 ImageVariants가 된다")
        void createWithNullReturnsEmpty() {
            // when
            ImageVariants variants = ImageVariants.of(null);

            // then
            assertThat(variants.isEmpty()).isTrue();
        }

        @Test
        @DisplayName("variant 목록으로 생성한다")
        void createWithVariantList() {
            // given
            List<ImageVariant> list =
                    List.of(
                            ImageVariantFixtures.activeImageVariant(1L),
                            ImageVariantFixtures.activeImageVariant(2L));

            // when
            ImageVariants variants = ImageVariants.of(list);

            // then
            assertThat(variants.isEmpty()).isFalse();
            assertThat(variants.size()).isEqualTo(2);
        }
    }

    @Nested
    @DisplayName("reconstitute() - 영속성 복원 테스트")
    class ReconstituteTest {

        @Test
        @DisplayName("영속성에서 variant 목록을 복원한다")
        void reconstituteVariantList() {
            // given
            List<ImageVariant> list = List.of(ImageVariantFixtures.activeImageVariant());

            // when
            ImageVariants variants = ImageVariants.reconstitute(list);

            // then
            assertThat(variants.size()).isEqualTo(1);
        }

        @Test
        @DisplayName("null로 복원하면 빈 ImageVariants가 된다")
        void reconstituteWithNullReturnsEmpty() {
            // when
            ImageVariants variants = ImageVariants.reconstitute(null);

            // then
            assertThat(variants.isEmpty()).isTrue();
        }
    }

    @Nested
    @DisplayName("toList() - 불변 리스트 반환 테스트")
    class ToListTest {

        @Test
        @DisplayName("toList()는 unmodifiable 리스트를 반환한다")
        void toListReturnsUnmodifiableList() {
            // given
            ImageVariants variants = ImageVariantFixtures.singleImageVariants();

            // when
            List<ImageVariant> list = variants.toList();

            // then
            assertThat(list).hasSize(1);
            org.assertj.core.api.Assertions.assertThatThrownBy(
                            () -> list.add(ImageVariantFixtures.newImageVariant()))
                    .isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @Nested
    @DisplayName("update() - diff 비교 및 상태 갱신")
    class UpdateTest {

        @Test
        @DisplayName("새 variant를 추가하면 added 목록에 포함된다")
        void updateAddsNewVariants() {
            // given
            ImageVariants existing = ImageVariantFixtures.emptyImageVariants();
            ImageVariant newVariant = ImageVariantFixtures.newImageVariant();
            ImageVariants newVariants = ImageVariants.of(List.of(newVariant));
            ImageVariantUpdateData updateData =
                    ImageVariantUpdateData.of(newVariants, CommonVoFixtures.now());

            // when
            ImageVariantDiff diff = existing.update(updateData);

            // then
            assertThat(diff.added()).hasSize(1);
            assertThat(diff.removed()).isEmpty();
            assertThat(diff.retained()).isEmpty();
        }

        @Test
        @DisplayName("기존 variant와 동일한 variantType + variantUrl이면 retained에 포함된다")
        void updateRetainsSameVariants() {
            // given
            ImageVariant existingVariant = ImageVariantFixtures.activeImageVariant();
            ImageVariants existing = ImageVariants.of(List.of(existingVariant));

            // 동일한 variantType + variantUrl을 가진 신규 variant
            ImageVariant sameKeyVariant =
                    ImageVariant.reconstitute(
                            ImageVariantFixtures.newImageVariantId(),
                            100L,
                            com.ryuqq.setof.domain.imagevariant.vo.ImageSourceType
                                    .PRODUCT_GROUP_IMAGE,
                            ImageVariantType.MEDIUM_WEBP,
                            ImageVariantFixtures.defaultResultAssetId(),
                            ImageVariantFixtures.defaultVariantUrl(),
                            ImageVariantFixtures.defaultDimension(),
                            CommonVoFixtures.now(),
                            null);

            ImageVariants newVariants = ImageVariants.of(List.of(sameKeyVariant));
            ImageVariantUpdateData updateData =
                    ImageVariantUpdateData.of(newVariants, CommonVoFixtures.now());

            // when
            ImageVariantDiff diff = existing.update(updateData);

            // then
            assertThat(diff.retained()).hasSize(1);
            assertThat(diff.added()).isEmpty();
            assertThat(diff.removed()).isEmpty();
        }

        @Test
        @DisplayName("새 목록에 없는 기존 variant는 removed에 포함되고 소프트 삭제된다")
        void updateRemovesMissingVariants() {
            // given
            ImageVariant existingVariant = ImageVariantFixtures.activeImageVariant();
            ImageVariants existing = ImageVariants.of(List.of(existingVariant));
            ImageVariants emptyNewVariants = ImageVariantFixtures.emptyImageVariants();
            ImageVariantUpdateData updateData =
                    ImageVariantUpdateData.of(emptyNewVariants, CommonVoFixtures.now());

            // when
            ImageVariantDiff diff = existing.update(updateData);

            // then
            assertThat(diff.removed()).hasSize(1);
            assertThat(diff.added()).isEmpty();
            assertThat(diff.retained()).isEmpty();
            assertThat(existingVariant.isDeleted()).isTrue();
        }

        @Test
        @DisplayName("변경 사항이 없으면 hasNoChanges()가 true이다")
        void noChangesWhenSameVariants() {
            // given
            ImageVariants existing = ImageVariantFixtures.emptyImageVariants();
            ImageVariantUpdateData updateData =
                    ImageVariantUpdateData.of(
                            ImageVariantFixtures.emptyImageVariants(), CommonVoFixtures.now());

            // when
            ImageVariantDiff diff = existing.update(updateData);

            // then
            assertThat(diff.hasNoChanges()).isTrue();
        }
    }
}
