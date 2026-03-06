package com.ryuqq.setof.application.refundpolicy.manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.refundpolicy.port.out.query.RefundPolicyQueryPort;
import com.ryuqq.setof.domain.common.CommonVoFixtures;
import com.ryuqq.setof.domain.refundpolicy.RefundPolicyFixtures;
import com.ryuqq.setof.domain.refundpolicy.aggregate.RefundPolicy;
import com.ryuqq.setof.domain.refundpolicy.exception.RefundPolicyNotFoundException;
import com.ryuqq.setof.domain.refundpolicy.id.RefundPolicyId;
import com.ryuqq.setof.domain.refundpolicy.query.RefundPolicySearchCriteria;
import com.ryuqq.setof.domain.seller.id.SellerId;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
@DisplayName("RefundPolicyReadManager 단위 테스트")
class RefundPolicyReadManagerTest {

    @InjectMocks private RefundPolicyReadManager sut;

    @Mock private RefundPolicyQueryPort queryPort;

    @Nested
    @DisplayName("getById() - ID로 환불 정책 조회")
    class GetByIdTest {

        @Test
        @DisplayName("ID로 환불 정책을 조회한다")
        void getById_ExistingPolicy_ReturnsPolicy() {
            // given
            RefundPolicyId id = RefundPolicyId.of(1L);
            RefundPolicy expected = RefundPolicyFixtures.activeRefundPolicy();

            given(queryPort.findById(id)).willReturn(Optional.of(expected));

            // when
            RefundPolicy result = sut.getById(id);

            // then
            assertThat(result).isEqualTo(expected);
            then(queryPort).should().findById(id);
        }

