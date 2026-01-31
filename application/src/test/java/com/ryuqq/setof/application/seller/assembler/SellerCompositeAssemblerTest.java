package com.ryuqq.setof.application.seller.assembler;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.application.seller.dto.composite.SellerAdminCompositeResult;
import com.ryuqq.setof.application.seller.dto.composite.SellerFullCompositeResult;
import com.ryuqq.setof.application.seller.dto.composite.SellerPolicyCompositeResult;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("SellerCompositeAssembler 단위 테스트")
class SellerCompositeAssemblerTest {

    private final SellerCompositeAssembler sut = new SellerCompositeAssembler();

    @Nested
    @DisplayName("assemble() - Composite 결과 조합")
    class AssembleTest {

        @Test
        @DisplayName(
                "SellerAdminCompositeResult와 SellerPolicyCompositeResult를 조합하여 FullComposite를 생성한다")
        void assemble_ReturnsFullCompositeResult() {
            // given
            SellerAdminCompositeResult adminComposite = createAdminComposite();
            SellerPolicyCompositeResult policyComposite = createPolicyComposite();

            // when
            SellerFullCompositeResult result = sut.assemble(adminComposite, policyComposite);

            // then
            assertThat(result).isNotNull();
            assertThat(result.sellerComposite()).isNotNull();
            assertThat(result.policyComposite()).isEqualTo(policyComposite);
            assertThat(result.contractInfo()).isNotNull();
            assertThat(result.settlementInfo()).isNotNull();
        }

        @Test
        @DisplayName("정책이 빈 목록인 경우에도 정상 조합한다")
        void assemble_EmptyPolicies_ReturnsResult() {
            // given
            SellerAdminCompositeResult adminComposite = createAdminComposite();
            SellerPolicyCompositeResult policyComposite = createEmptyPolicyComposite();

            // when
            SellerFullCompositeResult result = sut.assemble(adminComposite, policyComposite);

            // then
            assertThat(result).isNotNull();
            assertThat(result.sellerComposite()).isNotNull();
            assertThat(result.policyComposite().shippingPolicies()).isEmpty();
            assertThat(result.policyComposite().refundPolicies()).isEmpty();
        }
    }

    private SellerAdminCompositeResult createAdminComposite() {
        Instant now = Instant.now();

        SellerAdminCompositeResult.SellerInfo sellerInfo =
                new SellerAdminCompositeResult.SellerInfo(
                        1L,
                        "테스트 셀러",
                        "테스트 스토어",
                        "https://example.com/logo.png",
                        "설명",
                        true,
                        now,
                        now);

        SellerAdminCompositeResult.AddressInfo addressInfo =
                new SellerAdminCompositeResult.AddressInfo(
                        1L,
                        "SHIPPING",
                        "본사 창고",
                        "06141",
                        "서울시 강남구 테헤란로 123",
                        "테스트빌딩 5층",
                        "김담당",
                        "010-1234-5678",
                        true);

        SellerAdminCompositeResult.BusinessInfo businessInfo =
                new SellerAdminCompositeResult.BusinessInfo(
                        1L,
                        "123-45-67890",
                        "테스트 주식회사",
                        "홍길동",
                        "2024-서울강남-0001",
                        "06141",
                        "서울시 강남구 테헤란로 123",
                        "테스트빌딩 5층");

        SellerAdminCompositeResult.CsInfo csInfo =
                new SellerAdminCompositeResult.CsInfo(
                        1L,
                        "02-1234-5678",
                        "010-1234-5678",
                        "cs@test.com",
                        "09:00",
                        "18:00",
                        "MON,TUE,WED,THU,FRI",
                        "https://kakao.test");

        SellerAdminCompositeResult.ContractInfo contractInfo =
                new SellerAdminCompositeResult.ContractInfo(
                        1L,
                        BigDecimal.valueOf(10.0),
                        LocalDate.now(),
                        LocalDate.now().plusYears(1),
                        "ACTIVE",
                        null,
                        now,
                        now);

        SellerAdminCompositeResult.SettlementInfo settlementInfo =
                new SellerAdminCompositeResult.SettlementInfo(
                        1L,
                        "088",
                        "신한은행",
                        "123-456-789",
                        "테스트 주식회사",
                        "MONTHLY",
                        15,
                        true,
                        now,
                        now,
                        now);

        return new SellerAdminCompositeResult(
                sellerInfo, addressInfo, businessInfo, csInfo, contractInfo, settlementInfo);
    }

    private SellerPolicyCompositeResult createPolicyComposite() {
        return new SellerPolicyCompositeResult(1L, List.of(), List.of());
    }

    private SellerPolicyCompositeResult createEmptyPolicyComposite() {
        return new SellerPolicyCompositeResult(1L, List.of(), List.of());
    }
}
