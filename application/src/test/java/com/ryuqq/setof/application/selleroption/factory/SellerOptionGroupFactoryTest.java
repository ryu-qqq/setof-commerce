package com.ryuqq.setof.application.selleroption.factory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.ryuqq.setof.application.common.time.TimeProvider;
import com.ryuqq.setof.application.selleroption.SellerOptionCommandFixtures;
import com.ryuqq.setof.application.selleroption.dto.command.UpdateSellerOptionGroupsCommand;
import com.ryuqq.setof.domain.common.CommonVoFixtures;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import com.ryuqq.setof.domain.productgroup.vo.SellerOptionGroupUpdateData;
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

@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("SellerOptionGroupFactory 단위 테스트")
class SellerOptionGroupFactoryTest {

    @InjectMocks private SellerOptionGroupFactory sut;

    @Mock private TimeProvider timeProvider;

    @Nested
    @DisplayName(
            "toUpdateData() - UpdateSellerOptionGroupsCommand를 SellerOptionGroupUpdateData로 변환")
    class ToUpdateDataTest {

        @Test
        @DisplayName("기존 그룹 ID가 있는 커맨드를 SellerOptionGroupUpdateData로 변환한다")
        void toUpdateData_WithExistingGroupId_ReturnsUpdateData() {
            // given
            ProductGroupId productGroupId = ProductGroupId.of(1L);
            List<UpdateSellerOptionGroupsCommand.OptionGroupCommand> optionGroups =
                    SellerOptionCommandFixtures.updateCommand().optionGroups();
            Instant now = CommonVoFixtures.now();

            given(timeProvider.now()).willReturn(now);

            // when
            SellerOptionGroupUpdateData result = sut.toUpdateData(productGroupId, optionGroups);

            // then
            assertThat(result).isNotNull();
            assertThat(result.productGroupId()).isEqualTo(productGroupId);
            assertThat(result.groupEntries()).hasSize(1);
            assertThat(result.updatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("기존 그룹 ID가 있는 엔트리의 optionGroupName이 올바르게 매핑된다")
        void toUpdateData_MapsOptionGroupNameCorrectly() {
            // given
            ProductGroupId productGroupId = ProductGroupId.of(1L);
            List<UpdateSellerOptionGroupsCommand.OptionGroupCommand> optionGroups =
                    SellerOptionCommandFixtures.updateCommand().optionGroups();
            Instant now = CommonVoFixtures.now();

            given(timeProvider.now()).willReturn(now);

            // when
            SellerOptionGroupUpdateData result = sut.toUpdateData(productGroupId, optionGroups);

            // then
            SellerOptionGroupUpdateData.GroupEntry entry = result.groupEntries().get(0);
            assertThat(entry.sellerOptionGroupId()).isEqualTo(10L);
            assertThat(entry.optionGroupName()).isEqualTo("색상");
            assertThat(entry.values()).hasSize(2);
        }

        @Test
        @DisplayName("신규 그룹(ID null)을 포함하는 커맨드를 SellerOptionGroupUpdateData로 변환한다")
        void toUpdateData_WithNewGroup_ReturnsUpdateDataWithNullGroupId() {
            // given
            ProductGroupId productGroupId = ProductGroupId.of(1L);
            List<UpdateSellerOptionGroupsCommand.OptionGroupCommand> optionGroups =
                    SellerOptionCommandFixtures.updateCommandWithNewGroup().optionGroups();
            Instant now = CommonVoFixtures.now();

            given(timeProvider.now()).willReturn(now);

            // when
            SellerOptionGroupUpdateData result = sut.toUpdateData(productGroupId, optionGroups);

            // then
            assertThat(result.groupEntries()).hasSize(1);
            assertThat(result.groupEntries().get(0).sellerOptionGroupId()).isNull();
        }

        @Test
        @DisplayName("옵션 값의 sortOrder가 올바르게 매핑된다")
        void toUpdateData_MapsValueSortOrderCorrectly() {
            // given
            ProductGroupId productGroupId = ProductGroupId.of(1L);
            List<UpdateSellerOptionGroupsCommand.OptionGroupCommand> optionGroups =
                    SellerOptionCommandFixtures.updateCommand().optionGroups();
            Instant now = CommonVoFixtures.now();

            given(timeProvider.now()).willReturn(now);

            // when
            SellerOptionGroupUpdateData result = sut.toUpdateData(productGroupId, optionGroups);

            // then
            SellerOptionGroupUpdateData.GroupEntry entry = result.groupEntries().get(0);
            assertThat(entry.values().get(0).sellerOptionValueId()).isEqualTo(100L);
            assertThat(entry.values().get(0).optionValueName()).isEqualTo("검정");
            assertThat(entry.values().get(0).sortOrder()).isEqualTo(1);
        }

        @Test
        @DisplayName("빈 optionGroups 목록은 빈 groupEntries를 반환한다")
        void toUpdateData_EmptyGroups_ReturnsEmptyEntries() {
            // given
            ProductGroupId productGroupId = ProductGroupId.of(1L);
            Instant now = CommonVoFixtures.now();

            given(timeProvider.now()).willReturn(now);

            // when
            SellerOptionGroupUpdateData result = sut.toUpdateData(productGroupId, List.of());

            // then
            assertThat(result.groupEntries()).isEmpty();
        }
    }
}
