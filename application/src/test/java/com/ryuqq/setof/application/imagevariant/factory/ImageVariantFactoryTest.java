package com.ryuqq.setof.application.imagevariant.factory;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.application.imagevariant.ImageVariantCommandFixtures;
import com.ryuqq.setof.application.imagevariant.dto.command.SyncImageVariantsCommand.VariantCommand;
import com.ryuqq.setof.domain.imagevariant.aggregate.ImageVariant;
import com.ryuqq.setof.domain.imagevariant.vo.ImageSourceType;
import com.ryuqq.setof.domain.imagevariant.vo.ImageVariantType;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ImageVariantFactory 단위 테스트")
class ImageVariantFactoryTest {

    private ImageVariantFactory sut;

    @BeforeEach
    void setUp() {
        sut = new ImageVariantFactory();
    }

    @Nested
    @DisplayName("createVariants() - VariantCommand 목록 → ImageVariant 도메인 객체 목록 변환")
    class CreateVariantsTest {

        @Test
        @DisplayName("유효한 커맨드 목록으로 ImageVariant 도메인 객체 목록을 생성한다")
        void createVariants_ValidCommands_ReturnsImageVariantList() {
            // given
            Long sourceImageId = 100L;
            ImageSourceType sourceType = ImageSourceType.PRODUCT_GROUP_IMAGE;
            List<VariantCommand> variantCommands = ImageVariantCommandFixtures.variantCommands();
            Instant now = Instant.parse("2024-01-01T00:00:00Z");

            // when
            List<ImageVariant> result =
                    sut.createVariants(sourceImageId, sourceType, variantCommands, now);

            // then
            assertThat(result).hasSize(3);
        }

        @Test
        @DisplayName("생성된 ImageVariant의 sourceImageId가 정확히 반영된다")
        void createVariants_ValidCommands_SourceImageIdReflected() {
            // given
            Long sourceImageId = 999L;
            ImageSourceType sourceType = ImageSourceType.PRODUCT_GROUP_IMAGE;
            List<VariantCommand> variantCommands =
                    List.of(ImageVariantCommandFixtures.mediumWebpVariantCommand());
            Instant now = Instant.parse("2024-01-01T00:00:00Z");

            // when
            List<ImageVariant> result =
                    sut.createVariants(sourceImageId, sourceType, variantCommands, now);

            // then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).sourceImageId()).isEqualTo(sourceImageId);
        }

        @Test
        @DisplayName("생성된 ImageVariant의 variantType이 커맨드와 일치한다")
        void createVariants_ValidCommands_VariantTypeReflected() {
            // given
            Long sourceImageId = 100L;
            ImageSourceType sourceType = ImageSourceType.PRODUCT_GROUP_IMAGE;
            List<VariantCommand> variantCommands =
                    List.of(ImageVariantCommandFixtures.mediumWebpVariantCommand());
            Instant now = Instant.parse("2024-01-01T00:00:00Z");

            // when
            List<ImageVariant> result =
                    sut.createVariants(sourceImageId, sourceType, variantCommands, now);

            // then
            assertThat(result.get(0).variantType()).isEqualTo(ImageVariantType.MEDIUM_WEBP);
        }

        @Test
        @DisplayName("생성된 ImageVariant의 resultAssetId가 커맨드와 일치한다")
        void createVariants_ValidCommands_ResultAssetIdReflected() {
            // given
            Long sourceImageId = 100L;
            ImageSourceType sourceType = ImageSourceType.PRODUCT_GROUP_IMAGE;
            VariantCommand cmd = ImageVariantCommandFixtures.mediumWebpVariantCommand();
            Instant now = Instant.parse("2024-01-01T00:00:00Z");

            // when
            List<ImageVariant> result =
                    sut.createVariants(sourceImageId, sourceType, List.of(cmd), now);

            // then
            assertThat(result.get(0).resultAssetIdValue()).isEqualTo(cmd.resultAssetId());
        }

        @Test
        @DisplayName("생성된 ImageVariant의 variantUrl이 커맨드와 일치한다")
        void createVariants_ValidCommands_VariantUrlReflected() {
            // given
            Long sourceImageId = 100L;
            ImageSourceType sourceType = ImageSourceType.PRODUCT_GROUP_IMAGE;
            VariantCommand cmd = ImageVariantCommandFixtures.mediumWebpVariantCommand();
            Instant now = Instant.parse("2024-01-01T00:00:00Z");

            // when
            List<ImageVariant> result =
                    sut.createVariants(sourceImageId, sourceType, List.of(cmd), now);

            // then
            assertThat(result.get(0).variantUrlValue()).isEqualTo(cmd.variantUrl());
        }

