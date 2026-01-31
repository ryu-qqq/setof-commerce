package com.ryuqq.setof.adapter.out.persistence.composite.seller.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.ryuqq.setof.adapter.out.persistence.composite.seller.dto.SellerCompositeDto;
import com.ryuqq.setof.adapter.out.persistence.composite.seller.dto.SellerCompositeDtoFixtures;
import com.ryuqq.setof.adapter.out.persistence.composite.seller.dto.SellerPolicyCompositeDto;
import com.ryuqq.setof.adapter.out.persistence.composite.seller.repository.SellerCompositeQueryDslRepository;
import com.ryuqq.setof.adapter.out.persistence.composite.seller.repository.SellerPolicyCompositeQueryDslRepository;
import com.ryuqq.setof.application.seller.dto.composite.SellerCompositeResult;
import com.ryuqq.setof.application.seller.dto.composite.SellerPolicyCompositeResult;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * SellerCompositionQueryAdapterTest - 셀러 Composition 조회 Adapter 단위 테스트.
 *
 * <p>PER-ADP-004: QueryAdapter는 QueryDslRepository만 사용.
 *
 * <p>크로스 도메인 조인을 통한 성능 최적화된 조회 테스트.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("SellerCompositionQueryAdapter 단위 테스트")
class SellerCompositionQueryAdapterTest {

    @Mock private SellerCompositeQueryDslRepository compositeRepository;

    @Mock private SellerPolicyCompositeQueryDslRepository policyCompositeRepository;

    @InjectMocks private SellerCompositionQueryAdapter queryAdapter;

    // ========================================================================
    // 1. findSellerCompositeById 테스트
    // ========================================================================

    @Nested
    @DisplayName("findSellerCompositeById 메서드 테스트")
    class FindSellerCompositeByIdTest {

        @Test
        @DisplayName("존재하는 셀러 ID로 조회 시 SellerCompositeResult를 반환합니다")
        void findSellerCompositeById_WithExistingId_ReturnsResult() {
            // given
            Long sellerId = 1L;
            SellerCompositeDto dto = SellerCompositeDtoFixtures.activeSellerCompositeDto(sellerId);
            given(compositeRepository.findBySellerId(sellerId)).willReturn(Optional.of(dto));

            // when
            Optional<SellerCompositeResult> result = queryAdapter.findSellerCompositeById(sellerId);

            // then
            assertThat(result).isPresent();
            SellerCompositeResult compositeResult = result.get();

            // Seller Info 검증
            assertThat(compositeResult.seller().id()).isEqualTo(dto.sellerId());
            assertThat(compositeResult.seller().sellerName()).isEqualTo(dto.sellerName());
            assertThat(compositeResult.seller().displayName()).isEqualTo(dto.displayName());
            assertThat(compositeResult.seller().logoUrl()).isEqualTo(dto.logoUrl());
            assertThat(compositeResult.seller().description()).isEqualTo(dto.description());
            assertThat(compositeResult.seller().active()).isEqualTo(dto.active());

            // Address Info 검증
            assertThat(compositeResult.address().id()).isEqualTo(dto.addressId());
            assertThat(compositeResult.address().addressType()).isEqualTo(dto.addressType());
            assertThat(compositeResult.address().zipcode()).isEqualTo(dto.zipcode());
            assertThat(compositeResult.address().address()).isEqualTo(dto.address());

            // Business Info 검증
            assertThat(compositeResult.businessInfo().id()).isEqualTo(dto.businessInfoId());
            assertThat(compositeResult.businessInfo().registrationNumber())
                    .isEqualTo(dto.registrationNumber());
            assertThat(compositeResult.businessInfo().companyName()).isEqualTo(dto.companyName());
        }

        @Test
        @DisplayName("존재하지 않는 셀러 ID로 조회 시 빈 Optional을 반환합니다")
        void findSellerCompositeById_WithNonExistingId_ReturnsEmpty() {
            // given
            Long sellerId = 999L;
            given(compositeRepository.findBySellerId(sellerId)).willReturn(Optional.empty());

            // when
            Optional<SellerCompositeResult> result = queryAdapter.findSellerCompositeById(sellerId);

            // then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("비활성 상태 셀러 조회 시에도 정상적으로 반환합니다")
        void findSellerCompositeById_WithInactiveSeller_ReturnsResult() {
            // given
            Long sellerId = 2L;
            SellerCompositeDto dto = SellerCompositeDtoFixtures.inactiveSellerCompositeDto();
            given(compositeRepository.findBySellerId(sellerId)).willReturn(Optional.of(dto));

            // when
            Optional<SellerCompositeResult> result = queryAdapter.findSellerCompositeById(sellerId);

            // then
            assertThat(result).isPresent();
            assertThat(result.get().seller().active()).isFalse();
        }
    }

    // ========================================================================
    // 2. findPolicyCompositeById 테스트
    // ========================================================================

