package com.ryuqq.setof.application.selleroption.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.selleroption.port.out.command.SellerOptionGroupCommandPort;
import com.ryuqq.setof.application.selleroption.port.out.command.SellerOptionValueCommandPort;
import com.ryuqq.setof.domain.common.CommonVoFixtures;
import com.ryuqq.setof.domain.productgroup.ProductGroupFixtures;
import com.ryuqq.setof.domain.productgroup.aggregate.SellerOptionGroup;
import com.ryuqq.setof.domain.productgroup.aggregate.SellerOptionValue;
import com.ryuqq.setof.domain.productgroup.id.SellerOptionValueId;
import com.ryuqq.setof.domain.productgroup.vo.SellerOptionGroupDiff;
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
@DisplayName("SellerOptionPersistFacade 단위 테스트")
class SellerOptionPersistFacadeTest {

    @InjectMocks private SellerOptionPersistFacade sut;

    @Mock private SellerOptionGroupCommandPort groupCommandPort;
    @Mock private SellerOptionValueCommandPort valueCommandPort;

    @Nested
    @DisplayName("persistAll() - OptionGroup + OptionValue 저장 후 OptionValueId 반환")
    class PersistAllTest {

        @Test
        @DisplayName("단일 그룹을 저장하고 생성된 SellerOptionValueId 목록을 반환한다")
        void persistAll_SingleGroup_ReturnsValueIds() {
            // given
            SellerOptionGroup group = ProductGroupFixtures.newSellerOptionGroup();
            Long generatedGroupId = 10L;
            List<Long> generatedValueIds = List.of(100L);

            given(groupCommandPort.persist(group)).willReturn(generatedGroupId);
            given(valueCommandPort.persistAllForGroup(eq(generatedGroupId), anyList()))
                    .willReturn(generatedValueIds);

            // when
            List<SellerOptionValueId> result = sut.persistAll(List.of(group));

            // then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).value()).isEqualTo(100L);
            then(groupCommandPort).should().persist(group);
            then(valueCommandPort).should().persistAllForGroup(eq(generatedGroupId), anyList());
        }

        @Test
        @DisplayName("빈 그룹 목록을 저장하면 빈 목록을 반환한다")
        void persistAll_EmptyGroups_ReturnsEmptyList() {
            // when
            List<SellerOptionValueId> result = sut.persistAll(List.of());

            // then
            assertThat(result).isEmpty();
            then(groupCommandPort).shouldHaveNoInteractions();
            then(valueCommandPort).shouldHaveNoInteractions();
        }

        @Test
        @DisplayName("복수 그룹을 저장하면 모든 ValueId를 flat하게 반환한다")
        void persistAll_MultipleGroups_ReturnsAllValueIds() {
            // given
            SellerOptionGroup group1 = ProductGroupFixtures.newSellerOptionGroup();
            SellerOptionGroup group2 = ProductGroupFixtures.newSellerOptionGroup();

            given(groupCommandPort.persist(group1)).willReturn(10L);
            given(groupCommandPort.persist(group2)).willReturn(11L);
            given(valueCommandPort.persistAllForGroup(eq(10L), anyList()))
                    .willReturn(List.of(100L));
            given(valueCommandPort.persistAllForGroup(eq(11L), anyList()))
                    .willReturn(List.of(101L, 102L));

            // when
            List<SellerOptionValueId> result = sut.persistAll(List.of(group1, group2));

            // then
            assertThat(result).hasSize(3);
        }
    }

    @Nested
    @DisplayName("persistDiff() - Diff 기반 OptionGroup + OptionValue 저장 후 resolved ValueId 반환")
    class PersistDiffTest {

        @Test
        @DisplayName("변경 없는 diff(모두 retained)를 처리하면 기존 ValueId 목록을 반환한다")
        void persistDiff_NoChanges_ReturnsExistingValueIds() {
            // given
            SellerOptionGroup existingGroup = ProductGroupFixtures.activeSellerOptionGroup();
            SellerOptionValue existingValue = ProductGroupFixtures.activeSellerOptionValue();
            Instant now = CommonVoFixtures.now();

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

            // when
            List<SellerOptionValueId> result = sut.persistDiff(diff);

            // then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).value()).isEqualTo(existingValue.id().value());
            then(groupCommandPort).should().persistAll(anyList());
            then(valueCommandPort).should().persistAll(diff.allRemovedValues());
            then(valueCommandPort).should().persistAll(diff.allRetainedValues());
        }

