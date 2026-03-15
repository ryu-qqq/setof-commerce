package com.ryuqq.setof.domain.imagevariant.vo;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.CommonVoFixtures;
import com.ryuqq.setof.domain.imagevariant.ImageVariantFixtures;
import com.ryuqq.setof.domain.imagevariant.aggregate.ImageVariant;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ImageVariantDiff Value Object 테스트")
class ImageVariantDiffTest {

    @Nested
    @DisplayName("of() - 생성 테스트")
    class CreationTest {

        @Test
        @DisplayName("추가/삭제/유지 목록과 시각으로 생성한다")
        void createWithAllFields() {
            // given
            List<ImageVariant> added = List.of(ImageVariantFixtures.newImageVariant());
            List<ImageVariant> removed = List.of(ImageVariantFixtures.deletedImageVariant());
            List<ImageVariant> retained = List.of(ImageVariantFixtures.activeImageVariant());
            Instant now = CommonVoFixtures.now();

            // when
            ImageVariantDiff diff = ImageVariantDiff.of(added, removed, retained, now);

            // then
            assertThat(diff.added()).hasSize(1);
            assertThat(diff.removed()).hasSize(1);
            assertThat(diff.retained()).hasSize(1);
            assertThat(diff.occurredAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("생성 시 내부 리스트는 불변 복사본이 된다")
        void listsAreImmutableCopies() {
            // given
            List<ImageVariant> added = List.of(ImageVariantFixtures.newImageVariant());
            ImageVariantDiff diff =
                    ImageVariantDiff.of(added, List.of(), List.of(), CommonVoFixtures.now());

            // when & then
            org.assertj.core.api.Assertions.assertThatThrownBy(
                            () -> diff.added().add(ImageVariantFixtures.newImageVariant()))
                    .isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @Nested
    @DisplayName("hasNoChanges() - 변경 없음 확인")
    class HasNoChangesTest {

        @Test
        @DisplayName("added와 removed가 모두 비어 있으면 hasNoChanges()가 true이다")
        void hasNoChangesWhenAddedAndRemovedEmpty() {
            // given
            ImageVariantDiff diff = ImageVariantFixtures.emptyDiff();

            // then
            assertThat(diff.hasNoChanges()).isTrue();
        }

        @Test
        @DisplayName("added에 항목이 있으면 hasNoChanges()가 false이다")
        void hasChangeWhenAddedNotEmpty() {
            // given
            ImageVariantDiff diff =
                    ImageVariantDiff.of(
                            List.of(ImageVariantFixtures.newImageVariant()),
                            List.of(),
                            List.of(),
                            CommonVoFixtures.now());

            // then
            assertThat(diff.hasNoChanges()).isFalse();
        }

        @Test
        @DisplayName("removed에 항목이 있으면 hasNoChanges()가 false이다")
        void hasChangeWhenRemovedNotEmpty() {
            // given
            ImageVariantDiff diff =
                    ImageVariantDiff.of(
                            List.of(),
                            List.of(ImageVariantFixtures.deletedImageVariant()),
                            List.of(),
                            CommonVoFixtures.now());

            // then
            assertThat(diff.hasNoChanges()).isFalse();
        }

        @Test
        @DisplayName("retained만 있으면 hasNoChanges()가 true이다")
        void hasNoChangesWhenOnlyRetained() {
            // given
            ImageVariantDiff diff =
                    ImageVariantDiff.of(
                            List.of(),
                            List.of(),
                            List.of(ImageVariantFixtures.activeImageVariant()),
                            CommonVoFixtures.now());

            // then
            assertThat(diff.hasNoChanges()).isTrue();
        }
    }
}
