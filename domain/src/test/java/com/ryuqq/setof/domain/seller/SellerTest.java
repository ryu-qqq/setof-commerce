package com.ryuqq.setof.domain.seller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Seller Aggregate 테스트
 *
 * <p>셀러 정보 관리에 대한 도메인 로직을 테스트합니다.
 */
@DisplayName("Seller Aggregate")
class SellerTest {

    private static final Instant FIXED_TIME = Instant.parse("2025-01-01T00:00:00Z");

    @Nested
    @DisplayName("create() - 신규 셀러 생성")
    class Create {

        @Test
        @DisplayName("신규 셀러를 생성할 수 있다")
        void shouldCreateNewSeller() {
            // given
            SellerName name = SellerName.of("테스트 셀러");
            LogoUrl logoUrl = LogoUrl.of("https://example.com/logo.png");
            Description description = Description.of("테스트 셀러 설명");
            BusinessInfo businessInfo = createDefaultBusinessInfo();
            CustomerServiceInfo csInfo = createDefaultCustomerServiceInfo();

            // when
            Seller seller =
                    Seller.create(name, logoUrl, description, businessInfo, csInfo, FIXED_TIME);

            // then
            assertNotNull(seller);
            assertNull(seller.getId()); // ID는 null (Persistence에서 설정)
            assertEquals("테스트 셀러", seller.getNameValue());
            assertEquals("https://example.com/logo.png", seller.getLogoUrlValue());
            assertEquals("테스트 셀러 설명", seller.getDescriptionValue());
            assertEquals(ApprovalStatus.PENDING, seller.getApprovalStatus());
            assertFalse(seller.isActive());
            assertFalse(seller.isDeleted());
            assertNotNull(seller.getCreatedAt());
            assertNotNull(seller.getUpdatedAt());
        }

        @Test
        @DisplayName("로고 URL 없이 셀러를 생성할 수 있다")
        void shouldCreateSellerWithoutLogoUrl() {
            // given
            SellerName name = SellerName.of("테스트 셀러");
            BusinessInfo businessInfo = createDefaultBusinessInfo();
            CustomerServiceInfo csInfo = createDefaultCustomerServiceInfo();

            // when
            Seller seller = Seller.create(name, null, null, businessInfo, csInfo, FIXED_TIME);

            // then
            assertNotNull(seller);
            assertNull(seller.getLogoUrlValue());
            assertNull(seller.getDescriptionValue());
        }
    }

    @Nested
    @DisplayName("reconstitute() - Persistence 복원")
    class Reconstitute {

        @Test
        @DisplayName("Persistence에서 모든 필드를 복원할 수 있다")
        void shouldReconstituteSellerFromPersistence() {
            // given
            SellerId id = SellerId.of(1L);
            SellerName name = SellerName.of("복원된 셀러");
            LogoUrl logoUrl = LogoUrl.of("https://example.com/logo.png");
            Description description = Description.of("복원된 설명");
            BusinessInfo businessInfo = createDefaultBusinessInfo();
            CustomerServiceInfo csInfo = createDefaultCustomerServiceInfo();
            Instant createdAt = Instant.parse("2024-01-01T00:00:00Z");
            Instant updatedAt = Instant.parse("2024-06-01T00:00:00Z");

            // when
            Seller seller =
                    Seller.reconstitute(
                            id,
                            name,
                            logoUrl,
                            description,
                            ApprovalStatus.APPROVED,
                            businessInfo,
                            csInfo,
                            createdAt,
                            updatedAt,
                            null);

            // then
            assertEquals(1L, seller.getIdValue());
            assertEquals("복원된 셀러", seller.getNameValue());
            assertTrue(seller.isActive());
            assertFalse(seller.isDeleted());
            assertEquals(createdAt, seller.getCreatedAt());
            assertEquals(updatedAt, seller.getUpdatedAt());
        }

