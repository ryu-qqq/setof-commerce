package com.ryuqq.setof.application.brand.assembler;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.application.brand.dto.response.BrandDisplayResult;
import com.ryuqq.setof.application.brand.dto.response.BrandPageResult;
import com.ryuqq.setof.application.brand.dto.response.BrandResult;
import com.ryuqq.setof.domain.brand.BrandFixtures;
import com.ryuqq.setof.domain.brand.aggregate.Brand;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("BrandAssembler 단위 테스트")
class BrandAssemblerTest {

    private final BrandAssembler sut = new BrandAssembler();

    @Nested
    @DisplayName("toResult() - Domain → Result 변환")
    class ToResultTest {

        @Test
        @DisplayName("Brand를 BrandResult로 변환한다")
        void toResult_ConvertsToResult() {
            // given
            Brand domain = BrandFixtures.activeBrand();

            // when
            BrandResult result = sut.toResult(domain);

            // then
            assertThat(result).isNotNull();
            assertThat(result.brandId()).isEqualTo(domain.idValue());
            assertThat(result.brandName()).isEqualTo(domain.brandNameValue());
        }
    }

    @Nested
    @DisplayName("toResults() - Domain List → Result List 변환")
    class ToResultsTest {

        @Test
        @DisplayName("Brand 목록을 BrandResult 목록으로 변환한다")
        void toResults_ConvertsAllToResults() {
            // given
            List<Brand> domains =
                    List.of(BrandFixtures.activeBrand(), BrandFixtures.inactiveBrand());

            // when
            List<BrandResult> results = sut.toResults(domains);

            // then
            assertThat(results).hasSize(2);
        }

        @Test
        @DisplayName("빈 목록이면 빈 결과를 반환한다")
        void toResults_EmptyList_ReturnsEmptyList() {
            // given
            List<Brand> domains = Collections.emptyList();

            // when
            List<BrandResult> results = sut.toResults(domains);

            // then
            assertThat(results).isEmpty();
        }
    }

    @Nested
    @DisplayName("toPageResult() - Domain List → PageResult 변환")
    class ToPageResultTest {

        @Test
        @DisplayName("Domain 목록과 페이징 정보로 PageResult를 생성한다")
        void toPageResult_CreatesPageResult() {
            // given
            List<Brand> domains =
                    List.of(BrandFixtures.activeBrand(), BrandFixtures.inactiveBrand());
            int page = 0;
            int size = 20;
            long totalCount = 100L;

            // when
            BrandPageResult result = sut.toPageResult(domains, page, size, totalCount);

            // then
            assertThat(result).isNotNull();
            assertThat(result.content()).hasSize(2);
            assertThat(result.pageMeta().totalElements()).isEqualTo(totalCount);
            assertThat(result.pageMeta().page()).isEqualTo(page);
            assertThat(result.pageMeta().size()).isEqualTo(size);
        }

        @Test
        @DisplayName("빈 목록으로 PageResult를 생성한다")
        void toPageResult_EmptyList_CreatesEmptyPageResult() {
            // given
            List<Brand> domains = Collections.emptyList();
            int page = 0;
            int size = 20;
            long totalCount = 0L;

            // when
            BrandPageResult result = sut.toPageResult(domains, page, size, totalCount);

            // then
            assertThat(result.content()).isEmpty();
            assertThat(result.pageMeta().totalElements()).isZero();
        }
    }

    @Nested
    @DisplayName("toDisplayResult() - Domain → DisplayResult 변환")
    class ToDisplayResultTest {

        @Test
        @DisplayName("Brand를 BrandDisplayResult로 변환한다")
        void toDisplayResult_ConvertsToDisplayResult() {
            // given
            Brand domain = BrandFixtures.activeBrand();

            // when
            BrandDisplayResult result = sut.toDisplayResult(domain);

            // then
            assertThat(result).isNotNull();
            assertThat(result.brandId()).isEqualTo(domain.idValue());
            assertThat(result.brandName()).isEqualTo(domain.brandNameValue());
        }
    }

    @Nested
    @DisplayName("toDisplayResults() - Domain List → DisplayResult List 변환")
    class ToDisplayResultsTest {

        @Test
        @DisplayName("Brand 목록을 BrandDisplayResult 목록으로 변환한다")
        void toDisplayResults_ConvertsAllToDisplayResults() {
            // given
            List<Brand> domains =
                    List.of(BrandFixtures.activeBrand(), BrandFixtures.inactiveBrand());

            // when
            List<BrandDisplayResult> results = sut.toDisplayResults(domains);

            // then
            assertThat(results).hasSize(2);
        }

        @Test
        @DisplayName("빈 목록이면 빈 DisplayResult를 반환한다")
        void toDisplayResults_EmptyList_ReturnsEmptyList() {
            // given
            List<Brand> domains = Collections.emptyList();

            // when
            List<BrandDisplayResult> results = sut.toDisplayResults(domains);

            // then
            assertThat(results).isEmpty();
        }
    }
}
