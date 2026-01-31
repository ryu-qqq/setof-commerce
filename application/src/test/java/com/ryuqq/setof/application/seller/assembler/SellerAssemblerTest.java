package com.ryuqq.setof.application.seller.assembler;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.application.seller.dto.response.SellerAddressResult;
import com.ryuqq.setof.application.seller.dto.response.SellerAdminResult;
import com.ryuqq.setof.application.seller.dto.response.SellerBusinessInfoResult;
import com.ryuqq.setof.application.seller.dto.response.SellerCustomerResult;
import com.ryuqq.setof.application.seller.dto.response.SellerPageResult;
import com.ryuqq.setof.application.seller.dto.response.SellerResult;
import com.ryuqq.setof.domain.seller.SellerFixtures;
import com.ryuqq.setof.domain.seller.aggregate.Seller;
import com.ryuqq.setof.domain.seller.aggregate.SellerAddress;
import com.ryuqq.setof.domain.seller.aggregate.SellerBusinessInfo;
import com.ryuqq.setof.domain.seller.aggregate.SellerCs;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("SellerAssembler 단위 테스트")
class SellerAssemblerTest {

    private final SellerAssembler sut = new SellerAssembler();

    @Nested
    @DisplayName("toResult() - Seller Domain → SellerResult 변환")
    class ToResultTest {

        @Test
        @DisplayName("활성 Seller를 SellerResult로 변환한다")
        void toResult_ActiveSeller_ReturnsSellerResult() {
            // given
            Seller seller = SellerFixtures.activeSeller();

            // when
            SellerResult result = sut.toResult(seller);

            // then
            assertThat(result).isNotNull();
            assertThat(result.id()).isEqualTo(seller.idValue());
            assertThat(result.sellerName()).isEqualTo(seller.sellerNameValue());
            assertThat(result.displayName()).isEqualTo(seller.displayNameValue());
            assertThat(result.logoUrl()).isEqualTo(seller.logoUrlValue());
            assertThat(result.description()).isEqualTo(seller.descriptionValue());
            assertThat(result.active()).isTrue();
            assertThat(result.createdAt()).isEqualTo(seller.createdAt());
            assertThat(result.updatedAt()).isEqualTo(seller.updatedAt());
        }

        @Test
        @DisplayName("비활성 Seller를 변환하면 active가 false이다")
        void toResult_InactiveSeller_ReturnsInactiveResult() {
            // given
            Seller seller = SellerFixtures.inactiveSeller();

            // when
            SellerResult result = sut.toResult(seller);

            // then
            assertThat(result.active()).isFalse();
        }
    }

    @Nested
    @DisplayName("toResults() - Seller List → SellerResult List 변환")
    class ToResultsTest {

        @Test
        @DisplayName("Seller 목록을 SellerResult 목록으로 변환한다")
        void toResults_ReturnsList() {
            // given
            List<Seller> sellers =
                    List.of(
                            SellerFixtures.activeSeller(1L),
                            SellerFixtures.activeSeller(2L),
                            SellerFixtures.inactiveSeller());

            // when
            List<SellerResult> results = sut.toResults(sellers);

            // then
            assertThat(results).hasSize(3);
            assertThat(results.get(0).id()).isEqualTo(1L);
            assertThat(results.get(1).id()).isEqualTo(2L);
            assertThat(results.get(2).active()).isFalse();
        }

        @Test
        @DisplayName("빈 목록을 변환하면 빈 결과를 반환한다")
        void toResults_EmptyList_ReturnsEmpty() {
            // given
            List<Seller> sellers = List.of();

            // when
            List<SellerResult> results = sut.toResults(sellers);

            // then
            assertThat(results).isEmpty();
        }
    }

    @Nested
    @DisplayName("toBusinessInfoResult() - SellerBusinessInfo → SellerBusinessInfoResult 변환")
    class ToBusinessInfoResultTest {