        @Test
        @DisplayName("삭제된 셀러를 복원할 수 있다")
        void shouldReconstituteDeletedSeller() {
            // given
            Instant deletedAt = Instant.parse("2024-12-01T00:00:00Z");

            // when
            Seller seller =
                    Seller.reconstitute(
                            SellerId.of(1L),
                            SellerName.of("삭제된 셀러"),
                            null,
                            null,
                            ApprovalStatus.APPROVED,
                            createDefaultBusinessInfo(),
                            createDefaultCustomerServiceInfo(),
                            FIXED_TIME,
                            FIXED_TIME,
                            deletedAt);

            // then
            assertTrue(seller.isDeleted());
            assertEquals(deletedAt, seller.getDeletedAt());
        }
    }

    @Nested
    @DisplayName("update() - 셀러 정보 수정")
    class Update {

        @Test
        @DisplayName("셀러 정보를 수정할 수 있다")
        void shouldUpdateSeller() {
            // given
            Seller seller = createDefaultSeller();
            SellerName newName = SellerName.of("수정된 셀러");
            LogoUrl newLogoUrl = LogoUrl.of("https://example.com/new-logo.png");
            Description newDescription = Description.of("수정된 설명");
            BusinessInfo newBusinessInfo = createDefaultBusinessInfo();
            CustomerServiceInfo newCsInfo = createDefaultCustomerServiceInfo();

            // when
            Seller updatedSeller =
                    seller.update(
                            newName,
                            newLogoUrl,
                            newDescription,
                            newBusinessInfo,
                            newCsInfo,
                            FIXED_TIME);

            // then
            assertEquals("수정된 셀러", updatedSeller.getNameValue());
            assertEquals("https://example.com/new-logo.png", updatedSeller.getLogoUrlValue());
            assertEquals("수정된 설명", updatedSeller.getDescriptionValue());
            // 승인 상태는 유지됨
            assertEquals(seller.getApprovalStatus(), updatedSeller.getApprovalStatus());
        }
    }

    @Nested
    @DisplayName("approve() / reject() / suspend() - 상태 변경")
    class StatusChange {

        @Test
        @DisplayName("PENDING 상태에서 승인할 수 있다")
        void shouldApproveFromPending() {
            // given
            Seller seller = createPendingSeller();
            assertEquals(ApprovalStatus.PENDING, seller.getApprovalStatus());

            // when
            Seller approvedSeller = seller.approve(FIXED_TIME);

            // then
            assertEquals(ApprovalStatus.APPROVED, approvedSeller.getApprovalStatus());
            assertTrue(approvedSeller.isActive());
        }

        @Test
        @DisplayName("PENDING 상태에서 거부할 수 있다")
        void shouldRejectFromPending() {
            // given
            Seller seller = createPendingSeller();

            // when
            Seller rejectedSeller = seller.reject(FIXED_TIME);

            // then
            assertEquals(ApprovalStatus.REJECTED, rejectedSeller.getApprovalStatus());
            assertFalse(rejectedSeller.isActive());
        }

        @Test
        @DisplayName("APPROVED 상태에서 정지할 수 있다")
        void shouldSuspendFromApproved() {
            // given
            Seller seller = createApprovedSeller();

            // when
            Seller suspendedSeller = seller.suspend(FIXED_TIME);

            // then
            assertEquals(ApprovalStatus.SUSPENDED, suspendedSeller.getApprovalStatus());
            assertFalse(suspendedSeller.isActive());
        }

        @Test
        @DisplayName("APPROVED 상태에서는 승인할 수 없다")
        void shouldThrowExceptionWhenApprovingApproved() {
            // given
            Seller seller = createApprovedSeller();

            // when & then
            assertThrows(IllegalStateException.class, () -> seller.approve(FIXED_TIME));
        }

        @Test
        @DisplayName("PENDING 상태에서는 정지할 수 없다")
        void shouldThrowExceptionWhenSuspendingPending() {
            // given
            Seller seller = createPendingSeller();

            // when & then
            assertThrows(IllegalStateException.class, () -> seller.suspend(FIXED_TIME));
        }
    }

