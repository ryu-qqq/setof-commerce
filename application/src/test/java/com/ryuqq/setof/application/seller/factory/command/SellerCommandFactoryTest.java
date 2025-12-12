package com.ryuqq.setof.application.seller.factory.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import com.ryuqq.setof.application.seller.assembler.SellerAssembler;
import com.ryuqq.setof.application.seller.dto.command.RegisterSellerCommand;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import com.ryuqq.setof.domain.seller.aggregate.Seller;
import com.ryuqq.setof.domain.seller.vo.ApprovalStatus;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * SellerCommandFactory 테스트
 *
 * <p>Command → Domain 변환에 대한 단위 테스트
 */
@DisplayName("SellerCommandFactory")
@ExtendWith(MockitoExtension.class)
class SellerCommandFactoryTest {

    private static final Instant FIXED_TIME = Instant.parse("2025-01-01T00:00:00Z");
    private static final Clock FIXED_CLOCK = Clock.fixed(FIXED_TIME, ZoneId.of("UTC"));

    @Mock private ClockHolder clockHolder;

    private SellerAssembler sellerAssembler;
    private SellerCommandFactory sellerCommandFactory;

    @BeforeEach
    void setUp() {
        sellerAssembler = new SellerAssembler();
        sellerCommandFactory = new SellerCommandFactory(sellerAssembler, clockHolder);
    }

    @Nested
    @DisplayName("create")
    class CreateTest {

        @Test
        @DisplayName("모든 필드가 있는 Command로 Seller 생성 성공")
        void shouldCreateSellerWithAllFields() {
            // Given
            RegisterSellerCommand command = createFullCommand();
            when(clockHolder.getClock()).thenReturn(FIXED_CLOCK);

            // When
            Seller seller = sellerCommandFactory.create(command);

            // Then
            assertNotNull(seller);
            assertEquals("테스트 셀러", seller.getNameValue());
            assertEquals("https://example.com/logo.png", seller.getLogoUrlValue());
            assertEquals("테스트 셀러 설명", seller.getDescriptionValue());
            assertEquals(ApprovalStatus.PENDING, seller.getApprovalStatus());
            assertFalse(seller.isActive());
            assertFalse(seller.isDeleted());

            // Business Info
            assertEquals("1234567890", seller.getRegistrationNumber());
            assertEquals("2024-서울강남-1234", seller.getSaleReportNumber());
            assertEquals("홍길동", seller.getRepresentative());

            // CS Info
            assertEquals("cs@example.com", seller.getCsEmail());
            assertEquals("01012345678", seller.getCsMobilePhone());
            assertEquals("021234567", seller.getCsLandlinePhone());
        }

        @Test
        @DisplayName("선택 필드가 없는 Command로 Seller 생성 성공")
        void shouldCreateSellerWithMinimalFields() {
            // Given
            RegisterSellerCommand command = createMinimalCommand();
            when(clockHolder.getClock()).thenReturn(FIXED_CLOCK);

            // When
            Seller seller = sellerCommandFactory.create(command);

            // Then
            assertNotNull(seller);
            assertEquals("최소 셀러", seller.getNameValue());
            assertEquals(ApprovalStatus.PENDING, seller.getApprovalStatus());
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
                null,
                null,
                null);
    }
}