        @Test
        @DisplayName("SellerBusinessInfo를 SellerBusinessInfoResult로 변환한다")
        void toBusinessInfoResult_ReturnsResult() {
            // given
            SellerBusinessInfo businessInfo = SellerFixtures.activeSellerBusinessInfo();

            // when
            SellerBusinessInfoResult result = sut.toBusinessInfoResult(businessInfo);

            // then
            assertThat(result).isNotNull();
            assertThat(result.id()).isEqualTo(businessInfo.idValue());
            assertThat(result.sellerId()).isEqualTo(businessInfo.sellerIdValue());
            assertThat(result.registrationNumber())
                    .isEqualTo(businessInfo.registrationNumberValue());
            assertThat(result.companyName()).isEqualTo(businessInfo.companyNameValue());
            assertThat(result.representative()).isEqualTo(businessInfo.representativeValue());
            assertThat(result.saleReportNumber()).isEqualTo(businessInfo.saleReportNumberValue());
            assertThat(result.businessAddress()).isNotNull();
            assertThat(result.businessAddress().zipCode())
                    .isEqualTo(businessInfo.businessAddressZipCode());
        }
    }

    @Nested
    @DisplayName("toAddressResult() - SellerAddress → SellerAddressResult 변환")
    class ToAddressResultTest {

        @Test
        @DisplayName("배송 주소를 SellerAddressResult로 변환한다")
        void toAddressResult_ShippingAddress_ReturnsResult() {
            // given
            SellerAddress address = SellerFixtures.activeShippingAddress();

            // when
            SellerAddressResult result = sut.toAddressResult(address);

            // then
            assertThat(result).isNotNull();
            assertThat(result.id()).isEqualTo(address.idValue());
            assertThat(result.sellerId()).isEqualTo(address.sellerIdValue());
            assertThat(result.addressType()).isEqualTo(address.addressType().name());
            assertThat(result.addressName()).isEqualTo(address.addressNameValue());
            assertThat(result.defaultAddress()).isEqualTo(address.isDefaultAddress());
            assertThat(result.address()).isNotNull();
            assertThat(result.address().zipCode()).isEqualTo(address.addressZipCode());
            assertThat(result.contactInfo()).isNotNull();
            assertThat(result.contactInfo().name()).isEqualTo(address.contactInfoName());
        }

        @Test
        @DisplayName("반품 주소를 SellerAddressResult로 변환한다")
        void toAddressResult_ReturnAddress_ReturnsResult() {
            // given
            SellerAddress address = SellerFixtures.activeReturnAddress();

            // when
            SellerAddressResult result = sut.toAddressResult(address);

            // then
            assertThat(result).isNotNull();
            assertThat(result.addressType()).isEqualTo("RETURN");
        }
    }

    @Nested
    @DisplayName("toAdminResult() - 어드민용 상세 조회 결과 생성")
    class ToAdminResultTest {

        @Test
        @DisplayName("Seller, BusinessInfo, Address를 조합하여 AdminResult를 생성한다")
        void toAdminResult_ReturnsCompositeResult() {
            // given
            Seller seller = SellerFixtures.activeSeller();
            SellerBusinessInfo businessInfo = SellerFixtures.activeSellerBusinessInfo();
            SellerAddress address = SellerFixtures.activeShippingAddress();

            // when
            SellerAdminResult result = sut.toAdminResult(seller, businessInfo, address);

            // then
            assertThat(result).isNotNull();
            assertThat(result.seller()).isNotNull();
            assertThat(result.seller().id()).isEqualTo(seller.idValue());
            assertThat(result.businessInfo()).isNotNull();
            assertThat(result.businessInfo().id()).isEqualTo(businessInfo.idValue());
            assertThat(result.address()).isNotNull();
            assertThat(result.address().id()).isEqualTo(address.idValue());
        }
    }

    @Nested
    @DisplayName("toCustomerResult() - 고객용 조회 결과 생성")
    class ToCustomerResultTest {

