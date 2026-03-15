package com.ryuqq.setof.domain.imagevariant.aggregate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import com.ryuqq.setof.domain.common.CommonVoFixtures;
import com.ryuqq.setof.domain.imagevariant.ImageVariantFixtures;
import com.ryuqq.setof.domain.imagevariant.id.ImageVariantId;
import com.ryuqq.setof.domain.imagevariant.vo.ImageDimension;
import com.ryuqq.setof.domain.imagevariant.vo.ImageSourceType;
import com.ryuqq.setof.domain.imagevariant.vo.ImageVariantType;
import com.ryuqq.setof.domain.imagevariant.vo.ResultAssetId;
import com.ryuqq.setof.domain.productgroup.vo.ImageUrl;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ImageVariant Aggregate 단위 테스트")
class ImageVariantTest {

    @Nested
    @DisplayName("forNew() - 신규 ImageVariant 생성")
    class ForNewTest {

        @Test
        @DisplayName("필수 정보로 신규 ImageVariant를 생성한다")
        void createNewImageVariantWithRequiredFields() {
            // given
            Long sourceImageId = 100L;
            ImageSourceType sourceType = ImageSourceType.PRODUCT_GROUP_IMAGE;
            ImageVariantType variantType = ImageVariantType.MEDIUM_WEBP;
            ResultAssetId resultAssetId = ImageVariantFixtures.defaultResultAssetId();
            ImageUrl variantUrl = ImageVariantFixtures.defaultVariantUrl();
            ImageDimension dimension = ImageVariantFixtures.defaultDimension();
            Instant now = CommonVoFixtures.now();

            // when
            ImageVariant variant =
                    ImageVariant.forNew(
                            sourceImageId,
                            sourceType,
                            variantType,
                            resultAssetId,
                            variantUrl,
                            dimension,
                            now);

            // then
            assertThat(variant).isNotNull();
            assertThat(variant.isNew()).isTrue();
            assertThat(variant.sourceImageId()).isEqualTo(sourceImageId);
            assertThat(variant.sourceType()).isEqualTo(sourceType);
            assertThat(variant.variantType()).isEqualTo(variantType);
            assertThat(variant.resultAssetId()).isEqualTo(resultAssetId);
            assertThat(variant.variantUrl()).isEqualTo(variantUrl);
            assertThat(variant.dimension()).isEqualTo(dimension);
            assertThat(variant.createdAt()).isEqualTo(now);
            assertThat(variant.deletedAt()).isNull();
            assertThat(variant.isDeleted()).isFalse();
        }

        @Test
        @DisplayName("신규 생성 시 ID는 null 값(forNew 상태)이다")
        void newImageVariantHasNullId() {
            // when
            ImageVariant variant = ImageVariantFixtures.newImageVariant();

            // then
            assertThat(variant.isNew()).isTrue();
            assertThat(variant.id().isNew()).isTrue();
        }

        @Test
        @DisplayName("ORIGINAL_WEBP 타입으로 dimension null을 허용하여 생성한다")
        void createOriginalWebpVariantWithNullDimension() {
            // given
            ImageDimension nullDimension = ImageVariantFixtures.nullDimension();
            Instant now = CommonVoFixtures.now();

            // when
            ImageVariant variant =
                    ImageVariant.forNew(
                            100L,
                            ImageSourceType.PRODUCT_GROUP_IMAGE,
                            ImageVariantType.ORIGINAL_WEBP,
                            ImageVariantFixtures.defaultResultAssetId(),
                            ImageVariantFixtures.defaultVariantUrl(),
                            nullDimension,
                            now);

            // then
            assertThat(variant.dimension().hasValues()).isFalse();
            assertThat(variant.width()).isNull();
            assertThat(variant.height()).isNull();
        }

        @Test
        @DisplayName("DESCRIPTION_IMAGE 소스 타입으로 생성한다")
        void createImageVariantWithDescriptionSourceType() {
            // when
            ImageVariant variant =
                    ImageVariant.forNew(
                            200L,
                            ImageSourceType.DESCRIPTION_IMAGE,
                            ImageVariantType.SMALL_WEBP,
                            ImageVariantFixtures.defaultResultAssetId(),
                            ImageVariantFixtures.defaultVariantUrl(),
                            ImageVariantFixtures.smallDimension(),
                            CommonVoFixtures.now());

            // then
            assertThat(variant.sourceType()).isEqualTo(ImageSourceType.DESCRIPTION_IMAGE);
            assertThat(variant.sourceType().isDescriptionImage()).isTrue();
        }
    }

    @Nested
    @DisplayName("reconstitute() - 영속성에서 복원")
    class ReconstituteTest {

        @Test
        @DisplayName("영속성에서 활성 ImageVariant를 복원한다")
        void reconstituteActiveImageVariant() {
            // given
            ImageVariantId id = ImageVariantFixtures.defaultImageVariantId();
            Instant createdAt = CommonVoFixtures.yesterday();

            // when
            ImageVariant variant =
                    ImageVariant.reconstitute(
                            id,
                            100L,
                            ImageSourceType.PRODUCT_GROUP_IMAGE,
                            ImageVariantType.MEDIUM_WEBP,
                            ImageVariantFixtures.defaultResultAssetId(),
                            ImageVariantFixtures.defaultVariantUrl(),
                            ImageVariantFixtures.defaultDimension(),
                            createdAt,
                            null);

            // then
            assertThat(variant.isNew()).isFalse();
            assertThat(variant.id()).isEqualTo(id);
            assertThat(variant.isDeleted()).isFalse();
            assertThat(variant.deletedAt()).isNull();
            assertThat(variant.createdAt()).isEqualTo(createdAt);
        }

