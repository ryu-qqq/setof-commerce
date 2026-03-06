package com.ryuqq.setof.application.productgroupimage.factory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.ryuqq.setof.application.common.time.TimeProvider;
import com.ryuqq.setof.application.productgroupimage.ProductGroupImageCommandFixtures;
import com.ryuqq.setof.application.productgroupimage.dto.command.RegisterProductGroupImagesCommand;
import com.ryuqq.setof.application.productgroupimage.dto.command.UpdateProductGroupImagesCommand;
import com.ryuqq.setof.domain.common.CommonVoFixtures;
import com.ryuqq.setof.domain.productgroup.vo.ImageType;
import com.ryuqq.setof.domain.productgroupimage.vo.ProductGroupImageUpdateData;
import com.ryuqq.setof.domain.productgroupimage.vo.ProductGroupImages;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("ProductGroupImageFactory 단위 테스트")
class ProductGroupImageFactoryTest {

    @InjectMocks private ProductGroupImageFactory sut;

    @Mock private TimeProvider timeProvider;

    @Nested
    @DisplayName("createFromRegistration() - 등록 커맨드에서 ProductGroupImages 생성")
    class CreateFromRegistrationTest {

        @Test
        @DisplayName("등록 커맨드의 이미지 목록으로 ProductGroupImages를 생성한다")
        void createFromRegistration_ValidImages_ReturnsProductGroupImages() {
            // given
            RegisterProductGroupImagesCommand command =
                    ProductGroupImageCommandFixtures.registerCommand();

            // when
            ProductGroupImages result = sut.createFromRegistration(command.images());

            // then
            assertThat(result).isNotNull();
            assertThat(result.toList()).hasSize(2);
            assertThat(result.thumbnail()).isNotNull();
            assertThat(result.thumbnail().isThumbnail()).isTrue();
            assertThat(result.thumbnail().imageUrlValue())
                    .isEqualTo(ProductGroupImageCommandFixtures.DEFAULT_THUMBNAIL_URL);
        }

        @Test
        @DisplayName("썸네일만 포함된 커맨드로 ProductGroupImages를 생성한다")
        void createFromRegistration_ThumbnailOnly_ReturnsProductGroupImages() {
            // given
            RegisterProductGroupImagesCommand command =
                    ProductGroupImageCommandFixtures.registerThumbnailOnlyCommand();

            // when
            ProductGroupImages result = sut.createFromRegistration(command.images());

            // then
            assertThat(result).isNotNull();
            assertThat(result.toList()).hasSize(1);
            assertThat(result.thumbnail().imageType()).isEqualTo(ImageType.THUMBNAIL);
        }

        @Test
        @DisplayName("여러 상세 이미지를 포함한 커맨드로 ProductGroupImages를 생성한다")
        void createFromRegistration_MultipleDetails_ReturnsProductGroupImages() {
            // given
            RegisterProductGroupImagesCommand command =
                    ProductGroupImageCommandFixtures.registerCommandWithMultipleDetails();

            // when
            ProductGroupImages result = sut.createFromRegistration(command.images());

            // then
            assertThat(result).isNotNull();
            assertThat(result.toList()).hasSize(3);
            assertThat(result.detailImages()).hasSize(2);
        }

        @Test
        @DisplayName("생성된 이미지의 imageType이 커맨드와 일치한다")
        void createFromRegistration_ImageTypeMatchesCommand() {
            // given
            RegisterProductGroupImagesCommand command =
                    ProductGroupImageCommandFixtures.registerCommand();

            // when
            ProductGroupImages result = sut.createFromRegistration(command.images());

            // then
            assertThat(result.thumbnail().imageType()).isEqualTo(ImageType.THUMBNAIL);
            assertThat(result.detailImages().get(0).imageType()).isEqualTo(ImageType.DETAIL);
        }
    }

    @Nested
    @DisplayName("createUpdateData() - 수정 커맨드에서 ProductGroupImageUpdateData 생성")
    class CreateUpdateDataTest {

        @Test
        @DisplayName("수정 커맨드로 ProductGroupImageUpdateData를 생성한다")
        void createUpdateData_ValidCommand_ReturnsUpdateData() {
            // given
            UpdateProductGroupImagesCommand command =
                    ProductGroupImageCommandFixtures.updateCommand();
            Instant now = CommonVoFixtures.now();
            given(timeProvider.now()).willReturn(now);

            // when
            ProductGroupImageUpdateData result = sut.createUpdateData(command);

            // then
            assertThat(result).isNotNull();
            assertThat(result.newImages()).isNotNull();
            assertThat(result.newImages().toList()).hasSize(2);
            assertThat(result.updatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("수정 데이터의 이미지 URL이 커맨드와 일치한다")
        void createUpdateData_ImageUrlMatchesCommand() {
            // given
            UpdateProductGroupImagesCommand command =
                    ProductGroupImageCommandFixtures.updateCommand();
            Instant now = CommonVoFixtures.now();
            given(timeProvider.now()).willReturn(now);

            // when
            ProductGroupImageUpdateData result = sut.createUpdateData(command);

            // then
            assertThat(result.newImages().thumbnail().imageUrlValue())
                    .isEqualTo(ProductGroupImageCommandFixtures.UPDATED_THUMBNAIL_URL);
        }

        @Test
        @DisplayName("썸네일만 있는 수정 커맨드로 UpdateData를 생성한다")
        void createUpdateData_ThumbnailOnly_ReturnsUpdateData() {
            // given
            UpdateProductGroupImagesCommand command =
                    ProductGroupImageCommandFixtures.updateThumbnailOnlyCommand();
            Instant now = CommonVoFixtures.now();
            given(timeProvider.now()).willReturn(now);

            // when
            ProductGroupImageUpdateData result = sut.createUpdateData(command);

            // then
            assertThat(result).isNotNull();
            assertThat(result.newImages().toList()).hasSize(1);
            assertThat(result.newImages().detailImages()).isEmpty();
        }
    }
}
