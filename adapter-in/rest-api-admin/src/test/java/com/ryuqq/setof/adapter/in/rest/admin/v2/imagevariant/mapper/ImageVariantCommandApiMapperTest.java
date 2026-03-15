package com.ryuqq.setof.adapter.in.rest.admin.v2.imagevariant.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.in.rest.admin.imagevariant.ImageVariantApiFixtures;
import com.ryuqq.setof.adapter.in.rest.admin.v2.imagevariant.dto.command.SyncImageVariantsApiRequest;
import com.ryuqq.setof.application.imagevariant.dto.command.SyncImageVariantsCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * ImageVariantCommandApiMapper 단위 테스트.
 *
 * <p>Command API Mapper의 변환 로직을 테스트합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@DisplayName("ImageVariantCommandApiMapper 단위 테스트")
class ImageVariantCommandApiMapperTest {

    private ImageVariantCommandApiMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ImageVariantCommandApiMapper();
    }

    @Nested
    @DisplayName("toSyncCommand(SyncImageVariantsApiRequest)")
    class ToSyncCommandTest {

        @Test
        @DisplayName("동기화 요청을 SyncImageVariantsCommand로 변환한다")
        void toSyncCommand_Success() {
            // given
            SyncImageVariantsApiRequest request = ImageVariantApiFixtures.syncRequest();

            // when
            SyncImageVariantsCommand command = mapper.toSyncCommand(request);

            // then
            assertThat(command.sourceImageId()).isEqualTo(request.sourceImageId());
            assertThat(command.sourceType()).isEqualTo(request.sourceType());
        }

        @Test
        @DisplayName("Variant 목록이 SyncImageVariantsCommand.VariantCommand 목록으로 변환된다")
        void toSyncCommand_VariantList_Success() {
            // given
            SyncImageVariantsApiRequest request = ImageVariantApiFixtures.syncRequest();

            // when
            SyncImageVariantsCommand command = mapper.toSyncCommand(request);

            // then
            assertThat(command.variants()).hasSize(2);

            SyncImageVariantsCommand.VariantCommand firstVariant = command.variants().get(0);
            SyncImageVariantsApiRequest.VariantApiRequest firstRequestVariant =
                    request.variants().get(0);

            assertThat(firstVariant.variantType()).isEqualTo(firstRequestVariant.variantType());
            assertThat(firstVariant.resultAssetId()).isEqualTo(firstRequestVariant.resultAssetId());
            assertThat(firstVariant.variantUrl()).isEqualTo(firstRequestVariant.variantUrl());
            assertThat(firstVariant.width()).isEqualTo(firstRequestVariant.width());
            assertThat(firstVariant.height()).isEqualTo(firstRequestVariant.height());
        }

        @Test
        @DisplayName("모든 Variant 필드가 Command로 올바르게 매핑된다")
        void toSyncCommand_AllVariantFieldsMapped() {
            // given
            SyncImageVariantsApiRequest request = ImageVariantApiFixtures.syncRequest();

            // when
            SyncImageVariantsCommand command = mapper.toSyncCommand(request);

            // then
            assertThat(command.variants()).hasSize(2);
            assertThat(command.variants().get(0).variantType())
                    .isEqualTo(ImageVariantApiFixtures.DEFAULT_VARIANT_TYPE_SMALL);
            assertThat(command.variants().get(1).variantType())
                    .isEqualTo(ImageVariantApiFixtures.DEFAULT_VARIANT_TYPE_MEDIUM);
        }

        @Test
        @DisplayName("단일 Variant 동기화 요청을 Command로 변환한다")
        void toSyncCommand_SingleVariant_Success() {
            // given
            SyncImageVariantsApiRequest request =
                    ImageVariantApiFixtures.syncRequestWithSingleVariant();

            // when
            SyncImageVariantsCommand command = mapper.toSyncCommand(request);

            // then
            assertThat(command.sourceImageId())
                    .isEqualTo(ImageVariantApiFixtures.DEFAULT_SOURCE_IMAGE_ID);
            assertThat(command.sourceType()).isEqualTo(ImageVariantApiFixtures.DEFAULT_SOURCE_TYPE);
            assertThat(command.variants()).hasSize(1);
            assertThat(command.variants().get(0).variantType())
                    .isEqualTo(ImageVariantApiFixtures.DEFAULT_VARIANT_TYPE_SMALL);
        }

        @Test
        @DisplayName("width와 height가 null인 Variant 요청을 Command로 변환한다")
        void toSyncCommand_NullDimensions_Success() {
            // given
            SyncImageVariantsApiRequest request =
                    ImageVariantApiFixtures.syncRequestWithNullDimensions();

            // when
            SyncImageVariantsCommand command = mapper.toSyncCommand(request);

            // then
            assertThat(command.variants()).hasSize(1);
            assertThat(command.variants().get(0).width()).isNull();
            assertThat(command.variants().get(0).height()).isNull();
        }
    }
}