    @Nested
    @DisplayName("findPolicyCompositeById 메서드 테스트")
    class FindPolicyCompositeByIdTest {

        @Test
        @DisplayName("존재하는 셀러 ID로 조회 시 SellerPolicyCompositeResult를 반환합니다")
        void findPolicyCompositeById_WithExistingId_ReturnsResult() {
            // given
            Long sellerId = 1L;
            SellerPolicyCompositeDto dto =
                    SellerCompositeDtoFixtures.sellerPolicyCompositeDto(sellerId);
            given(policyCompositeRepository.findBySellerId(sellerId)).willReturn(Optional.of(dto));

            // when
            Optional<SellerPolicyCompositeResult> result =
                    queryAdapter.findPolicyCompositeById(sellerId);

            // then
            assertThat(result).isPresent();
            SellerPolicyCompositeResult policyResult = result.get();

            assertThat(policyResult.sellerId()).isEqualTo(sellerId);
            assertThat(policyResult.shippingPolicies()).hasSize(2);
            assertThat(policyResult.refundPolicies()).hasSize(1);
        }

        @Test
        @DisplayName("존재하지 않는 셀러 ID로 조회 시 빈 Optional을 반환합니다")
        void findPolicyCompositeById_WithNonExistingId_ReturnsEmpty() {
            // given
            Long sellerId = 999L;
            given(policyCompositeRepository.findBySellerId(sellerId)).willReturn(Optional.empty());

            // when
            Optional<SellerPolicyCompositeResult> result =
                    queryAdapter.findPolicyCompositeById(sellerId);

            // then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("빈 정책 목록을 가진 셀러 조회 시 빈 목록을 반환합니다")
        void findPolicyCompositeById_WithEmptyPolicies_ReturnsEmptyLists() {
            // given
            Long sellerId = 3L;
            SellerPolicyCompositeDto dto =
                    SellerCompositeDtoFixtures.emptyPolicyCompositeDto(sellerId);
            given(policyCompositeRepository.findBySellerId(sellerId)).willReturn(Optional.of(dto));

            // when
            Optional<SellerPolicyCompositeResult> result =
                    queryAdapter.findPolicyCompositeById(sellerId);

            // then
            assertThat(result).isPresent();
            assertThat(result.get().shippingPolicies()).isEmpty();
            assertThat(result.get().refundPolicies()).isEmpty();
        }

        @Test
        @DisplayName("배송 정책 DTO가 올바르게 변환됩니다")
        void findPolicyCompositeById_ShippingPolicyConvertedCorrectly() {
            // given
            Long sellerId = 1L;
            SellerPolicyCompositeDto dto =
                    SellerCompositeDtoFixtures.sellerPolicyCompositeDto(sellerId);
            given(policyCompositeRepository.findBySellerId(sellerId)).willReturn(Optional.of(dto));

            // when
            Optional<SellerPolicyCompositeResult> result =
                    queryAdapter.findPolicyCompositeById(sellerId);

            // then
            assertThat(result).isPresent();
            SellerPolicyCompositeResult.ShippingPolicyInfo shippingPolicy =
                    result.get().shippingPolicies().get(0);

            assertThat(shippingPolicy.id()).isEqualTo(dto.shippingPolicies().get(0).id());
            assertThat(shippingPolicy.policyName())
                    .isEqualTo(dto.shippingPolicies().get(0).policyName());
            assertThat(shippingPolicy.defaultPolicy())
                    .isEqualTo(dto.shippingPolicies().get(0).defaultPolicy());
            assertThat(shippingPolicy.baseFee()).isEqualTo(dto.shippingPolicies().get(0).baseFee());
        }

        @Test
        @DisplayName("환불 정책 DTO가 올바르게 변환됩니다")
        void findPolicyCompositeById_RefundPolicyConvertedCorrectly() {
            // given
            Long sellerId = 1L;
            SellerPolicyCompositeDto dto =
                    SellerCompositeDtoFixtures.sellerPolicyCompositeDto(sellerId);
            given(policyCompositeRepository.findBySellerId(sellerId)).willReturn(Optional.of(dto));

            // when
            Optional<SellerPolicyCompositeResult> result =
                    queryAdapter.findPolicyCompositeById(sellerId);

            // then
            assertThat(result).isPresent();
            SellerPolicyCompositeResult.RefundPolicyInfo refundPolicy =
                    result.get().refundPolicies().get(0);

            assertThat(refundPolicy.id()).isEqualTo(dto.refundPolicies().get(0).id());
            assertThat(refundPolicy.policyName())
                    .isEqualTo(dto.refundPolicies().get(0).policyName());
            assertThat(refundPolicy.returnPeriodDays())
                    .isEqualTo(dto.refundPolicies().get(0).returnPeriodDays());
            assertThat(refundPolicy.partialRefundEnabled())
                    .isEqualTo(dto.refundPolicies().get(0).partialRefundEnabled());
        }
    }
}
