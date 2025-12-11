package com.ryuqq.setof.adapter.out.persistence.brand.adapter;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.out.persistence.brand.entity.BrandJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.common.RepositoryTestSupport;
import com.ryuqq.setof.application.brand.port.out.query.BrandQueryPort;
import com.ryuqq.setof.domain.brand.aggregate.Brand;
import com.ryuqq.setof.domain.brand.query.criteria.BrandSearchCriteria;
import com.ryuqq.setof.domain.brand.vo.BrandCode;
import com.ryuqq.setof.domain.brand.vo.BrandId;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * BrandQueryAdapter 통합 테스트
 *
 * <p>BrandQueryPort 구현체의 통합 동작을 검증합니다.
 *
 * <p><strong>테스트 범위:</strong>
 *
 * <ul>
 *   <li>BrandQueryPort 인터페이스 구현 검증
 *   <li>JpaEntity → Domain 변환 검증
 *   <li>검색 조건(Criteria) 적용 검증
 * </ul>
 *
 * @author Development Team
 * @since 1.0.0
 */
@DisplayName("BrandQueryAdapter 통합 테스트")
class BrandQueryAdapterTest extends RepositoryTestSupport {

    @Autowired private BrandQueryPort brandQueryPort;

    private static final Instant NOW = Instant.now();

    private Long nikeBrandId;
    private Long adidasBrandId;
    private Long pumaBrandId;
    private Long inactiveBrandId;

    @BeforeEach
    void setUp() {
        // 테스트 데이터 설정
        BrandJpaEntity nike =
                BrandJpaEntity.of(
                        null,
                        "NIKE",
                        "나이키",
                        "Nike",
                        "https://cdn.example.com/nike.png",
                        "ACTIVE",
                        NOW,
                        NOW);
        BrandJpaEntity adidas =
                BrandJpaEntity.of(
                        null,
                        "ADIDAS",
                        "아디다스",
                        "Adidas",
                        "https://cdn.example.com/adidas.png",
                        "ACTIVE",
                        NOW,
                        NOW);
        BrandJpaEntity puma =
                BrandJpaEntity.of(
                        null,
                        "PUMA",
                        "푸마",
                        "Puma",
                        "https://cdn.example.com/puma.png",
                        "ACTIVE",
                        NOW,
                        NOW);
        BrandJpaEntity inactive =
                BrandJpaEntity.of(
                        null,
                        "INACTIVE_BRAND",
                        "비활성브랜드",
                        "InactiveBrand",
                        null,
                        "INACTIVE",
                        NOW,
                        NOW);

        persistAll(nike, adidas, puma, inactive);
        flushAndClear();

        nikeBrandId = nike.getId();
        adidasBrandId = adidas.getId();
        pumaBrandId = puma.getId();
        inactiveBrandId = inactive.getId();
    }

    @Nested
    @DisplayName("findById 메서드")
    class FindById {

        @Test
        @DisplayName("성공 - ID로 Brand 도메인 조회")
        void findById_returnsDomain() {
            // Given
            BrandId brandId = BrandId.of(nikeBrandId);

            // When
            Optional<Brand> result = brandQueryPort.findById(brandId);

            // Then
            assertThat(result).isPresent();
            Brand brand = result.get();
            assertThat(brand.getIdValue()).isEqualTo(nikeBrandId);
            assertThat(brand.getCodeValue()).isEqualTo("NIKE");
            assertThat(brand.getNameKoValue()).isEqualTo("나이키");
            assertThat(brand.getNameEnValue()).isEqualTo("Nike");
            assertThat(brand.getLogoUrlValue()).isEqualTo("https://cdn.example.com/nike.png");
            assertThat(brand.getStatusValue()).isEqualTo("ACTIVE");
        }