        @Test
        @DisplayName("Seller, BusinessInfo, SellerCs로 CustomerResult를 생성한다")
        void toCustomerResult_ReturnsResult() {
            // given
            Seller seller = SellerFixtures.activeSeller();
            SellerBusinessInfo businessInfo = SellerFixtures.activeSellerBusinessInfo();
            SellerCs sellerCs = SellerFixtures.activeSellerCs();

            // when
            SellerCustomerResult result = sut.toCustomerResult(seller, businessInfo, sellerCs);

            // then
            assertThat(result).isNotNull();
            assertThat(result.id()).isEqualTo(seller.idValue());
            assertThat(result.displayName()).isEqualTo(seller.displayNameValue());
            assertThat(result.logoUrl()).isEqualTo(seller.logoUrlValue());
            assertThat(result.description()).isEqualTo(seller.descriptionValue());
            assertThat(result.companyName()).isEqualTo(businessInfo.companyNameValue());
            assertThat(result.representative()).isEqualTo(businessInfo.representativeValue());
            assertThat(result.csPhone()).isEqualTo(sellerCs.csPhone());
            assertThat(result.csEmail()).isEqualTo(sellerCs.csEmail());
        }

        @Test
        @DisplayName("BusinessInfo와 SellerCs가 null이면 해당 필드들이 null이다")
        void toCustomerResult_NullBusinessInfoAndCs_ReturnsPartialResult() {
            // given
            Seller seller = SellerFixtures.activeSeller();

            // when
            SellerCustomerResult result = sut.toCustomerResult(seller, null, null);

            // then
            assertThat(result).isNotNull();
            assertThat(result.id()).isEqualTo(seller.idValue());
            assertThat(result.displayName()).isEqualTo(seller.displayNameValue());
            assertThat(result.companyName()).isNull();
            assertThat(result.representative()).isNull();
            assertThat(result.csPhone()).isNull();
            assertThat(result.csEmail()).isNull();
        }
    }

    @Nested
    @DisplayName("toPageResult() - 페이지 결과 생성")
    class ToPageResultTest {

        @Test
        @DisplayName("Seller 목록으로 PageResult를 생성한다")
        void toPageResult_ReturnsPageResult() {
            // given
            List<Seller> sellers = List.of(SellerFixtures.activeSeller());
            int page = 0;
            int size = 20;
            long totalCount = 1L;

            // when
            SellerPageResult result = sut.toPageResult(sellers, page, size, totalCount);

            // then
            assertThat(result).isNotNull();
            assertThat(result.content()).hasSize(1);
            assertThat(result.totalCount()).isEqualTo(totalCount);
            assertThat(result.page()).isEqualTo(page);
            assertThat(result.size()).isEqualTo(size);
            assertThat(result.hasNext()).isFalse();
        }

        @Test
        @DisplayName("빈 목록으로 빈 PageResult를 생성한다")
        void toPageResult_EmptyList_ReturnsEmptyPageResult() {
            // given
            List<Seller> sellers = List.of();
            int page = 0;
            int size = 20;
            long totalCount = 0L;

            // when
            SellerPageResult result = sut.toPageResult(sellers, page, size, totalCount);

            // then
            assertThat(result.content()).isEmpty();
            assertThat(result.totalCount()).isZero();
        }

        @Test
        @DisplayName("다음 페이지가 있으면 hasNext가 true이다")
        void toPageResult_HasMorePages_HasNextIsTrue() {
            // given
            List<Seller> sellers =
                    List.of(SellerFixtures.activeSeller(1L), SellerFixtures.activeSeller(2L));
            int page = 0;
            int size = 2;
            long totalCount = 10L;

            // when
            SellerPageResult result = sut.toPageResult(sellers, page, size, totalCount);

            // then
            assertThat(result.hasNext()).isTrue();
        }

        @Test
        @DisplayName("마지막 페이지이면 hasNext가 false이다")
        void toPageResult_LastPage_HasNextIsFalse() {
            // given
            List<Seller> sellers = List.of(SellerFixtures.activeSeller(1L));
            int page = 4;
            int size = 2;
            long totalCount = 10L;

            // when
            SellerPageResult result = sut.toPageResult(sellers, page, size, totalCount);

            // then
            assertThat(result.hasNext()).isFalse();
        }
    }
}