        @Test
        @DisplayName("생성된 ImageVariant의 너비와 높이가 커맨드와 일치한다")
        void createVariants_ValidCommands_DimensionReflected() {
            // given
            Long sourceImageId = 100L;
            ImageSourceType sourceType = ImageSourceType.PRODUCT_GROUP_IMAGE;
            VariantCommand cmd = ImageVariantCommandFixtures.mediumWebpVariantCommand();
            Instant now = Instant.parse("2024-01-01T00:00:00Z");

            // when
            List<ImageVariant> result =
                    sut.createVariants(sourceImageId, sourceType, List.of(cmd), now);

            // then
            assertThat(result.get(0).width()).isEqualTo(cmd.width());
            assertThat(result.get(0).height()).isEqualTo(cmd.height());
        }

        @Test
        @DisplayName("DESCRIPTION_IMAGE 소스 타입으로도 정상 생성된다")
        void createVariants_DescriptionImageSourceType_ReturnsImageVariantList() {
            // given
            Long sourceImageId = 200L;
            ImageSourceType sourceType = ImageSourceType.DESCRIPTION_IMAGE;
            List<VariantCommand> variantCommands =
                    List.of(ImageVariantCommandFixtures.largeWebpVariantCommand());
            Instant now = Instant.parse("2024-01-01T00:00:00Z");

            // when
            List<ImageVariant> result =
                    sut.createVariants(sourceImageId, sourceType, variantCommands, now);

            // then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).sourceType()).isEqualTo(ImageSourceType.DESCRIPTION_IMAGE);
        }

        @Test
        @DisplayName("커맨드 목록이 null이면 빈 목록을 반환한다")
        void createVariants_NullCommands_ReturnsEmptyList() {
            // given
            Long sourceImageId = 100L;
            ImageSourceType sourceType = ImageSourceType.PRODUCT_GROUP_IMAGE;
            Instant now = Instant.parse("2024-01-01T00:00:00Z");

            // when
            List<ImageVariant> result = sut.createVariants(sourceImageId, sourceType, null, now);

            // then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("커맨드 목록이 비어있으면 빈 목록을 반환한다")
        void createVariants_EmptyCommands_ReturnsEmptyList() {
            // given
            Long sourceImageId = 100L;
            ImageSourceType sourceType = ImageSourceType.PRODUCT_GROUP_IMAGE;
            Instant now = Instant.parse("2024-01-01T00:00:00Z");

            // when
            List<ImageVariant> result =
                    sut.createVariants(sourceImageId, sourceType, List.of(), now);

            // then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("ORIGINAL_WEBP 타입의 경우 dimension이 null이어도 정상 생성된다")
        void createVariants_OriginalWebpVariant_CreatesWithNullDimension() {
            // given
            Long sourceImageId = 100L;
            ImageSourceType sourceType = ImageSourceType.PRODUCT_GROUP_IMAGE;
            List<VariantCommand> variantCommands =
                    List.of(ImageVariantCommandFixtures.originalWebpVariantCommand());
            Instant now = Instant.parse("2024-01-01T00:00:00Z");

            // when
            List<ImageVariant> result =
                    sut.createVariants(sourceImageId, sourceType, variantCommands, now);

            // then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).variantType()).isEqualTo(ImageVariantType.ORIGINAL_WEBP);
            assertThat(result.get(0).width()).isNull();
            assertThat(result.get(0).height()).isNull();
        }

        @Test
        @DisplayName("생성된 모든 ImageVariant는 신규(isNew) 상태이다")
        void createVariants_ValidCommands_AllVariantsAreNew() {
            // given
            Long sourceImageId = 100L;
            ImageSourceType sourceType = ImageSourceType.PRODUCT_GROUP_IMAGE;
            List<VariantCommand> variantCommands = ImageVariantCommandFixtures.variantCommands();
            Instant now = Instant.parse("2024-01-01T00:00:00Z");

            // when
            List<ImageVariant> result =
                    sut.createVariants(sourceImageId, sourceType, variantCommands, now);

            // then
            assertThat(result).allMatch(ImageVariant::isNew);
        }
    }
}