        @Test
        @DisplayName("존재하지 않는 ID로 조회 시 예외가 발생한다")
        void getById_NonExistingPolicy_ThrowsException() {
            // given
            RefundPolicyId id = RefundPolicyId.of(999L);

            given(queryPort.findById(id)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> sut.getById(id))
                    .isInstanceOf(RefundPolicyNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("getByIds() - ID 목록으로 환불 정책 조회")
    class GetByIdsTest {

        @Test
        @DisplayName("ID 목록으로 환불 정책 목록을 조회한다")
        void getByIds_ReturnsMatchingPolicies() {
            // given
            List<RefundPolicyId> ids = List.of(RefundPolicyId.of(1L), RefundPolicyId.of(2L));
            List<RefundPolicy> expected =
                    List.of(
                            RefundPolicyFixtures.activeRefundPolicy(),
                            RefundPolicyFixtures.inactiveRefundPolicy());

            given(queryPort.findByIds(ids)).willReturn(expected);

            // when
            List<RefundPolicy> result = sut.getByIds(ids);

            // then
            assertThat(result).hasSize(2);
            then(queryPort).should().findByIds(ids);
        }

        @Test
        @DisplayName("빈 ID 목록이면 빈 목록을 반환한다")
        void getByIds_EmptyIds_ReturnsEmptyList() {
            // given
            List<RefundPolicyId> ids = Collections.emptyList();

            given(queryPort.findByIds(ids)).willReturn(Collections.emptyList());

            // when
            List<RefundPolicy> result = sut.getByIds(ids);

            // then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("findDefaultBySellerId() - 셀러 ID로 기본 정책 조회")
    class FindDefaultBySellerIdTest {

        @Test
        @DisplayName("셀러의 기본 정책을 반환한다")
        void findDefaultBySellerId_ReturnsDefaultPolicy() {
            // given
            SellerId sellerId = CommonVoFixtures.defaultSellerId();
            RefundPolicy expected = RefundPolicyFixtures.activeRefundPolicy();

            given(queryPort.findDefaultBySellerId(sellerId)).willReturn(Optional.of(expected));

            // when
            Optional<RefundPolicy> result = sut.findDefaultBySellerId(sellerId);

            // then
            assertThat(result).isPresent();
            assertThat(result.get()).isEqualTo(expected);
            then(queryPort).should().findDefaultBySellerId(sellerId);
        }

        @Test
        @DisplayName("기본 정책이 없으면 빈 Optional을 반환한다")
        void findDefaultBySellerId_NoDefault_ReturnsEmpty() {
            // given
            SellerId sellerId = CommonVoFixtures.defaultSellerId();

            given(queryPort.findDefaultBySellerId(sellerId)).willReturn(Optional.empty());

            // when
            Optional<RefundPolicy> result = sut.findDefaultBySellerId(sellerId);

            // then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("findBySellerIdAndId() - 셀러 ID와 정책 ID로 조회")
    class FindBySellerIdAndIdTest {

        @Test
        @DisplayName("셀러가 소유한 정책을 반환한다")
        void findBySellerIdAndId_ReturnsPolicy() {
            // given
            SellerId sellerId = CommonVoFixtures.defaultSellerId();
            RefundPolicyId policyId = RefundPolicyId.of(1L);
            RefundPolicy expected = RefundPolicyFixtures.activeRefundPolicy();

            given(queryPort.findBySellerIdAndId(sellerId, policyId))
                    .willReturn(Optional.of(expected));

            // when
            Optional<RefundPolicy> result = sut.findBySellerIdAndId(sellerId, policyId);

            // then
            assertThat(result).isPresent();
            assertThat(result.get()).isEqualTo(expected);
            then(queryPort).should().findBySellerIdAndId(sellerId, policyId);
        }

        @Test
        @DisplayName("해당 셀러의 정책이 없으면 빈 Optional을 반환한다")
        void findBySellerIdAndId_NotFound_ReturnsEmpty() {
            // given
            SellerId sellerId = CommonVoFixtures.defaultSellerId();
            RefundPolicyId policyId = RefundPolicyId.of(999L);

            given(queryPort.findBySellerIdAndId(sellerId, policyId)).willReturn(Optional.empty());

            // when
            Optional<RefundPolicy> result = sut.findBySellerIdAndId(sellerId, policyId);

            // then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("findByCriteria() - 검색 조건으로 환불 정책 조회")
    class FindByCriteriaTest {

        @Test
        @DisplayName("검색 조건으로 환불 정책 목록을 조회한다")
        void findByCriteria_ReturnsMatchingPolicies() {
            // given
            RefundPolicySearchCriteria criteria =
                    RefundPolicySearchCriteria.defaultCriteria(CommonVoFixtures.defaultSellerId());
            List<RefundPolicy> expected =
                    List.of(
                            RefundPolicyFixtures.activeRefundPolicy(),
                            RefundPolicyFixtures.newRefundPolicy(7, 14));

            given(queryPort.findByCriteria(criteria)).willReturn(expected);

            // when
            List<RefundPolicy> result = sut.findByCriteria(criteria);

            // then
            assertThat(result).hasSize(2);
            then(queryPort).should().findByCriteria(criteria);
        }

        @Test
        @DisplayName("검색 결과가 없으면 빈 목록을 반환한다")
        void findByCriteria_NoResults_ReturnsEmptyList() {
            // given
            RefundPolicySearchCriteria criteria =
                    RefundPolicySearchCriteria.defaultCriteria(CommonVoFixtures.defaultSellerId());

            given(queryPort.findByCriteria(criteria)).willReturn(Collections.emptyList());

            // when
            List<RefundPolicy> result = sut.findByCriteria(criteria);

            // then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("countByCriteria() - 검색 조건으로 환불 정책 수 조회")
    class CountByCriteriaTest {

        @Test
        @DisplayName("검색 조건에 맞는 환불 정책 수를 반환한다")
        void countByCriteria_ReturnsCount() {
            // given
            RefundPolicySearchCriteria criteria =
                    RefundPolicySearchCriteria.defaultCriteria(CommonVoFixtures.defaultSellerId());
            long expectedCount = 5L;

            given(queryPort.countByCriteria(criteria)).willReturn(expectedCount);

            // when
            long result = sut.countByCriteria(criteria);

            // then
            assertThat(result).isEqualTo(expectedCount);
            then(queryPort).should().countByCriteria(criteria);
        }
    }

    @Nested
    @DisplayName("countActiveBySellerId() - 셀러의 활성 정책 수 조회")
    class CountActiveBySellerIdTest {

        @Test
        @DisplayName("셀러의 활성 정책 수를 반환한다")
        void countActiveBySellerId_ReturnsActiveCount() {
            // given
            SellerId sellerId = CommonVoFixtures.defaultSellerId();
            long expectedCount = 3L;

            given(queryPort.countActiveBySellerId(sellerId)).willReturn(expectedCount);

            // when
            long result = sut.countActiveBySellerId(sellerId);

            // then
            assertThat(result).isEqualTo(expectedCount);
            then(queryPort).should().countActiveBySellerId(sellerId);
        }

        @Test
        @DisplayName("활성 정책이 없으면 0을 반환한다")
        void countActiveBySellerId_NoPolicies_ReturnsZero() {
            // given
            SellerId sellerId = CommonVoFixtures.defaultSellerId();

            given(queryPort.countActiveBySellerId(sellerId)).willReturn(0L);

            // when
            long result = sut.countActiveBySellerId(sellerId);

            // then
            assertThat(result).isZero();
        }
    }
}