    @Nested
    @DisplayName("delete() - 셀러 삭제")
    class Delete {

        @Test
        @DisplayName("셀러를 소프트 삭제할 수 있다")
        void shouldSoftDeleteSeller() {
            // given
            Seller seller = createDefaultSeller();
            assertFalse(seller.isDeleted());

            // when
            Seller deletedSeller = seller.delete(FIXED_TIME);

            // then
            assertTrue(deletedSeller.isDeleted());
            assertNotNull(deletedSeller.getDeletedAt());
        }
    }

    @Nested
    @DisplayName("Law of Demeter Helper Methods")
    class HelperMethods {

        @Test
        @DisplayName("사업자 정보 헬퍼 메서드가 올바르게 동작한다")
        void shouldReturnBusinessInfoCorrectly() {
            // given
            Seller seller = createDefaultSeller();

            // then
            assertEquals("1234567890", seller.getRegistrationNumber());
            assertEquals("홍길동", seller.getRepresentative());
            assertEquals("서울시 강남구", seller.getBusinessAddressLine1());
        }

        @Test
        @DisplayName("CS 정보 헬퍼 메서드가 올바르게 동작한다")
        void shouldReturnCsInfoCorrectly() {
            // given
            Seller seller = createDefaultSeller();

            // then
            assertEquals("cs@example.com", seller.getCsEmail());
            assertEquals("01012345678", seller.getCsMobilePhone());
            assertEquals("021234567", seller.getCsLandlinePhone());
        }

        @Test
        @DisplayName("getIdValue()는 ID가 없으면 null을 반환한다")
        void shouldReturnNullWhenIdIsNull() {
            // given
            Seller seller =
                    Seller.create(
                            SellerName.of("테스트"),
                            null,
                            null,
                            createDefaultBusinessInfo(),
                            createDefaultCustomerServiceInfo(),
                            FIXED_TIME);

            // then
            assertNull(seller.getIdValue());
            assertFalse(seller.hasId());
        }
    }

    // ========== Helper Methods ==========

    private Seller createDefaultSeller() {
        return Seller.reconstitute(
                SellerId.of(1L),
                SellerName.of("기본 셀러"),
                LogoUrl.of("https://example.com/logo.png"),
                Description.of("기본 설명"),
                ApprovalStatus.APPROVED,
                createDefaultBusinessInfo(),
                createDefaultCustomerServiceInfo(),
                FIXED_TIME,
                FIXED_TIME,
                null);
    }

    private Seller createPendingSeller() {
        return Seller.reconstitute(
                SellerId.of(1L),
                SellerName.of("대기 셀러"),
                null,
                null,
                ApprovalStatus.PENDING,
                createDefaultBusinessInfo(),
                createDefaultCustomerServiceInfo(),
                FIXED_TIME,
                FIXED_TIME,
                null);
    }

    private Seller createApprovedSeller() {
        return Seller.reconstitute(
                SellerId.of(1L),
                SellerName.of("승인 셀러"),
                null,
                null,
                ApprovalStatus.APPROVED,
                createDefaultBusinessInfo(),
                createDefaultCustomerServiceInfo(),
                FIXED_TIME,
                FIXED_TIME,
                null);
    }

    private BusinessInfo createDefaultBusinessInfo() {
        return BusinessInfo.of(
                RegistrationNumber.of("1234567890"),
                SaleReportNumber.of("2024-서울강남-1234"),
                Representative.of("홍길동"),
                BusinessAddress.of("서울시 강남구", "테헤란로 123", "06234"));
    }

    private CustomerServiceInfo createDefaultCustomerServiceInfo() {
        return CustomerServiceInfo.of(
                CsEmail.of("cs@example.com"),
                CsMobilePhone.of("01012345678"),
                CsLandlinePhone.of("021234567"));
    }
}
