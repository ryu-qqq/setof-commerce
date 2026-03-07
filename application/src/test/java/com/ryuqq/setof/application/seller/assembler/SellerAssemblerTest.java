package com.ryuqq.setof.application.seller.assembler;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.application.seller.SellerDomainFixtures;
import com.ryuqq.setof.application.seller.dto.response.SellerPageResult;
import com.ryuqq.setof.application.seller.dto.response.SellerResult;
import com.ryuqq.setof.domain.seller.aggregate.Seller;
import java.util.Collections;
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
    @DisplayName("toResult() - Domain → Result 변환")
    class ToResultTest {

        @Test
        @DisplayName("Seller를 SellerResult로 변환한다")
        void toResult_ConvertsSeller_ReturnsResult() {
            // given
            Seller seller = SellerDomainFixtures.activeSeller();

            // when
            SellerResult result = sut.toResult(seller);

            // then
            assertThat(result).isNotNull();
            assertThat(result.id()).isEqualTo(seller.idValue());
            assertThat(result.sellerName()).isEqualTo(seller.sellerNameValue());
            assertThat(result.displayName()).isEqualTo(seller.displayNameValue());
            assertThat(result.logoUrl()).isEqualTo(seller.logoUrlValue());
            assertThat(result.description()).isEqualTo(seller.descriptionValue());
            assertThat(result.active()).isEqualTo(seller.isActive());
        }
    }

    @Nested
    @DisplayName("toResults() - Domain List → Result List 변환")
    class ToResultsTest {

        @Test
        @DisplayName("Seller 목록을 SellerResult 목록으로 변환한다")
        void toResults_ConvertsAllSellers_ReturnsResults() {
            // given
            List<Seller> sellers =
                    List.of(
                            SellerDomainFixtures.activeSeller(),
                            SellerDomainFixtures.inactiveSeller());

            // when
            List<SellerResult> results = sut.toResults(sellers);

            // then
            assertThat(results).hasSize(2);
        }

        @Test
        @DisplayName("빈 목록이면 빈 결과를 반환한다")
        void toResults_EmptyList_ReturnsEmptyList() {
            // given
            List<Seller> sellers = Collections.emptyList();

            // when
            List<SellerResult> results = sut.toResults(sellers);

            // then
            assertThat(results).isEmpty();
        }
    }

    @Nested
    @DisplayName("toPageResult() - Domain List → PageResult 변환")
    class ToPageResultTest {

        @Test
        @DisplayName("Domain 목록과 페이징 정보로 PageResult를 생성한다")
        void toPageResult_CreatePageResult() {
            // given
            List<Seller> sellers =
                    List.of(
                            SellerDomainFixtures.activeSeller(),
                            SellerDomainFixtures.inactiveSeller());
            int page = 0;
            int size = 20;
            long totalCount = 100L;

            // when
            SellerPageResult result = sut.toPageResult(sellers, page, size, totalCount);

            // then
            assertThat(result).isNotNull();
            assertThat(result.results()).hasSize(2);
            assertThat(result.pageMeta().totalElements()).isEqualTo(totalCount);
            assertThat(result.pageMeta().page()).isEqualTo(page);
            assertThat(result.pageMeta().size()).isEqualTo(size);
        }

        @Test
        @DisplayName("빈 목록으로 PageResult를 생성한다")
        void toPageResult_EmptyList_CreatesEmptyPageResult() {
            // given
            List<Seller> sellers = Collections.emptyList();
            int page = 0;
            int size = 20;
            long totalCount = 0L;

            // when
            SellerPageResult result = sut.toPageResult(sellers, page, size, totalCount);

            // then
            assertThat(result.results()).isEmpty();
            assertThat(result.pageMeta().totalElements()).isZero();
        }
    }
}
