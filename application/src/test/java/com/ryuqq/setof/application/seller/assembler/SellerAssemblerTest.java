package com.ryuqq.setof.application.seller.assembler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.ryuqq.setof.application.seller.dto.command.RegisterSellerCommand;
import com.ryuqq.setof.application.seller.dto.response.SellerResponse;
import com.ryuqq.setof.application.seller.dto.response.SellerSummaryResponse;
import com.ryuqq.setof.domain.seller.aggregate.Seller;
import com.ryuqq.setof.domain.seller.vo.ApprovalStatus;
import com.ryuqq.setof.domain.seller.vo.BusinessAddress;
import com.ryuqq.setof.domain.seller.vo.BusinessInfo;
import com.ryuqq.setof.domain.seller.vo.CsEmail;
import com.ryuqq.setof.domain.seller.vo.CsLandlinePhone;
import com.ryuqq.setof.domain.seller.vo.CsMobilePhone;
import com.ryuqq.setof.domain.seller.vo.CustomerServiceInfo;
import com.ryuqq.setof.domain.seller.vo.Description;
import com.ryuqq.setof.domain.seller.vo.LogoUrl;
import com.ryuqq.setof.domain.seller.vo.RegistrationNumber;
import com.ryuqq.setof.domain.seller.vo.Representative;
import com.ryuqq.setof.domain.seller.vo.SaleReportNumber;
import com.ryuqq.setof.domain.seller.vo.SellerId;
import com.ryuqq.setof.domain.seller.vo.SellerName;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * SellerAssembler 테스트
 *
 * <p>Command ↔ Domain ↔ Response 변환에 대한 단위 테스트
 */
@DisplayName("SellerAssembler")
class SellerAssemblerTest {

    private static final Instant FIXED_TIME = Instant.parse("2025-01-01T00:00:00Z");

    private SellerAssembler sellerAssembler;

    @BeforeEach
    void setUp() {
        sellerAssembler = new SellerAssembler();
    }

    @Nested
    @DisplayName("toDomain")
    class ToDomainTest {

        @Test
        @DisplayName("모든 필드가 있는 Command를 Domain으로 변환")
        void shouldConvertFullCommandToDomain() {
            // Given
            RegisterSellerCommand command = createFullCommand();

            // When
            Seller seller = sellerAssembler.toDomain(command, FIXED_TIME);

            // Then
            assertNotNull(seller);
            assertEquals("테스트 셀러", seller.getNameValue());
            assertEquals("https://example.com/logo.png", seller.getLogoUrlValue());
            assertEquals("테스트 셀러 설명", seller.getDescriptionValue());
            assertEquals(ApprovalStatus.PENDING, seller.getApprovalStatus());
            assertFalse(seller.isActive());
            assertEquals("1234567890", seller.getRegistrationNumber());
            assertEquals("cs@example.com", seller.getCsEmail());
        }

        @Test
        @DisplayName("선택 필드가 없는 Command를 Domain으로 변환")
        void shouldConvertMinimalCommandToDomain() {
            // Given
            RegisterSellerCommand command = createMinimalCommand();

            // When
            Seller seller = sellerAssembler.toDomain(command, FIXED_TIME);

            // Then
            assertNotNull(seller);
            assertEquals("최소 셀러", seller.getNameValue());
            assertNull(seller.getLogoUrlValue());
            assertNull(seller.getDescriptionValue());
        }
    }

    @Nested
    @DisplayName("toSellerResponse")
    class ToSellerResponseTest {

        @Test
        @DisplayName("Domain을 SellerResponse로 변환")
        void shouldConvertDomainToResponse() {
            // Given
            Seller seller = createApprovedSeller();

            // When
            SellerResponse response = sellerAssembler.toSellerResponse(seller);

            // Then
            assertNotNull(response);
            assertEquals(1L, response.id());
            assertEquals("테스트 셀러", response.sellerName());
            assertEquals("https://example.com/logo.png", response.logoUrl());
            assertEquals("테스트 셀러 설명", response.description());
            assertEquals("APPROVED", response.approvalStatus());

            // Business Info
            assertNotNull(response.businessInfo());
            assertEquals("1234567890", response.businessInfo().registrationNumber());

            // CS Info
            assertNotNull(response.csInfo());
            assertEquals("cs@example.com", response.csInfo().email());
        }
    }

