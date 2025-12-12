package com.ryuqq.setof.application.seller.service.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ryuqq.setof.application.seller.dto.command.RegisterSellerCommand;
import com.ryuqq.setof.application.seller.factory.command.SellerCommandFactory;
import com.ryuqq.setof.application.seller.manager.command.SellerPersistenceManager;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * RegisterSellerService 테스트
 *
 * <p>셀러 등록 서비스에 대한 단위 테스트
 */
@DisplayName("RegisterSellerService")
@ExtendWith(MockitoExtension.class)
class RegisterSellerServiceTest {

    private static final Instant FIXED_TIME = Instant.parse("2025-01-01T00:00:00Z");

    @Mock private SellerCommandFactory sellerCommandFactory;
    @Mock private SellerPersistenceManager sellerPersistenceManager;

    private RegisterSellerService registerSellerService;

    @BeforeEach
    void setUp() {
        registerSellerService =
                new RegisterSellerService(sellerCommandFactory, sellerPersistenceManager);
    }

    @Nested
    @DisplayName("execute")
    class ExecuteTest {

        @Test
        @DisplayName("셀러 등록 성공")
        void shouldRegisterSellerSuccessfully() {
            // Given
            RegisterSellerCommand command = createCommand();
            Seller seller = createSeller();
            SellerId savedId = SellerId.of(1L);

            when(sellerCommandFactory.create(any())).thenReturn(seller);
            when(sellerPersistenceManager.persist(any())).thenReturn(savedId);

            // When
            Long result = registerSellerService.execute(command);

            // Then
            assertNotNull(result);
            assertEquals(1L, result);
            verify(sellerCommandFactory).create(command);
            verify(sellerPersistenceManager).persist(seller);
        }

        @Test
        @DisplayName("Factory와 Manager가 순서대로 호출된다")
        void shouldCallFactoryAndManagerInOrder() {
            // Given
            RegisterSellerCommand command = createCommand();
            Seller seller = createSeller();
            SellerId savedId = SellerId.of(100L);

            when(sellerCommandFactory.create(any())).thenReturn(seller);
            when(sellerPersistenceManager.persist(any())).thenReturn(savedId);

            // When
            Long result = registerSellerService.execute(command);

            // Then
            assertEquals(100L, result);
            verify(sellerCommandFactory).create(command);
            verify(sellerPersistenceManager).persist(seller);
        }
    }

    // ========== Helper Methods ==========

    private RegisterSellerCommand createCommand() {
        return RegisterSellerCommand.of(
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

    private Seller createSeller() {
        return Seller.reconstitute(
                SellerId.of(1L),
                SellerName.of("테스트 셀러"),
                LogoUrl.of("https://example.com/logo.png"),
                Description.of("테스트 셀러 설명"),
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