        @Test
        @DisplayName("성공 - 존재하지 않는 ID로 조회 시 빈 Optional 반환")
        void findById_notFound() {
            // Given
            BrandId nonExistentId = BrandId.of(999999L);

            // When
            Optional<Brand> result = brandQueryPort.findById(nonExistentId);

            // Then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("findByCode 메서드")
    class FindByCode {

        @Test
        @DisplayName("성공 - 코드로 Brand 도메인 조회")
        void findByCode_returnsDomain() {
            // Given
            BrandCode code = BrandCode.of("ADIDAS");

            // When
            Optional<Brand> result = brandQueryPort.findByCode(code);

            // Then
            assertThat(result).isPresent();
            Brand brand = result.get();
            assertThat(brand.getCodeValue()).isEqualTo("ADIDAS");
            assertThat(brand.getNameKoValue()).isEqualTo("아디다스");
        }

        @Test
        @DisplayName("성공 - 존재하지 않는 코드로 조회 시 빈 Optional 반환")
        void findByCode_notFound() {
            // Given
            BrandCode nonExistentCode = BrandCode.of("NON_EXISTENT");

            // When
            Optional<Brand> result = brandQueryPort.findByCode(nonExistentCode);

            // Then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("findByCriteria 메서드")
    class FindByCriteria {

        @Test
        @DisplayName("성공 - 기본 조건으로 Domain 목록 조회")
        void findByCriteria_returnsDomainList() {
            // Given
            BrandSearchCriteria criteria = BrandSearchCriteria.of(null, null, 0, 20);

            // When
            List<Brand> result = brandQueryPort.findByCriteria(criteria);

            // Then
            assertThat(result).hasSize(3);
            assertThat(result)
                    .allSatisfy(
                            brand -> {
                                assertThat(brand.getStatusValue()).isEqualTo("ACTIVE");
                                assertThat(brand.getIdValue()).isNotNull();
                                assertThat(brand.getCodeValue()).isNotNull();
                            });
        }

        @Test
        @DisplayName("성공 - 키워드 검색으로 Domain 목록 조회")
        void findByCriteria_withKeyword() {
            // Given
            BrandSearchCriteria criteria = BrandSearchCriteria.ofKeyword("푸마", 0, 20);

            // When
            List<Brand> result = brandQueryPort.findByCriteria(criteria);

            // Then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getNameKoValue()).isEqualTo("푸마");
        }

        @Test
        @DisplayName("성공 - INACTIVE 상태 필터로 Domain 목록 조회")
        void findByCriteria_inactiveStatus() {
            // Given
            BrandSearchCriteria criteria = BrandSearchCriteria.of(null, "INACTIVE", 0, 20);

            // When
            List<Brand> result = brandQueryPort.findByCriteria(criteria);

            // Then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getStatusValue()).isEqualTo("INACTIVE");
        }

        @Test
        @DisplayName("성공 - 페이징 적용")
        void findByCriteria_withPaging() {
            // Given
            BrandSearchCriteria criteria = BrandSearchCriteria.of(null, null, 0, 2);

            // When
            List<Brand> result = brandQueryPort.findByCriteria(criteria);

            // Then
            assertThat(result).hasSize(2);
        }
    }

    @Nested
    @DisplayName("findAllActive 메서드")
    class FindAllActive {

        @Test
        @DisplayName("성공 - 모든 활성 Brand 도메인 목록 조회")
        void findAllActive_returnsDomainList() {
            // When
            List<Brand> result = brandQueryPort.findAllActive();

            // Then
            assertThat(result).hasSize(3);
            assertThat(result)
                    .allSatisfy(
                            brand -> {
                                assertThat(brand.getStatusValue()).isEqualTo("ACTIVE");
                            });
        }

        @Test
        @DisplayName("성공 - nameKo 기준 정렬")
        void findAllActive_sortedByNameKo() {
            // When
            List<Brand> result = brandQueryPort.findAllActive();

            // Then
            assertThat(result.get(0).getNameKoValue()).isEqualTo("나이키");
            assertThat(result.get(1).getNameKoValue()).isEqualTo("아디다스");
            assertThat(result.get(2).getNameKoValue()).isEqualTo("푸마");
        }
    }

    @Nested
    @DisplayName("countByCriteria 메서드")
    class CountByCriteria {

        @Test
        @DisplayName("성공 - 기본 조건 개수 조회")
        void countByCriteria_default() {
            // Given
            BrandSearchCriteria criteria = BrandSearchCriteria.of(null, null, 0, 20);

            // When
            long count = brandQueryPort.countByCriteria(criteria);

            // Then
            assertThat(count).isEqualTo(3);
        }

        @Test
        @DisplayName("성공 - 키워드 검색 개수 조회")
        void countByCriteria_withKeyword() {
            // Given
            BrandSearchCriteria criteria = BrandSearchCriteria.ofKeyword("나이키", 0, 20);

            // When
            long count = brandQueryPort.countByCriteria(criteria);

            // Then
            assertThat(count).isEqualTo(1);
        }
    }

    @Nested
    @DisplayName("existsById 메서드")
    class ExistsById {

        @Test
        @DisplayName("성공 - 존재하는 브랜드")
        void existsById_exists() {
            // Given
            BrandId brandId = BrandId.of(nikeBrandId);

            // When
            boolean exists = brandQueryPort.existsById(brandId);

            // Then
            assertThat(exists).isTrue();
        }

        @Test
        @DisplayName("성공 - 존재하지 않는 브랜드")
        void existsById_notExists() {
            // Given
            BrandId nonExistentId = BrandId.of(999999L);

            // When
            boolean exists = brandQueryPort.existsById(nonExistentId);

            // Then
            assertThat(exists).isFalse();
        }
    }

    @Nested
    @DisplayName("existsActiveById 메서드")
    class ExistsActiveById {

        @Test
        @DisplayName("성공 - 활성 브랜드 존재")
        void existsActiveById_activeExists() {
            // Given
            Long brandId = nikeBrandId;

            // When
            boolean exists = brandQueryPort.existsActiveById(brandId);

            // Then
            assertThat(exists).isTrue();
        }

        @Test
        @DisplayName("성공 - 비활성 브랜드는 false 반환")
        void existsActiveById_inactiveReturnsFalse() {
            // Given
            Long brandId = inactiveBrandId;

            // When
            boolean exists = brandQueryPort.existsActiveById(brandId);

            // Then
            assertThat(exists).isFalse();
        }
    }
}
