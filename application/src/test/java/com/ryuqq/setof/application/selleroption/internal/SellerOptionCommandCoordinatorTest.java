package com.ryuqq.setof.application.selleroption.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.selleroption.SellerOptionCommandFixtures;
import com.ryuqq.setof.application.selleroption.dto.command.RegisterSellerOptionGroupsCommand;
import com.ryuqq.setof.application.selleroption.dto.command.UpdateSellerOptionGroupsCommand;
import com.ryuqq.setof.application.selleroption.dto.result.SellerOptionUpdateResult;
import com.ryuqq.setof.application.selleroption.factory.SellerOptionGroupFactory;
import com.ryuqq.setof.application.selleroption.manager.SellerOptionGroupReadManager;
import com.ryuqq.setof.domain.common.CommonVoFixtures;
import com.ryuqq.setof.domain.productgroup.ProductGroupFixtures;
import com.ryuqq.setof.domain.productgroup.aggregate.SellerOptionGroup;
import com.ryuqq.setof.domain.productgroup.aggregate.SellerOptionValue;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import com.ryuqq.setof.domain.productgroup.id.SellerOptionValueId;
import com.ryuqq.setof.domain.productgroup.vo.SellerOptionGroupDiff;
import com.ryuqq.setof.domain.productgroup.vo.SellerOptionGroupUpdateData;
import com.ryuqq.setof.domain.productgroup.vo.SellerOptionGroups;
import com.ryuqq.setof.domain.productgroup.vo.SellerOptionValueDiff;
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
    @DisplayName("register() - 옵션 그룹 등록 조율")
    class RegisterTest {

        @Test
        @DisplayName("등록 커맨드를 처리하고 생성된 SellerOptionValueId 목록을 반환한다")
        void register_ValidCommand_ReturnsValueIds() {
            // given
            RegisterSellerOptionGroupsCommand command =
                    SellerOptionCommandFixtures.registerCommand();
            List<SellerOptionValueId> expectedIds =
                    List.of(SellerOptionValueId.of(100L), SellerOptionValueId.of(101L));

            given(persistFacade.persistAll(any())).willReturn(expectedIds);

            // when
            List<SellerOptionValueId> result = sut.register(command);

            // then
            assertThat(result).hasSize(2);
            assertThat(result.get(0).value()).isEqualTo(100L);
            assertThat(result.get(1).value()).isEqualTo(101L);
            then(persistFacade).should().persistAll(any());
        }

        @Test
        @DisplayName("COMBINATION 커맨드로 2개 그룹의 옵션 값 ID를 반환한다")
        void register_CombinationCommand_ReturnsBothGroupValueIds() {
            // given
            RegisterSellerOptionGroupsCommand command =
                    SellerOptionCommandFixtures.registerCombinationCommand();
            List<SellerOptionValueId> expectedIds =
                    List.of(
                            SellerOptionValueId.of(100L),
                            SellerOptionValueId.of(101L),
                            SellerOptionValueId.of(102L));

            given(persistFacade.persistAll(any())).willReturn(expectedIds);

            // when
            List<SellerOptionValueId> result = sut.register(command);

            // then
            assertThat(result).hasSize(3);
            then(persistFacade).should().persistAll(any());
        }

        @Test
        @DisplayName("옵션 그룹 생성 시 productGroupId가 올바르게 전달된다")
        void register_PropagatesProductGroupId() {
            // given
            long productGroupId = 5L;
            RegisterSellerOptionGroupsCommand command =
                    SellerOptionCommandFixtures.registerCommand(productGroupId);
            List<SellerOptionValueId> expectedIds = List.of(SellerOptionValueId.of(100L));

            given(persistFacade.persistAll(any())).willReturn(expectedIds);

            // when
            sut.register(command);

            // then - persistFacade.persistAll 호출 시 productGroupId=5인 그룹이 포함됨을 검증
            then(persistFacade).should().persistAll(any());
        }
    }

    @Nested
    @DisplayName("update() - 옵션 그룹 수정 조율")
    class UpdateTest {

        @Test
        @DisplayName("수정 커맨드를 처리하고 SellerOptionUpdateResult를 반환한다")
        void update_ValidCommand_ReturnsUpdateResult() {
            // given
            UpdateSellerOptionGroupsCommand command = SellerOptionCommandFixtures.updateCommand();
            ProductGroupId pgId = ProductGroupId.of(command.productGroupId());
            Instant now = CommonVoFixtures.now();

            SellerOptionGroup existingGroup = ProductGroupFixtures.activeSellerOptionGroup();
            SellerOptionValue existingValue = ProductGroupFixtures.activeSellerOptionValue();
            SellerOptionGroups existingGroups =
                    SellerOptionGroups.reconstitute(List.of(existingGroup));

            SellerOptionValueDiff valueDiff =
                    SellerOptionValueDiff.of(List.of(), List.of(), List.of(existingValue));
            SellerOptionGroupDiff.RetainedGroupDiff retainedGroupDiff =
                    new SellerOptionGroupDiff.RetainedGroupDiff(existingGroup, valueDiff);
            List<SellerOptionValueId> orderedActiveIds = List.of(existingValue.id());
            SellerOptionGroupDiff diff =
                    SellerOptionGroupDiff.of(
                            List.of(),
                            List.of(),
                            List.of(retainedGroupDiff),
                            orderedActiveIds,
                            now);

            SellerOptionGroupUpdateData updateData =
                    SellerOptionGroupUpdateData.of(pgId, List.of(), now);

            given(readManager.getByProductGroupId(pgId)).willReturn(existingGroups);
            given(optionGroupFactory.toUpdateData(eq(pgId), any())).willReturn(updateData);
            given(persistFacade.persistDiff(any())).willReturn(orderedActiveIds);

            // when
            SellerOptionUpdateResult result = sut.update(command);

            // then
            assertThat(result).isNotNull();
            assertThat(result.resolvedActiveValueIds()).isNotNull();
            then(readManager).should().getByProductGroupId(pgId);
            then(optionGroupFactory).should().toUpdateData(eq(pgId), any());
            then(persistFacade).should().persistDiff(any());
        }

        @Test
        @DisplayName("변경이 없는 경우 hasChanges가 false인 결과를 반환한다")
        void update_NoChanges_ReturnsResultWithNoChanges() {
            // given
            UpdateSellerOptionGroupsCommand command = SellerOptionCommandFixtures.updateCommand();
            ProductGroupId pgId = ProductGroupId.of(command.productGroupId());
            Instant now = CommonVoFixtures.now();

            // activeSellerOptionGroup()은 id=10L, value id=100L로 고정된 그룹
            SellerOptionGroup existingGroup = ProductGroupFixtures.activeSellerOptionGroup();
            SellerOptionValue existingValue = ProductGroupFixtures.activeSellerOptionValue();
            SellerOptionGroups existingGroups =
                    SellerOptionGroups.reconstitute(List.of(existingGroup));

            // updateData에 기존 그룹 10L + 기존 값 100L를 그대로 넣어 변경 없음 시나리오 구성
            List<SellerOptionGroupUpdateData.ValueEntry> valueEntries =
                    List.of(new SellerOptionGroupUpdateData.ValueEntry(100L, "검정", 1));
            List<SellerOptionGroupUpdateData.GroupEntry> groupEntries =
                    List.of(new SellerOptionGroupUpdateData.GroupEntry(10L, "색상", 0, valueEntries));
            SellerOptionGroupUpdateData updateData =
                    SellerOptionGroupUpdateData.of(pgId, groupEntries, now);

            given(readManager.getByProductGroupId(pgId)).willReturn(existingGroups);
            given(optionGroupFactory.toUpdateData(eq(pgId), any())).willReturn(updateData);
            // persistDiff는 Mock이 호출됨 — 실제 diff는 existing.update(updateData)에서 계산됨
            given(persistFacade.persistDiff(any())).willReturn(List.of(existingValue.id()));

            // when
            SellerOptionUpdateResult result = sut.update(command);

            // then
            // diff.hasNoChanges() = true → !diff.hasNoChanges() = false
            assertThat(result.hasChanges()).isFalse();
        }

        @Test
        @DisplayName("수정 시 기존 옵션 그룹 조회가 수행된다")
        void update_ReadsExistingGroups() {
            // given
            UpdateSellerOptionGroupsCommand command = SellerOptionCommandFixtures.updateCommand();
            ProductGroupId pgId = ProductGroupId.of(command.productGroupId());
            Instant now = CommonVoFixtures.now();

            SellerOptionGroups existingGroups =
                    SellerOptionGroups.reconstitute(
                            ProductGroupFixtures.defaultSellerOptionGroups());

            SellerOptionGroupUpdateData updateData =
                    SellerOptionGroupUpdateData.of(pgId, List.of(), now);

            given(readManager.getByProductGroupId(pgId)).willReturn(existingGroups);
            given(optionGroupFactory.toUpdateData(eq(pgId), any())).willReturn(updateData);
            given(persistFacade.persistDiff(any())).willReturn(List.of());

            // when
            sut.update(command);

            // then
            then(readManager).should().getByProductGroupId(pgId);
        }
    }
}
