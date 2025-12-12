package com.ryuqq.setof.adapter.out.persistence.seller.adapter;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.out.persistence.common.RepositoryTestSupport;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * SellerPersistenceAdapter 통합 테스트
 *
 * <p>SellerPersistencePort 구현체의 저장 기능을 검증합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@DisplayName("SellerPersistenceAdapter 통합 테스트")
class SellerPersistenceAdapterTest extends RepositoryTestSupport {

    @Autowired private SellerPersistenceAdapter sellerPersistenceAdapter;

    private static final Instant NOW = Instant.parse("2025-01-01T00:00:00Z");

    @Nested
    @DisplayName("persist 메서드")
    class Persist {

        @Test
        @DisplayName("성공 - 새 Seller를 저장하고 ID를 반환한다")
        void persist_newSeller_savesAndReturnsId() {
            // Given
            Seller newSeller = createNewSeller();

            // When
            SellerId savedId = sellerPersistenceAdapter.persist(newSeller);
            flushAndClear();

            // Then
            assertThat(savedId).isNotNull();
            assertThat(savedId.value()).isNotNull();

            SellerJpaEntity foundSeller = find(SellerJpaEntity.class, savedId.value());
            assertThat(foundSeller).isNotNull();
            assertThat(foundSeller.getSellerName()).isEqualTo("테스트 셀러");
            assertThat(foundSeller.getApprovalStatus()).isEqualTo("PENDING");
        }

        @Test
        @DisplayName("성공 - 기존 Seller를 수정한다")
        void persist_existingSeller_updates() {
            // Given
            SellerJpaEntity existingEntity =
                    persistAndFlush(
                            SellerJpaEntity.of(
                                    null,
                                    "기존 셀러",
                                    null,
                                    null,
                                    "PENDING",
                                    "123-45-67890",
                                    null,
                                    "홍길동",
                                    "서울시 강남구 테헤란로 123",
                                    null,
                                    "06234",
                                    NOW,
                                    NOW,
                                    null));
            flushAndClear();

            Seller updatedSeller = createSellerWithId(existingEntity.getId());

            // When
            SellerId savedId = sellerPersistenceAdapter.persist(updatedSeller);
            flushAndClear();

            // Then
            assertThat(savedId.value()).isEqualTo(existingEntity.getId());
            SellerJpaEntity foundSeller = find(SellerJpaEntity.class, savedId.value());
            assertThat(foundSeller).isNotNull();
        }

        @Test
        @DisplayName("성공 - CS 정보 없이 Seller를 저장한다")
        void persist_sellerWithoutCsInfo_saves() {
            // Given
            Seller sellerWithoutCsInfo = createSellerWithoutCsInfo();

            // When
            SellerId savedId = sellerPersistenceAdapter.persist(sellerWithoutCsInfo);
            flushAndClear();

            // Then
            SellerJpaEntity foundSeller = find(SellerJpaEntity.class, savedId.value());
            assertThat(foundSeller).isNotNull();
            assertThat(foundSeller.getSellerName()).isEqualTo("심플 셀러");
        }

        @Test
        @DisplayName("성공 - 삭제된 Seller를 저장한다 (soft delete)")
        void persist_deletedSeller_savesSoftDeleted() {
            // Given
            Seller deletedSeller = createDeletedSeller();

            // When
            SellerId savedId = sellerPersistenceAdapter.persist(deletedSeller);
            flushAndClear();

            // Then
            SellerJpaEntity foundSeller = find(SellerJpaEntity.class, savedId.value());
            assertThat(foundSeller).isNotNull();
            assertThat(foundSeller.getDeletedAt()).isNotNull();
        }
    }

    // ========== Helper Methods ==========

    private Seller createNewSeller() {
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

        return Seller.create(
                SellerName.of("테스트 셀러"),
                LogoUrl.of("https://example.com/logo.png"),
                Description.of("셀러 설명입니다"),
                businessInfo,
                customerServiceInfo,
                NOW);
    }

    private Seller createSellerWithId(Long id) {
        BusinessInfo businessInfo =
                BusinessInfo.of(
                        RegistrationNumber.of("123-45-67890"),
                        SaleReportNumber.of("2024-서울강남-0001"),
                        Representative.of("홍길동"),
                        BusinessAddress.of("서울시 강남구 테헤란로 123", "101동 1001호", "06234"));

        CustomerServiceInfo customerServiceInfo =
                CustomerServiceInfo.of(
                        CsEmail.of("updated@example.com"), CsMobilePhone.of("01087654321"), null);

        return Seller.reconstitute(
                SellerId.of(id),
                SellerName.of("수정된 셀러"),
                LogoUrl.of("https://example.com/new-logo.png"),
                Description.of("수정된 설명"),
                ApprovalStatus.APPROVED,
                businessInfo,
                customerServiceInfo,
                NOW,
                NOW,
                null);
    }

    private Seller createSellerWithoutCsInfo() {
        BusinessInfo businessInfo =
                BusinessInfo.of(
                        RegistrationNumber.of("234-56-78901"),
                        null,
                        Representative.of("김철수"),
                        BusinessAddress.of("서울시 서초구 서초대로 456", null, "06789"));

        return Seller.create(SellerName.of("심플 셀러"), null, null, businessInfo, null, NOW);
    }

    private Seller createDeletedSeller() {
        BusinessInfo businessInfo =
                BusinessInfo.of(
                        RegistrationNumber.of("345-67-89012"),
                        null,
                        Representative.of("박영희"),
                        BusinessAddress.of("서울시 마포구 마포대로 789", null, "04001"));

        return Seller.reconstitute(
                null,
                SellerName.of("삭제된 셀러"),
                null,
                null,
                ApprovalStatus.SUSPENDED,
                businessInfo,
                null,
                NOW,
                NOW,
                NOW);
    }
}
