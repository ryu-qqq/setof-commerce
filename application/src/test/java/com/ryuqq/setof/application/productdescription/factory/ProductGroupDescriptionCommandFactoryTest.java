package com.ryuqq.setof.application.productdescription.factory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.ryuqq.setof.application.common.time.TimeProvider;
import com.ryuqq.setof.application.productdescription.dto.command.RegisterProductGroupDescriptionCommand;
import com.ryuqq.setof.application.productdescription.dto.command.RegisterProductGroupDescriptionCommand.DescriptionImageCommand;
import com.ryuqq.setof.application.productdescription.dto.command.UpdateProductGroupDescriptionCommand;
import com.ryuqq.setof.domain.productdescription.aggregate.DescriptionImage;
import com.ryuqq.setof.domain.productdescription.aggregate.ProductGroupDescription;
import com.ryuqq.setof.domain.productdescription.vo.DescriptionHtml;
import com.ryuqq.setof.domain.productdescription.vo.DescriptionUpdateData;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * ProductGroupDescriptionCommandFactory 단위 테스트.
 *
 * <p>커맨드 이미지 우선 사용 + content HTML 자동 추출 폴백을 검증합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("ProductGroupDescriptionCommandFactory 단위 테스트")
class ProductGroupDescriptionCommandFactoryTest {

    @InjectMocks private ProductGroupDescriptionCommandFactory sut;

    @Mock private TimeProvider timeProvider;

    // ========================================================================
    // 1. createNewDescription 테스트
    // ========================================================================

    @Nested
    @DisplayName("createNewDescription 테스트")
    class CreateNewDescriptionTest {

        @Test
        @DisplayName("커맨드로부터 ProductGroupDescription을 생성합니다")
        void createNewDescription_ReturnsDescription() {
            // given
            Instant now = Instant.now();
            given(timeProvider.now()).willReturn(now);
            RegisterProductGroupDescriptionCommand command =
                    new RegisterProductGroupDescriptionCommand(100L, "<p>상세설명</p>", null);

            // when
            ProductGroupDescription result = sut.createNewDescription(command);

            // then
            assertThat(result).isNotNull();
            assertThat(result.contentValue()).isEqualTo("<p>상세설명</p>");
        }
    }

    // ========================================================================
    // 2. createNewImages 테스트
    // ========================================================================

    @Nested
    @DisplayName("createNewImages 테스트")
    class CreateNewImagesTest {

        @Test
        @DisplayName("커맨드에 이미지가 있으면 커맨드 이미지를 우선 사용합니다")
        void createNewImages_WithCommands_UsesCommands() {
            // given
            List<DescriptionImageCommand> commands =
                    List.of(
                            new DescriptionImageCommand("https://cmd.example.com/1.jpg", 0),
                            new DescriptionImageCommand("https://cmd.example.com/2.jpg", 1));
            DescriptionHtml content =
                    DescriptionHtml.of("<p><img src=\"https://html.example.com/ignored.jpg\"></p>");

            // when
            List<DescriptionImage> result = sut.createNewImages(commands, content);

            // then
            assertThat(result).hasSize(2);
            assertThat(result.get(0).imageUrl()).isEqualTo("https://cmd.example.com/1.jpg");
            assertThat(result.get(1).imageUrl()).isEqualTo("https://cmd.example.com/2.jpg");
        }

        @Test
        @DisplayName("커맨드 이미지가 null이면 content HTML에서 img 태그를 추출합니다")
        void createNewImages_WithNullCommands_ExtractsFromContent() {
            // given
            DescriptionHtml content =
                    DescriptionHtml.of(
                            "<p>설명</p>"
                                    + "<img src=\"https://example.com/image1.jpg\">"
                                    + "<p>중간 텍스트</p>"
                                    + "<img src=\"https://example.com/image2.jpg\">"
                                    + "<img src=\"https://example.com/image3.jpg\">");

            // when
            List<DescriptionImage> result = sut.createNewImages(null, content);

            // then
            assertThat(result).hasSize(3);
            assertThat(result.get(0).imageUrl()).isEqualTo("https://example.com/image1.jpg");
            assertThat(result.get(0).sortOrder()).isZero();
            assertThat(result.get(1).imageUrl()).isEqualTo("https://example.com/image2.jpg");
            assertThat(result.get(1).sortOrder()).isEqualTo(1);
            assertThat(result.get(2).imageUrl()).isEqualTo("https://example.com/image3.jpg");
            assertThat(result.get(2).sortOrder()).isEqualTo(2);
        }

