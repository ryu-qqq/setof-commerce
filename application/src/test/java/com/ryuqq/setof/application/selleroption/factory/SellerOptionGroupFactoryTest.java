package com.ryuqq.setof.application.selleroption.factory;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.application.common.time.TimeProvider;
import com.ryuqq.setof.application.productgroup.dto.command.RegisterProductGroupCommand;
import com.ryuqq.setof.domain.productgroup.aggregate.SellerOptionGroup;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
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
    @DisplayName("createNewGroups() - 신규 옵션 그룹 생성")
    class CreateNewGroupsTest {

        @Test
        @DisplayName("커맨드로부터 SellerOptionGroup 도메인 객체 목록을 생성한다")
        void createNewGroups_ValidCommands_CreatesGroups() {
            // given
            ProductGroupId pgId = ProductGroupId.of(1L);
            List<RegisterProductGroupCommand.OptionGroupCommand> commands =
                    List.of(
                            new RegisterProductGroupCommand.OptionGroupCommand(
                                    "색상",
                                    0,
                                    List.of(
                                            new RegisterProductGroupCommand.OptionValueCommand(
                                                    "레드", 0),
                                            new RegisterProductGroupCommand.OptionValueCommand(
                                                    "블루", 1))),
                            new RegisterProductGroupCommand.OptionGroupCommand(
                                    "사이즈",
                                    1,
                                    List.of(
                                            new RegisterProductGroupCommand.OptionValueCommand(
                                                    "S", 0))));

            // when
            List<SellerOptionGroup> result = sut.createNewGroups(pgId, commands);

            // then
            assertThat(result).hasSize(2);
            assertThat(result.get(0).optionGroupNameValue()).isEqualTo("색상");
            assertThat(result.get(0).optionValues()).hasSize(2);
            assertThat(result.get(0).optionValues().get(0).optionValueNameValue()).isEqualTo("레드");
            assertThat(result.get(0).optionValues().get(1).optionValueNameValue()).isEqualTo("블루");
            assertThat(result.get(1).optionGroupNameValue()).isEqualTo("사이즈");
            assertThat(result.get(1).optionValues()).hasSize(1);
        }

        @Test
        @DisplayName("생성된 그룹의 sortOrder가 커맨드와 동일하다")
        void createNewGroups_SortOrderPreserved() {
            // given
            ProductGroupId pgId = ProductGroupId.of(1L);
            List<RegisterProductGroupCommand.OptionGroupCommand> commands =
                    List.of(
                            new RegisterProductGroupCommand.OptionGroupCommand(
                                    "색상",
                                    3,
                                    List.of(
                                            new RegisterProductGroupCommand.OptionValueCommand(
                                                    "레드", 5))));

            // when
            List<SellerOptionGroup> result = sut.createNewGroups(pgId, commands);

            // then
            assertThat(result.get(0).sortOrder()).isEqualTo(3);
            assertThat(result.get(0).optionValues().get(0).sortOrder()).isEqualTo(5);
        }

        @Test
        @DisplayName("생성된 그룹의 productGroupId가 올바르다")
        void createNewGroups_ProductGroupIdMatches() {
            // given
            ProductGroupId pgId = ProductGroupId.of(99L);
            List<RegisterProductGroupCommand.OptionGroupCommand> commands =
                    List.of(
                            new RegisterProductGroupCommand.OptionGroupCommand(
                                    "색상",
                                    0,
                                    List.of(
                                            new RegisterProductGroupCommand.OptionValueCommand(
                                                    "레드", 0))));

            // when
            List<SellerOptionGroup> result = sut.createNewGroups(pgId, commands);

            // then
            assertThat(result.get(0).productGroupIdValue()).isEqualTo(99L);
        }
    }
}
