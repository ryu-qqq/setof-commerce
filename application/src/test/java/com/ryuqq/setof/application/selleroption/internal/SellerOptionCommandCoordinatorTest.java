package com.ryuqq.setof.application.selleroption.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.productgroup.dto.command.RegisterProductGroupCommand;
import com.ryuqq.setof.application.selleroption.dto.command.UpdateSellerOptionGroupsCommand;
import com.ryuqq.setof.application.selleroption.dto.result.SellerOptionUpdateResult;
import com.ryuqq.setof.application.selleroption.factory.SellerOptionGroupFactory;
import com.ryuqq.setof.application.selleroption.manager.SellerOptionGroupReadManager;
import com.ryuqq.setof.domain.productgroup.aggregate.SellerOptionGroup;
import com.ryuqq.setof.domain.productgroup.aggregate.SellerOptionValue;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import com.ryuqq.setof.domain.productgroup.id.SellerOptionGroupId;
import com.ryuqq.setof.domain.productgroup.id.SellerOptionValueId;
import com.ryuqq.setof.domain.productgroup.vo.OptionGroupName;
import com.ryuqq.setof.domain.productgroup.vo.OptionValueName;
import com.ryuqq.setof.domain.productgroup.vo.SellerOptionGroupDiff;
import com.ryuqq.setof.domain.productgroup.vo.SellerOptionGroupUpdateData;
import com.ryuqq.setof.domain.productgroup.vo.SellerOptionGroups;
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
@DisplayName("SellerOptionCommandCoordinator 단위 테스트")
class SellerOptionCommandCoordinatorTest {

    @InjectMocks private SellerOptionCommandCoordinator sut;

    @Mock private SellerOptionGroupFactory optionGroupFactory;
    @Mock private SellerOptionGroupReadManager readManager;
    @Mock private SellerOptionPersistFacade persistFacade;

    @Nested
    @DisplayName("register() - 신규 옵션 그룹 등록")
    class RegisterTest {

        @Test
        @DisplayName("옵션 그룹 커맨드로 도메인 객체를 생성하고 영속화한다")
        void register_ValidCommands_CreatesGroupsAndPersists() {
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
                                                    "블루", 1))));

            List<SellerOptionGroup> groups =
                    List.of(
                            SellerOptionGroup.forNew(
                                    pgId,
                                    OptionGroupName.of("색상"),
                                    0,
                                    List.of(
                                            SellerOptionValue.forNew(
                                                    SellerOptionGroupId.forNew(),
                                                    OptionValueName.of("레드"),
                                                    0),
                                            SellerOptionValue.forNew(
                                                    SellerOptionGroupId.forNew(),
                                                    OptionValueName.of("블루"),
                                                    1))));

            List<SellerOptionValueId> expectedIds =
                    List.of(SellerOptionValueId.of(100L), SellerOptionValueId.of(101L));

            given(optionGroupFactory.createNewGroups(pgId, commands)).willReturn(groups);
            given(persistFacade.persistAll(groups)).willReturn(expectedIds);

            // when
            List<SellerOptionValueId> result = sut.register(pgId, commands);

            // then
            assertThat(result).isEqualTo(expectedIds);
            then(optionGroupFactory).should().createNewGroups(pgId, commands);
            then(persistFacade).should().persistAll(groups);
        }

        @Test
        @DisplayName("빈 커맨드 목록이면 빈 리스트를 반환한다")
        void register_EmptyCommands_ReturnsEmptyList() {
            // given
            ProductGroupId pgId = ProductGroupId.of(1L);

            // when
            List<SellerOptionValueId> result = sut.register(pgId, List.of());

            // then
            assertThat(result).isEmpty();
            then(optionGroupFactory).shouldHaveNoInteractions();
            then(persistFacade).shouldHaveNoInteractions();
        }

        @Test
        @DisplayName("null 커맨드 목록이면 빈 리스트를 반환한다")
        void register_NullCommands_ReturnsEmptyList() {
            // given
            ProductGroupId pgId = ProductGroupId.of(1L);

            // when
            List<SellerOptionValueId> result = sut.register(pgId, null);

            // then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("update() - 옵션 그룹 수정")
    class UpdateTest {

        @Test
        @DisplayName("기존 옵션을 조회하고 diff 기반으로 영속화한다")
        void update_ValidCommand_QueriesExistingAndPersistsDiff() {
            // given
            ProductGroupId pgId = ProductGroupId.of(1L);
            UpdateSellerOptionGroupsCommand command =
                    new UpdateSellerOptionGroupsCommand(1L, List.of());

            SellerOptionGroupUpdateData updateData =
                    SellerOptionGroupUpdateData.of(pgId, List.of(), Instant.now());
            SellerOptionGroups existing = SellerOptionGroups.reconstitute(List.of());
            SellerOptionGroupDiff diff = existing.update(updateData);

            given(optionGroupFactory.toUpdateData(pgId, command.optionGroups()))
                    .willReturn(updateData);
            given(readManager.getByProductGroupId(pgId)).willReturn(existing);
            given(persistFacade.persistDiff(diff)).willReturn(List.of());

            // when
            SellerOptionUpdateResult result = sut.update(command);

            // then
            assertThat(result).isNotNull();
            then(readManager).should().getByProductGroupId(pgId);
        }
    }
}