        @Test
        @DisplayName("커맨드 이미지가 빈 리스트이면 content HTML에서 추출합니다")
        void createNewImages_WithEmptyCommands_ExtractsFromContent() {
            // given
            DescriptionHtml content =
                    DescriptionHtml.of("<img src=\"https://example.com/only.jpg\">");

            // when
            List<DescriptionImage> result = sut.createNewImages(List.of(), content);

            // then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).imageUrl()).isEqualTo("https://example.com/only.jpg");
        }

        @Test
        @DisplayName("커맨드 이미지도 없고 content에 img 태그도 없으면 빈 리스트를 반환합니다")
        void createNewImages_WithNoImagesAnywhere_ReturnsEmpty() {
            // given
            DescriptionHtml content = DescriptionHtml.of("<p>이미지 없는 설명</p>");

            // when
            List<DescriptionImage> result = sut.createNewImages(null, content);

            // then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("다양한 img 태그 형식을 추출합니다")
        void createNewImages_WithVariousImgFormats_ExtractsAll() {
            // given
            DescriptionHtml content =
                    DescriptionHtml.of(
                            "<img src=\"https://example.com/double-quote.jpg\">"
                                    + "<img src='https://example.com/single-quote.jpg'>"
                                    + "<IMG SRC=\"https://example.com/uppercase.jpg\">");

            // when
            List<DescriptionImage> result = sut.createNewImages(null, content);

            // then
            assertThat(result).hasSize(3);
            assertThat(result.get(0).imageUrl()).isEqualTo("https://example.com/double-quote.jpg");
            assertThat(result.get(1).imageUrl()).isEqualTo("https://example.com/single-quote.jpg");
            assertThat(result.get(2).imageUrl()).isEqualTo("https://example.com/uppercase.jpg");
        }
    }

    // ========================================================================
    // 3. createUpdateData 테스트
    // ========================================================================

    @Nested
    @DisplayName("createUpdateData 테스트")
    class CreateUpdateDataTest {

        @Test
        @DisplayName("커맨드에 이미지가 있으면 커맨드 이미지를 우선 사용합니다")
        void createUpdateData_WithCommands_UsesCommands() {
            // given
            Instant now = Instant.now();
            given(timeProvider.now()).willReturn(now);

            List<UpdateProductGroupDescriptionCommand.DescriptionImageCommand> images =
                    List.of(
                            new UpdateProductGroupDescriptionCommand.DescriptionImageCommand(
                                    "https://cmd.example.com/update.jpg", 0));
            UpdateProductGroupDescriptionCommand command =
                    new UpdateProductGroupDescriptionCommand(
                            100L,
                            "<p><img src=\"https://html.example.com/ignored.jpg\"></p>",
                            images);

            // when
            DescriptionUpdateData result = sut.createUpdateData(command);

            // then
            assertThat(result.newImages()).hasSize(1);
            assertThat(result.newImages().get(0).imageUrl())
                    .isEqualTo("https://cmd.example.com/update.jpg");
        }

        @Test
        @DisplayName("커맨드 이미지가 null이면 content HTML에서 추출합니다")
        void createUpdateData_WithNullCommands_ExtractsFromContent() {
            // given
            Instant now = Instant.now();
            given(timeProvider.now()).willReturn(now);

            UpdateProductGroupDescriptionCommand command =
                    new UpdateProductGroupDescriptionCommand(
                            100L,
                            "<img src=\"https://example.com/extracted1.jpg\">"
                                    + "<img src=\"https://example.com/extracted2.jpg\">",
                            null);

            // when
            DescriptionUpdateData result = sut.createUpdateData(command);

            // then
            assertThat(result.newImages()).hasSize(2);
            assertThat(result.newImages().get(0).imageUrl())
                    .isEqualTo("https://example.com/extracted1.jpg");
            assertThat(result.newImages().get(0).sortOrder()).isZero();
            assertThat(result.newImages().get(1).imageUrl())
                    .isEqualTo("https://example.com/extracted2.jpg");
            assertThat(result.newImages().get(1).sortOrder()).isEqualTo(1);
        }

        @Test
        @DisplayName("커맨드 이미지가 빈 리스트이고 content에도 img가 없으면 빈 이미지 목록입니다")
        void createUpdateData_WithNoImages_ReturnsEmptyImages() {
            // given
            Instant now = Instant.now();
            given(timeProvider.now()).willReturn(now);

            UpdateProductGroupDescriptionCommand command =
                    new UpdateProductGroupDescriptionCommand(100L, "<p>텍스트만</p>", List.of());

            // when
            DescriptionUpdateData result = sut.createUpdateData(command);

            // then
            assertThat(result.newImages()).isEmpty();
        }
    }
}