        @Test
        @DisplayName("신규 그룹 추가 시 persist 후 생성된 ID로 resolved된 ValueId를 반환한다")
        void persistDiff_AddedGroup_ReturnsResolvedValueIds() {
            // given
            SellerOptionGroup newGroup = ProductGroupFixtures.newSellerOptionGroup();
            SellerOptionValue newValue = newGroup.optionValues().get(0);
            Instant now = CommonVoFixtures.now();

            Long generatedGroupId = 20L;
            Long generatedValueId = 200L;

            List<SellerOptionValueId> orderedActiveIds = List.of(newValue.id());

            SellerOptionGroupDiff diff =
                    SellerOptionGroupDiff.of(
                            List.of(newGroup), List.of(), List.of(), orderedActiveIds, now);

            given(groupCommandPort.persist(newGroup)).willReturn(generatedGroupId);
            given(valueCommandPort.persistAllForGroup(eq(generatedGroupId), anyList()))
                    .willReturn(List.of(generatedValueId));

            // when
            List<SellerOptionValueId> result = sut.persistDiff(diff);

            // then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).value()).isEqualTo(generatedValueId);
        }

        @Test
        @DisplayName(
                "orderedActiveIds에 있는 신규 ValueId의 생성 ID가 identity 맵에 없으면 IllegalStateException이"
                        + " 발생한다")
        void persistDiff_MissingGeneratedId_ThrowsIllegalStateException() {
            // given
            // newGroup의 value 인스턴스를 직접 취득하여 orderedActiveIds에 사용
            SellerOptionGroup newGroup = ProductGroupFixtures.newSellerOptionGroup();
            SellerOptionValue newValue = newGroup.optionValues().get(0);
            Instant now = CommonVoFixtures.now();

            // orderedActiveIds에 newValue.id()를 포함 (isNew() = true)
            List<SellerOptionValueId> orderedActiveIds = List.of(newValue.id());

            SellerOptionGroupDiff diff =
                    SellerOptionGroupDiff.of(
                            List.of(newGroup), List.of(), List.of(), orderedActiveIds, now);

            given(groupCommandPort.persist(newGroup)).willReturn(20L);
            // persistAllForGroup이 생성된 ID를 반환하지만,
            // IdentityHashMap 매핑은 allAddedValueInstances의 인스턴스 기준으로 수행됨.
            // orderedActiveIds에 있는 newValue.id()는 신규(isNew=true)이고,
            // 실제 매핑에서 newValue.id() 인스턴스가 map에 없을 때 IllegalStateException 발생.
            // 여기서는 persistAllForGroup을 실제 그룹의 optionValues 처리 중에 호출하므로
            // 다른 별도 그룹의 newSellerOptionGroup()을 orderedActiveIds에 넣어 불일치를 유발한다.
            SellerOptionGroup anotherGroup = ProductGroupFixtures.newSellerOptionGroup();
            SellerOptionValue anotherValue = anotherGroup.optionValues().get(0);

            // diff를 anotherValue.id()가 포함된 orderedActiveIds로 구성
            List<SellerOptionValueId> mismatchedActiveIds = List.of(anotherValue.id());
            SellerOptionGroupDiff mismatchedDiff =
                    SellerOptionGroupDiff.of(
                            List.of(newGroup), List.of(), List.of(), mismatchedActiveIds, now);

            given(valueCommandPort.persistAllForGroup(eq(20L), anyList()))
                    .willReturn(List.of(200L));

            // when & then
            // resolveActiveValueIds: anotherValue.id().isNew()=true, generatedIdMap에
            // anotherValue.id() 없음
            assertThatThrownBy(() -> sut.persistDiff(mismatchedDiff))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("신규 SellerOptionValueId에 대한 생성된 ID를 찾을 수 없습니다");
        }
    }
}
