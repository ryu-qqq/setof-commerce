package com.ryuqq.setof.application.seller.service.query;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.seller.SellerDomainFixtures;
import com.ryuqq.setof.application.seller.SellerQueryFixtures;
import com.ryuqq.setof.application.seller.assembler.SellerAssembler;
import com.ryuqq.setof.application.seller.dto.query.SellerSearchParams;
import com.ryuqq.setof.application.seller.dto.response.SellerPageResult;
import com.ryuqq.setof.application.seller.factory.SellerQueryFactory;
import com.ryuqq.setof.application.seller.manager.SellerReadManager;
import com.ryuqq.setof.domain.seller.aggregate.Seller;
import com.ryuqq.setof.domain.seller.query.SellerSearchCriteria;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("SearchSellerByOffsetService 단위 테스트")
class SearchSellerByOffsetServiceTest {

    @InjectMocks private SearchSellerByOffsetService sut;

    @Mock private SellerReadManager sellerReadManager;
    @Mock private SellerQueryFactory sellerQueryFactory;
    @Mock private SellerAssembler sellerAssembler;

    @Nested
    @DisplayName("execute() - 오프셋 기반 셀러 검색")
    class ExecuteTest {

        @Test
        @DisplayName("검색 조건으로 셀러 목록을 조회하고 PageResult를 반환한다")
        void execute_ValidParams_ReturnsPageResult() {
            // given
            SellerSearchParams params = SellerQueryFixtures.searchParams();
            SellerSearchCriteria criteria = SellerSearchCriteria.defaultCriteria();
            List<Seller> sellers = SellerDomainFixtures.activeSellers();
            long totalCount = 2L;
            SellerPageResult expected = SellerQueryFixtures.sellerPageResult();

            given(sellerQueryFactory.createCriteria(params)).willReturn(criteria);
            given(sellerReadManager.findByCriteria(criteria)).willReturn(sellers);
            given(sellerReadManager.countByCriteria(criteria)).willReturn(totalCount);
            given(sellerAssembler.toPageResult(sellers, params.page(), params.size(), totalCount))
                    .willReturn(expected);

            // when
            SellerPageResult result = sut.execute(params);

            // then
            assertThat(result).isEqualTo(expected);
            then(sellerQueryFactory).should().createCriteria(params);
            then(sellerReadManager).should().findByCriteria(criteria);
            then(sellerReadManager).should().countByCriteria(criteria);
            then(sellerAssembler)
                    .should()
                    .toPageResult(sellers, params.page(), params.size(), totalCount);
        }

        @Test
        @DisplayName("검색 결과가 없으면 빈 PageResult를 반환한다")
        void execute_NoResults_ReturnsEmptyPageResult() {
            // given
            SellerSearchParams params = SellerQueryFixtures.searchParams();
            SellerSearchCriteria criteria = SellerSearchCriteria.defaultCriteria();
            List<Seller> emptyList = Collections.emptyList();
            long totalCount = 0L;
            SellerPageResult expected = SellerQueryFixtures.emptySellerPageResult();

            given(sellerQueryFactory.createCriteria(params)).willReturn(criteria);
            given(sellerReadManager.findByCriteria(criteria)).willReturn(emptyList);
            given(sellerReadManager.countByCriteria(criteria)).willReturn(totalCount);
            given(sellerAssembler.toPageResult(emptyList, params.page(), params.size(), totalCount))
                    .willReturn(expected);

            // when
            SellerPageResult result = sut.execute(params);

            // then
            assertThat(result.results()).isEmpty();
            assertThat(result.pageMeta().totalElements()).isZero();
        }
    }
}
