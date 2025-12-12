package com.ryuqq.setof.adapter.out.persistence.seller.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.out.persistence.common.MapperTestSupport;
import com.ryuqq.setof.adapter.out.persistence.seller.entity.SellerCsInfoJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.seller.entity.SellerJpaEntity;
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

/**
 * SellerJpaEntityMapper 단위 테스트
 *
 * <p>Seller Domain ↔ SellerJpaEntity, SellerCsInfoJpaEntity 간의 변환 로직을 검증합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@DisplayName("SellerJpaEntityMapper 단위 테스트")
class SellerJpaEntityMapperTest extends MapperTestSupport {

    private static final Instant FIXED_TIME = Instant.parse("2025-01-01T00:00:00Z");

    private SellerJpaEntityMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new SellerJpaEntityMapper();
    }

    @Nested
    @DisplayName("toSellerEntity 메서드")
    class ToSellerEntity {

        @Test
        @DisplayName("성공 - Seller 도메인을 SellerEntity로 변환한다")
        void toSellerEntity_success() {
            // Given
            Seller seller = createFullSeller();

            // When
            SellerJpaEntity entity = mapper.toSellerEntity(seller);

            // Then
            assertThat(entity).isNotNull();
            assertThat(entity.getId()).isEqualTo(seller.getIdValue());
            assertThat(entity.getSellerName()).isEqualTo(seller.getNameValue());
            assertThat(entity.getLogoUrl()).isEqualTo(seller.getLogoUrlValue());
            assertThat(entity.getDescription()).isEqualTo(seller.getDescriptionValue());
            assertThat(entity.getApprovalStatus()).isEqualTo(seller.getApprovalStatusValue());
            assertThat(entity.getRegistrationNumber()).isEqualTo(seller.getRegistrationNumber());
            assertThat(entity.getSaleReportNumber()).isEqualTo(seller.getSaleReportNumber());
            assertThat(entity.getRepresentative()).isEqualTo(seller.getRepresentative());
            assertThat(entity.getBusinessAddressLine1())
                    .isEqualTo(seller.getBusinessAddressLine1());
            assertThat(entity.getBusinessAddressLine2())
                    .isEqualTo(seller.getBusinessAddressLine2());
            assertThat(entity.getBusinessZipCode()).isEqualTo(seller.getBusinessZipCode());
        }

        @Test
        @DisplayName("성공 - 선택적 필드가 없는 도메인을 Entity로 변환한다")
        void toSellerEntity_minimalFields_success() {
            // Given
            Seller minimalSeller = createMinimalSeller();

            // When
            SellerJpaEntity entity = mapper.toSellerEntity(minimalSeller);

            // Then
            assertThat(entity).isNotNull();
            assertThat(entity.getLogoUrl()).isNull();
            assertThat(entity.getDescription()).isNull();
            assertThat(entity.getBusinessAddressLine2()).isNull();
        }

        @Test
        @DisplayName("성공 - 삭제된 도메인을 Entity로 변환한다")
        void toSellerEntity_deleted_success() {
            // Given
            Seller deleted = createDeletedSeller();

            // When
            SellerJpaEntity entity = mapper.toSellerEntity(deleted);

            // Then
            assertThat(entity).isNotNull();
            assertThat(entity.getDeletedAt()).isNotNull();
        }
    }

    @Nested
    @DisplayName("toCsInfoEntity 메서드")
    class ToCsInfoEntity {

        @Test
        @DisplayName("성공 - Seller 도메인을 CsInfoEntity로 변환한다")
        void toCsInfoEntity_success() {
            // Given
            Seller seller = createFullSeller();

            // When
            SellerCsInfoJpaEntity entity = mapper.toCsInfoEntity(seller, null);

            // Then
            assertThat(entity).isNotNull();
            assertThat(entity.getId()).isNull();
            assertThat(entity.getSellerId()).isEqualTo(seller.getIdValue());
            assertThat(entity.getEmail()).isEqualTo(seller.getCsEmail());
            assertThat(entity.getMobilePhone()).isEqualTo(seller.getCsMobilePhone());
            assertThat(entity.getLandlinePhone()).isEqualTo(seller.getCsLandlinePhone());
        }

        @Test
        @DisplayName("성공 - 기존 CS Info ID로 Entity를 생성한다")
        void toCsInfoEntity_withExistingId_success() {
            // Given
            Seller seller = createFullSeller();
            Long existingCsInfoId = 100L;

            // When
            SellerCsInfoJpaEntity entity = mapper.toCsInfoEntity(seller, existingCsInfoId);

            // Then
            assertThat(entity).isNotNull();
            assertThat(entity.getId()).isEqualTo(existingCsInfoId);
        }

        @Test
        @DisplayName("성공 - CS 정보가 없는 도메인을 Entity로 변환한다")
        void toCsInfoEntity_noCsInfo_success() {
            // Given
            Seller seller = createMinimalSeller();

            // When
            SellerCsInfoJpaEntity entity = mapper.toCsInfoEntity(seller, null);

            // Then
            assertThat(entity).isNotNull();
            assertThat(entity.getEmail()).isNull();
            assertThat(entity.getMobilePhone()).isNull();
            assertThat(entity.getLandlinePhone()).isNull();
        }
    }

    @Nested
    @DisplayName("toDomain 메서드")
    class ToDomain {

        @Test
        @DisplayName("성공 - Entity들을 Seller 도메인으로 변환한다")
        void toDomain_success() {
            // Given
            SellerJpaEntity sellerEntity =
                    SellerJpaEntity.of(
                            1L,
                            "테스트 셀러",
                            "https://example.com/logo.png",
                            "셀러 설명입니다",
                            "APPROVED",
                            "123-45-67890",
                            "2024-서울강남-0001",
                            "홍길동",
                            "서울시 강남구 테헤란로 123",
                            "101동 1001호",
                            "06234",
                            FIXED_TIME,
                            FIXED_TIME,
                            null);

            SellerCsInfoJpaEntity csInfoEntity =
                    SellerCsInfoJpaEntity.of(
                            10L,
                            1L,
                            "cs@example.com",
                            "01012345678",
                            "0212345678",
                            FIXED_TIME,
                            FIXED_TIME);

            // When
            Seller domain = mapper.toDomain(sellerEntity, csInfoEntity);

            // Then
            assertThat(domain).isNotNull();
            assertThat(domain.getIdValue()).isEqualTo(sellerEntity.getId());
            assertThat(domain.getNameValue()).isEqualTo(sellerEntity.getSellerName());
            assertThat(domain.getLogoUrlValue()).isEqualTo(sellerEntity.getLogoUrl());
            assertThat(domain.getDescriptionValue()).isEqualTo(sellerEntity.getDescription());
            assertThat(domain.getApprovalStatusValue()).isEqualTo(sellerEntity.getApprovalStatus());
            assertThat(domain.getRegistrationNumber())
                    .isEqualTo(sellerEntity.getRegistrationNumber());
            assertThat(domain.getSaleReportNumber()).isEqualTo(sellerEntity.getSaleReportNumber());
            assertThat(domain.getRepresentative()).isEqualTo(sellerEntity.getRepresentative());
            assertThat(domain.getBusinessAddressLine1())
                    .isEqualTo(sellerEntity.getBusinessAddressLine1());
            assertThat(domain.getBusinessAddressLine2())
                    .isEqualTo(sellerEntity.getBusinessAddressLine2());
            assertThat(domain.getBusinessZipCode()).isEqualTo(sellerEntity.getBusinessZipCode());
            assertThat(domain.getCsEmail()).isEqualTo(csInfoEntity.getEmail());
            assertThat(domain.getCsMobilePhone()).isEqualTo(csInfoEntity.getMobilePhone());
            assertThat(domain.getCsLandlinePhone()).isEqualTo(csInfoEntity.getLandlinePhone());
        }

        @Test
        @DisplayName("성공 - CS Info가 null인 경우 도메인으로 변환한다")
        void toDomain_noCsInfo_success() {
            // Given
            SellerJpaEntity sellerEntity =
                    SellerJpaEntity.of(
                            2L,
                            "미니멀 셀러",
                            null,
                            null,
                            "PENDING",
                            "234-56-78901",
                            null,
                            "김철수",
                            "서울시 서초구 서초대로 456",
                            null,
                            "06789",
                            FIXED_TIME,
                            FIXED_TIME,
                            null);

            // When
            Seller domain = mapper.toDomain(sellerEntity, null);

            // Then
            assertThat(domain).isNotNull();
            assertThat(domain.getLogoUrlValue()).isNull();
            assertThat(domain.getDescriptionValue()).isNull();
            assertThat(domain.getCsEmail()).isNull();
            assertThat(domain.getCsMobilePhone()).isNull();
            assertThat(domain.getCsLandlinePhone()).isNull();
        }

        @Test
        @DisplayName("성공 - 삭제된 Entity를 도메인으로 변환한다")
        void toDomain_deleted_success() {
            // Given
            SellerJpaEntity sellerEntity =
                    SellerJpaEntity.of(
                            3L,
                            "삭제된 셀러",
                            null,
                            null,
                            "SUSPENDED",
                            "345-67-89012",
                            null,
                            "박영희",
                            "서울시 마포구 마포대로 789",
                            null,
                            "04001",
                            FIXED_TIME,
                            FIXED_TIME,
                            FIXED_TIME);

            // When
            Seller domain = mapper.toDomain(sellerEntity, null);

            // Then
            assertThat(domain).isNotNull();
            assertThat(domain.getDeletedAt()).isNotNull();
            assertThat(domain.isDeleted()).isTrue();
        }
    }

    @Nested
    @DisplayName("양방향 변환 검증")
    class RoundTrip {

        @Test
        @DisplayName("성공 - Domain -> Entity -> Domain 변환 시 데이터가 보존된다")
        void roundTrip_domainToEntityToDomain_preservesData() {
            // Given
            Seller original = createFullSeller();

            // When
            SellerJpaEntity sellerEntity = mapper.toSellerEntity(original);
            SellerCsInfoJpaEntity csInfoEntity = mapper.toCsInfoEntity(original, null);
            Seller converted = mapper.toDomain(sellerEntity, csInfoEntity);

            // Then
            assertThat(converted.getIdValue()).isEqualTo(original.getIdValue());
            assertThat(converted.getNameValue()).isEqualTo(original.getNameValue());
            assertThat(converted.getLogoUrlValue()).isEqualTo(original.getLogoUrlValue());
            assertThat(converted.getDescriptionValue()).isEqualTo(original.getDescriptionValue());
            assertThat(converted.getApprovalStatusValue())
                    .isEqualTo(original.getApprovalStatusValue());
            assertThat(converted.getRegistrationNumber())
                    .isEqualTo(original.getRegistrationNumber());
            assertThat(converted.getSaleReportNumber()).isEqualTo(original.getSaleReportNumber());
            assertThat(converted.getRepresentative()).isEqualTo(original.getRepresentative());
            assertThat(converted.getBusinessAddressLine1())
                    .isEqualTo(original.getBusinessAddressLine1());
            assertThat(converted.getBusinessAddressLine2())
                    .isEqualTo(original.getBusinessAddressLine2());
            assertThat(converted.getBusinessZipCode()).isEqualTo(original.getBusinessZipCode());
            assertThat(converted.getCsEmail()).isEqualTo(original.getCsEmail());
            assertThat(converted.getCsMobilePhone()).isEqualTo(original.getCsMobilePhone());
            assertThat(converted.getCsLandlinePhone()).isEqualTo(original.getCsLandlinePhone());
        }
    }

    // ========== Helper Methods ==========

    private Seller createFullSeller() {
        BusinessInfo businessInfo =
                BusinessInfo.of(
                        RegistrationNumber.of("123-45-67890"),
                        SaleReportNumber.of("2024-서울강남-0001"),
                        Representative.of("홍길동"),
                        BusinessAddress.of("서울시 강남구 테헤란로 123", "101동 1001호", "06234"));

        CustomerServiceInfo customerServiceInfo =
                CustomerServiceInfo.of(
                        CsEmail.of("cs@example.com"),
                        CsMobilePhone.of("01012345678"),
                        CsLandlinePhone.of("0212345678"));

        return Seller.reconstitute(
                SellerId.of(1L),
                SellerName.of("테스트 셀러"),
                LogoUrl.of("https://example.com/logo.png"),
                Description.of("셀러 설명입니다"),
                ApprovalStatus.APPROVED,
                businessInfo,
                customerServiceInfo,
                FIXED_TIME,
                FIXED_TIME,
                null);
    }

    private Seller createMinimalSeller() {
        BusinessInfo businessInfo =
                BusinessInfo.of(
                        RegistrationNumber.of("234-56-78901"),
                        null,
                        Representative.of("김철수"),
                        BusinessAddress.of("서울시 서초구 서초대로 456", null, "06789"));

        return Seller.reconstitute(
                SellerId.of(2L),
                SellerName.of("미니멀 셀러"),
                null,
                null,
                ApprovalStatus.PENDING,
                businessInfo,
                null,
                FIXED_TIME,
                FIXED_TIME,
                null);
    }

    private Seller createDeletedSeller() {
        BusinessInfo businessInfo =
                BusinessInfo.of(
                        RegistrationNumber.of("345-67-89012"),
                        null,
                        Representative.of("박영희"),
                        BusinessAddress.of("서울시 마포구 마포대로 789", null, "04001"));

        return Seller.reconstitute(
                SellerId.of(3L),
                SellerName.of("삭제된 셀러"),
                null,
                null,
                ApprovalStatus.SUSPENDED,
                businessInfo,
                null,
                FIXED_TIME,
                FIXED_TIME,
                FIXED_TIME);
    }
}
