package com.ryuqq.setof.adapter.in.rest.admin.v1.discount.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.in.rest.admin.discount.DiscountPolicyApiFixtures;
import com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.request.DiscountPolicySearchV1ApiRequest;
import com.ryuqq.setof.application.discount.dto.query.DiscountPolicySearchParams;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * DiscountPolicyQueryApiMapper 단위 테스트.
 *
 * <p>v1 레거시 Discount Query API Mapper의 변환 로직을 테스트합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@DisplayName("DiscountPolicyQueryApiMapper 단위 테스트")
class DiscountPolicyQueryApiMapperTest {

    private DiscountPolicyQueryApiMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new DiscountPolicyQueryApiMapper();
    }

    @Nested
    @DisplayName("toSearchParams(DiscountPolicySearchV1ApiRequest)")
    class ToSearchParamsTest {

        @Test
        @DisplayName("기본 검색 요청을 DiscountPolicySearchParams로 변환한다")
        void toSearchParams_Default_Success() {
            // given
            DiscountPolicySearchV1ApiRequest request = DiscountPolicyApiFixtures.searchRequest();

            // when
            DiscountPolicySearchParams params = mapper.toSearchParams(request);

            // then
            assertThat(params.page()).isEqualTo(0);
            assertThat(params.size()).isEqualTo(20);
            assertThat(params.sortKey()).isEqualTo("id");
            assertThat(params.sortDirection()).isEqualTo("DESC");
        }

        @Test
        @DisplayName("activeYn Y를 active=true로 변환한다")
        void toSearchParams_ActiveYnY_ActiveTrue() {
            // given
            DiscountPolicySearchV1ApiRequest request =
                    DiscountPolicyApiFixtures.searchRequestWithActiveFilter("Y");

            // when
            DiscountPolicySearchParams params = mapper.toSearchParams(request);

            // then
            assertThat(params.active()).isTrue();
        }

        @Test
        @DisplayName("activeYn N을 active=false로 변환한다")
        void toSearchParams_ActiveYnN_ActiveFalse() {
            // given
            DiscountPolicySearchV1ApiRequest request =
                    DiscountPolicyApiFixtures.searchRequestWithActiveFilter("N");

            // when
            DiscountPolicySearchParams params = mapper.toSearchParams(request);

            // then
            assertThat(params.active()).isFalse();
        }

        @Test
        @DisplayName("activeYn null이면 active=null로 변환한다")
        void toSearchParams_ActiveYnNull_ActiveNull() {
            // given
            DiscountPolicySearchV1ApiRequest request = DiscountPolicyApiFixtures.searchRequest();

            // when
            DiscountPolicySearchParams params = mapper.toSearchParams(request);

            // then
            assertThat(params.active()).isNull();
        }

        @Test
        @DisplayName("publisherType을 그대로 전달한다")
        void toSearchParams_PublisherType_PassThrough() {
            // given
            DiscountPolicySearchV1ApiRequest request =
                    DiscountPolicyApiFixtures.searchRequestWithPublisherType("ADMIN");

            // when
            DiscountPolicySearchParams params = mapper.toSearchParams(request);

            // then
            assertThat(params.publisherType()).isEqualTo("ADMIN");
        }

        @Test
        @DisplayName("issueType은 applicationType으로 매핑 불가하여 null로 변환한다")
        void toSearchParams_IssueType_MappedToNull() {
            // given
            DiscountPolicySearchV1ApiRequest request = DiscountPolicyApiFixtures.searchRequest();

            // when
            DiscountPolicySearchParams params = mapper.toSearchParams(request);

            // then
            assertThat(params.applicationType()).isNull();
        }

        @Test
        @DisplayName("page/size null이면 withDefaults() 기본값이 적용된다")
        void toSearchParams_NullPage_WithDefaults() {
            // given
            DiscountPolicySearchV1ApiRequest request =
                    DiscountPolicyApiFixtures.searchRequestWithNullPage();

            // when
            DiscountPolicySearchParams params = mapper.toSearchParams(request);

            // then
            assertThat(params.page()).isEqualTo(0);
            assertThat(params.size()).isEqualTo(20);
            assertThat(params.sortKey()).isEqualTo("id");
            assertThat(params.sortDirection()).isEqualTo("DESC");
        }

        @Test
        @DisplayName("sellerId는 항상 null로 변환한다")
        void toSearchParams_SellerId_AlwaysNull() {
            // given
            DiscountPolicySearchV1ApiRequest request = DiscountPolicyApiFixtures.searchRequest();

            // when
            DiscountPolicySearchParams params = mapper.toSearchParams(request);

            // then
            assertThat(params.sellerId()).isNull();
        }

        @Test
        @DisplayName("stackingGroup은 항상 null로 변환한다")
        void toSearchParams_StackingGroup_AlwaysNull() {
            // given
            DiscountPolicySearchV1ApiRequest request = DiscountPolicyApiFixtures.searchRequest();

            // when
            DiscountPolicySearchParams params = mapper.toSearchParams(request);

            // then
            assertThat(params.stackingGroup()).isNull();
        }
    }
}
