package com.ryuqq.setof.domain.imagevariant.vo;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.CommonVoFixtures;
import com.ryuqq.setof.domain.imagevariant.ImageVariantFixtures;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ImageVariantUpdateData Value Object 테스트")
class ImageVariantUpdateDataTest {

    @Nested
    @DisplayName("of() - 생성 테스트")
    class CreationTest {

        @Test
        @DisplayName("신규 variant 목록과 수정 시각으로 생성한다")
        void createWithNewVariantsAndUpdatedAt() {
            // given
            ImageVariants newVariants = ImageVariantFixtures.singleImageVariants();
            Instant updatedAt = CommonVoFixtures.now();

            // when
            ImageVariantUpdateData updateData = ImageVariantUpdateData.of(newVariants, updatedAt);

            // then
            assertThat(updateData.newVariants()).isEqualTo(newVariants);
            assertThat(updateData.updatedAt()).isEqualTo(updatedAt);
        }

        @Test
        @DisplayName("빈 variant 목록으로 생성한다")
        void createWithEmptyVariants() {
            // given
            ImageVariants emptyVariants = ImageVariantFixtures.emptyImageVariants();
            Instant updatedAt = CommonVoFixtures.now();

            // when
            ImageVariantUpdateData updateData = ImageVariantUpdateData.of(emptyVariants, updatedAt);

            // then
            assertThat(updateData.newVariants().isEmpty()).isTrue();
            assertThat(updateData.updatedAt()).isEqualTo(updatedAt);
        }
    }

    @Nested
    @DisplayName("불변성 테스트")
    class ImmutabilityTest {

        @Test
        @DisplayName("생성 후 newVariants와 updatedAt은 변경되지 않는다")
        void fieldsDoNotChangeAfterCreation() {
            // given
            ImageVariants newVariants = ImageVariantFixtures.singleImageVariants();
            Instant updatedAt = CommonVoFixtures.now();
            ImageVariantUpdateData updateData = ImageVariantUpdateData.of(newVariants, updatedAt);

            // then
            assertThat(updateData.newVariants()).isSameAs(newVariants);
            assertThat(updateData.updatedAt()).isEqualTo(updatedAt);
        }
    }
}