    @Nested
    @DisplayName("toSellerSummaryResponse")
    class ToSellerSummaryResponseTest {

        @Test
        @DisplayName("Domain을 SellerSummaryResponse로 변환")
        void shouldConvertDomainToSummaryResponse() {
            // Given
            Seller seller = createApprovedSeller();

            // When
            SellerSummaryResponse response = sellerAssembler.toSellerSummaryResponse(seller);

            // Then
            assertNotNull(response);
            assertEquals(1L, response.id());
            assertEquals("테스트 셀러", response.sellerName());
            assertEquals("https://example.com/logo.png", response.logoUrl());
            assertEquals("APPROVED", response.approvalStatus());
        }
    }

    @Nested
    @DisplayName("toSellerSummaryResponses")
    class ToSellerSummaryResponsesTest {

        @Test
        @DisplayName("Domain 목록을 SellerSummaryResponse 목록으로 변환")
        void shouldConvertDomainListToSummaryResponseList() {
            // Given
            List<Seller> sellers = List.of(createApprovedSeller(), createPendingSeller());

            // When
            List<SellerSummaryResponse> responses =
                    sellerAssembler.toSellerSummaryResponses(sellers);

            // Then
            assertNotNull(responses);
            assertEquals(2, responses.size());
            assertEquals(1L, responses.get(0).id());
            assertEquals(2L, responses.get(1).id());
        }
    }

    // ========== Helper Methods ==========

    private RegisterSellerCommand createFullCommand() {
        return new RegisterSellerCommand(
                "테스트 셀러",
                "https://example.com/logo.png",
                "테스트 셀러 설명",
                "1234567890",
                "2024-서울강남-1234",
                "홍길동",
                "서울시 강남구",
                "테헤란로 123",
                "06234",
                "cs@example.com",
                "01012345678",
                "021234567");
    }

    private RegisterSellerCommand createMinimalCommand() {
        return new RegisterSellerCommand(
                "최소 셀러",
                null,
                null,
                "1234567890",
                null,
                "홍길동",
                "서울시 강남구",
                null,
                "06234",
                "cs@example.com", // 도메인 규칙: 이메일/휴대폰/유선전화 중 최소 1개 필수
                null,
                null);
    }

    private Seller createApprovedSeller() {
        return Seller.reconstitute(
                SellerId.of(1L),
                SellerName.of("테스트 셀러"),
                LogoUrl.of("https://example.com/logo.png"),
                Description.of("테스트 셀러 설명"),
                ApprovalStatus.APPROVED,
                createBusinessInfo(),
                createCustomerServiceInfo(),
                FIXED_TIME,
                FIXED_TIME,
                null);
    }

    private Seller createPendingSeller() {
        return Seller.reconstitute(
                SellerId.of(2L),
                SellerName.of("대기 셀러"),
                null,
                null,
                ApprovalStatus.PENDING,
                createBusinessInfo(),
                createCustomerServiceInfo(),
                FIXED_TIME,
                FIXED_TIME,
                null);
    }

    private BusinessInfo createBusinessInfo() {
        return BusinessInfo.of(
                RegistrationNumber.of("1234567890"),
                SaleReportNumber.of("2024-서울강남-1234"),
                Representative.of("홍길동"),
                BusinessAddress.of("서울시 강남구", "테헤란로 123", "06234"));
    }

    private CustomerServiceInfo createCustomerServiceInfo() {
        return CustomerServiceInfo.of(
                CsEmail.of("cs@example.com"),
                CsMobilePhone.of("01012345678"),
                CsLandlinePhone.of("021234567"));
    }
}
