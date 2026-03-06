package com.ryuqq.setof.application.productnotice.factory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.ryuqq.setof.application.common.time.TimeProvider;
import com.ryuqq.setof.application.productnotice.ProductNoticeCommandFixtures;
import com.ryuqq.setof.application.productnotice.dto.command.RegisterProductNoticeCommand;
import com.ryuqq.setof.application.productnotice.dto.command.UpdateProductNoticeCommand;
import com.ryuqq.setof.domain.common.CommonVoFixtures;
import com.ryuqq.setof.domain.productnotice.aggregate.ProductNotice;
import com.ryuqq.setof.domain.productnotice.vo.ProductNoticeUpdateData;
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
@DisplayName("ProductNoticeCommandFactory 단위 테스트")
class ProductNoticeCommandFactoryTest {

    @InjectMocks private ProductNoticeCommandFactory sut;

    @Mock private TimeProvider timeProvider;

    @Nested
    @DisplayName("create() - 신규 ProductNotice 생성")
    class CreateTest {

        @Test
        @DisplayName("RegisterProductNoticeCommand로 ProductNotice를 생성한다")
        void create_ValidCommand_ReturnsProductNotice() {
            // given
            RegisterProductNoticeCommand command = ProductNoticeCommandFixtures.registerCommand();
            Instant now = CommonVoFixtures.now();
            given(timeProvider.now()).willReturn(now);

            // when
            ProductNotice result = sut.create(command);

            // then
            assertThat(result).isNotNull();
            assertThat(result.isNew()).isTrue();
            assertThat(result.productGroupIdValue()).isEqualTo(command.productGroupId());
            assertThat(result.entries()).hasSize(command.entries().size());
        }

        @Test
        @DisplayName("엔트리가 없는 커맨드로도 ProductNotice를 생성한다")
        void create_EmptyEntries_ReturnsProductNotice() {
            // given
            RegisterProductNoticeCommand command =
                    ProductNoticeCommandFixtures.registerCommandWithEmptyEntries();
            Instant now = CommonVoFixtures.now();
            given(timeProvider.now()).willReturn(now);

            // when
            ProductNotice result = sut.create(command);

            // then
            assertThat(result).isNotNull();
            assertThat(result.isNew()).isTrue();
            assertThat(result.isEmpty()).isTrue();
        }

        @Test
        @DisplayName("생성된 ProductNotice의 엔트리 필드명이 커맨드와 일치한다")
        void create_ValidCommand_EntryFieldNamesMatch() {
            // given
            RegisterProductNoticeCommand command = ProductNoticeCommandFixtures.registerCommand();
            Instant now = CommonVoFixtures.now();
            given(timeProvider.now()).willReturn(now);

            // when
            ProductNotice result = sut.create(command);

            // then
            assertThat(result.entries()).hasSize(4);
            assertThat(result.entries().get(0).fieldName()).isEqualTo("소재");
            assertThat(result.entries().get(1).fieldName()).isEqualTo("세탁방법");
        }
    }

    @Nested
    @DisplayName("createUpdateData() - ProductNoticeUpdateData 생성")
    class CreateUpdateDataTest {

        @Test
        @DisplayName("UpdateProductNoticeCommand로 ProductNoticeUpdateData를 생성한다")
        void createUpdateData_ValidCommand_ReturnsUpdateData() {
            // given
            UpdateProductNoticeCommand command = ProductNoticeCommandFixtures.updateCommand();
            Instant now = CommonVoFixtures.now();
            given(timeProvider.now()).willReturn(now);

            // when
            ProductNoticeUpdateData result = sut.createUpdateData(command);

            // then
            assertThat(result).isNotNull();
            assertThat(result.entries()).hasSize(command.entries().size());
            assertThat(result.updatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("UpdateData의 엔트리 필드명이 커맨드와 일치한다")
        void createUpdateData_ValidCommand_EntryFieldNamesMatch() {
            // given
            UpdateProductNoticeCommand command = ProductNoticeCommandFixtures.updateCommand();
            Instant now = CommonVoFixtures.now();
            given(timeProvider.now()).willReturn(now);

            // when
            ProductNoticeUpdateData result = sut.createUpdateData(command);

            // then
            assertThat(result.entries()).hasSize(3);
            assertThat(result.entries().get(0).fieldName()).isEqualTo("소재");
            assertThat(result.entries().get(0).fieldValueText()).isEqualTo("폴리에스터 100%");
        }
    }
}