        @Test
        @DisplayName("영속성에서 삭제된 ImageVariant를 복원한다")
        void reconstituteDeletedImageVariant() {
            // given
            Instant deletedAt = CommonVoFixtures.yesterday();

            // when
            ImageVariant variant =
                    ImageVariant.reconstitute(
                            ImageVariantFixtures.defaultImageVariantId(),
                            100L,
                            ImageSourceType.PRODUCT_GROUP_IMAGE,
                            ImageVariantType.SMALL_WEBP,
                            ImageVariantFixtures.defaultResultAssetId(),
                            ImageVariantFixtures.defaultVariantUrl(),
                            ImageVariantFixtures.smallDimension(),
                            CommonVoFixtures.yesterday(),
                            deletedAt);

            // then
            assertThat(variant.isDeleted()).isTrue();
            assertThat(variant.deletedAt()).isEqualTo(deletedAt);
        }
    }

    @Nested
    @DisplayName("delete() - 소프트 삭제")
    class DeleteTest {

        @Test
        @DisplayName("활성 ImageVariant를 소프트 삭제한다")
        void deleteActiveImageVariant() {
            // given
            ImageVariant variant = ImageVariantFixtures.activeImageVariant();
            Instant now = CommonVoFixtures.now();

            // when
            variant.delete(now);

            // then
            assertThat(variant.isDeleted()).isTrue();
            assertThat(variant.deletedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("소프트 삭제 후 deletedAt이 설정된다")
        void deleteSetsDeletedAt() {
            // given
            ImageVariant variant = ImageVariantFixtures.newImageVariant();
            Instant now = CommonVoFixtures.now();

            // when
            variant.delete(now);

            // then
            assertThat(variant.deletedAt()).isNotNull();
            assertThat(variant.deletedAt()).isEqualTo(now);
        }
    }

    @Nested
    @DisplayName("isNew() / isDeleted() - 상태 확인")
    class StateCheckTest {

        @Test
        @DisplayName("신규 생성 variant는 isNew()가 true이다")
        void newVariantIsNew() {
            ImageVariant variant = ImageVariantFixtures.newImageVariant();
            assertThat(variant.isNew()).isTrue();
        }

        @Test
        @DisplayName("영속화된 variant는 isNew()가 false이다")
        void persistedVariantIsNotNew() {
            ImageVariant variant = ImageVariantFixtures.activeImageVariant();
            assertThat(variant.isNew()).isFalse();
        }

        @Test
        @DisplayName("deletedAt이 null이면 isDeleted()가 false이다")
        void notDeletedWhenDeletedAtIsNull() {
            ImageVariant variant = ImageVariantFixtures.activeImageVariant();
            assertThat(variant.isDeleted()).isFalse();
        }

        @Test
        @DisplayName("deletedAt이 설정되면 isDeleted()가 true이다")
        void deletedWhenDeletedAtIsSet() {
            ImageVariant variant = ImageVariantFixtures.deletedImageVariant();
            assertThat(variant.isDeleted()).isTrue();
        }
    }

    @Nested
    @DisplayName("Getter 메서드 테스트")
    class GetterTest {

        @Test
        @DisplayName("idValue()는 ID의 Long 값을 반환한다")
        void idValueReturnsLongValue() {
            // given
            ImageVariant variant = ImageVariantFixtures.activeImageVariant(42L);

            // then
            assertThat(variant.idValue()).isEqualTo(42L);
        }

        @Test
        @DisplayName("resultAssetIdValue()는 에셋 ID의 문자열 값을 반환한다")
        void resultAssetIdValueReturnsStringValue() {
            // given
            ImageVariant variant = ImageVariantFixtures.activeImageVariant();

            // then
            assertThat(variant.resultAssetIdValue()).isEqualTo("asset-uuid-001");
        }

        @Test
        @DisplayName("variantUrlValue()는 URL의 문자열 값을 반환한다")
        void variantUrlValueReturnsStringValue() {
            // given
            ImageVariant variant = ImageVariantFixtures.activeImageVariant();

            // then
            assertThat(variant.variantUrlValue())
                    .isEqualTo("https://cdn.example.com/images/variant/600x600.webp");
        }

        @Test
        @DisplayName("width()와 height()는 dimension의 값을 반환한다")
        void widthAndHeightReturnDimensionValues() {
            // given
            ImageVariant variant = ImageVariantFixtures.activeImageVariant();

            // then
            assertThat(variant.width()).isEqualTo(600);
            assertThat(variant.height()).isEqualTo(600);
        }

        @Test
        @DisplayName("삭제 전에는 delete() 호출이 예외 없이 처리된다")
        void deleteCallDoesNotThrowException() {
            // given
            ImageVariant variant = ImageVariantFixtures.activeImageVariant();

            // when & then
            assertThatCode(() -> variant.delete(CommonVoFixtures.now())).doesNotThrowAnyException();
        }
    }
}
