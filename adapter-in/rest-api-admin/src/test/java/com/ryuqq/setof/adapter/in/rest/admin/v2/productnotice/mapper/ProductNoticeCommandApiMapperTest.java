package com.ryuqq.setof.adapter.in.rest.admin.v2.productnotice.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.in.rest.admin.productnotice.ProductNoticeApiFixtures;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productnotice.dto.command.RegisterProductNoticeApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productnotice.dto.command.UpdateProductNoticeApiRequest;
import com.ryuqq.setof.application.productnotice.dto.command.RegisterProductNoticeCommand;
import com.ryuqq.setof.application.productnotice.dto.command.UpdateProductNoticeCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * ProductNoticeCommandApiMapper 단위 테스트.
 *
 * <p>Command API Mapper의 변환 로직을 테스트합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@DisplayName("ProductNoticeCommandApiMapper 단위 테스트")
class ProductNoticeCommandApiMapperTest {

    private ProductNoticeCommandApiMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ProductNoticeCommandApiMapper();
    }

    @Nested
    @DisplayName("toRegisterCommand(Long, RegisterProductNoticeApiRequest)")
    class ToRegisterCommandTest {

        @Test
        @DisplayName("등록 요청을 RegisterProductNoticeCommand로 변환한다")
        void toRegisterCommand_Success() {
            // given
            Long productGroupId = ProductNoticeApiFixtures.DEFAULT_PRODUCT_GROUP_ID;
            RegisterProductNoticeApiRequest request = ProductNoticeApiFixtures.registerRequest();

            // when
            RegisterProductNoticeCommand command =
                    mapper.toRegisterCommand(productGroupId, request);

            // then
            assertThat(command.productGroupId()).isEqualTo(productGroupId);
            assertThat(command.entries()).hasSize(1);

            RegisterProductNoticeCommand.NoticeEntryCommand entry = command.entries().get(0);
            RegisterProductNoticeApiRequest.NoticeEntryApiRequest requestEntry =
                    request.entries().get(0);

            assertThat(entry.noticeFieldId()).isEqualTo(requestEntry.noticeFieldId());
            assertThat(entry.fieldName()).isEqualTo(requestEntry.fieldName());
            assertThat(entry.fieldValue()).isEqualTo(requestEntry.fieldValue());
        }

        @Test
        @DisplayName("여러 항목이 있는 등록 요청을 Command로 변환한다")
        void toRegisterCommand_MultipleEntries_Success() {
            // given
            Long productGroupId = ProductNoticeApiFixtures.DEFAULT_PRODUCT_GROUP_ID;
            RegisterProductNoticeApiRequest request =
                    ProductNoticeApiFixtures.registerRequest(
                            java.util.List.of(
                                    ProductNoticeApiFixtures.registerNoticeEntryRequest(
                                            1L, "소재", "면 100%"),
                                    ProductNoticeApiFixtures.registerNoticeEntryRequest(
                                            2L, "세탁방법", "손세탁")));

            // when
            RegisterProductNoticeCommand command =
                    mapper.toRegisterCommand(productGroupId, request);

            // then
            assertThat(command.productGroupId()).isEqualTo(productGroupId);
            assertThat(command.entries()).hasSize(2);
            assertThat(command.entries().get(0).noticeFieldId()).isEqualTo(1L);
            assertThat(command.entries().get(0).fieldName()).isEqualTo("소재");
            assertThat(command.entries().get(0).fieldValue()).isEqualTo("면 100%");
            assertThat(command.entries().get(1).noticeFieldId()).isEqualTo(2L);
            assertThat(command.entries().get(1).fieldName()).isEqualTo("세탁방법");
            assertThat(command.entries().get(1).fieldValue()).isEqualTo("손세탁");
        }
    }

    @Nested
    @DisplayName("toUpdateCommand(Long, UpdateProductNoticeApiRequest)")
    class ToUpdateCommandTest {

        @Test
        @DisplayName("수정 요청을 UpdateProductNoticeCommand로 변환한다")
        void toUpdateCommand_Success() {
            // given
            Long productGroupId = ProductNoticeApiFixtures.DEFAULT_PRODUCT_GROUP_ID;
            UpdateProductNoticeApiRequest request = ProductNoticeApiFixtures.updateRequest();

            // when
            UpdateProductNoticeCommand command = mapper.toUpdateCommand(productGroupId, request);

            // then
            assertThat(command.productGroupId()).isEqualTo(productGroupId);
            assertThat(command.entries()).hasSize(1);

            UpdateProductNoticeCommand.NoticeEntryCommand entry = command.entries().get(0);
            UpdateProductNoticeApiRequest.NoticeEntryApiRequest requestEntry =
                    request.entries().get(0);

            assertThat(entry.noticeFieldId()).isEqualTo(requestEntry.noticeFieldId());
            assertThat(entry.fieldName()).isEqualTo(requestEntry.fieldName());
            assertThat(entry.fieldValue()).isEqualTo(requestEntry.fieldValue());
        }

        @Test
        @DisplayName("여러 항목이 있는 수정 요청을 Command로 변환한다")
        void toUpdateCommand_MultipleEntries_Success() {
            // given
            Long productGroupId = ProductNoticeApiFixtures.DEFAULT_PRODUCT_GROUP_ID;
            UpdateProductNoticeApiRequest request =
                    ProductNoticeApiFixtures.updateRequest(
                            java.util.List.of(
                                    ProductNoticeApiFixtures.updateNoticeEntryRequest(),
                                    new UpdateProductNoticeApiRequest.NoticeEntryApiRequest(
                                            2L, "세탁방법", "드라이클리닝")));

            // when
            UpdateProductNoticeCommand command = mapper.toUpdateCommand(productGroupId, request);

            // then
            assertThat(command.productGroupId()).isEqualTo(productGroupId);
            assertThat(command.entries()).hasSize(2);
        }
    }
}
