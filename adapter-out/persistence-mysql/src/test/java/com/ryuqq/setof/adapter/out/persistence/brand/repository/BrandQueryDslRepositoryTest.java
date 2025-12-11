package com.ryuqq.setof.adapter.out.persistence.brand.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.out.persistence.brand.entity.BrandJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.common.JpaSliceTestSupport;
import com.ryuqq.setof.domain.brand.query.criteria.BrandSearchCriteria;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

/**
 * BrandQueryDslRepository Slice 테스트
 *
 * <p>QueryDSL 기반 Brand 조회 쿼리 검증
 *
 * <p><strong>테스트 범위:</strong>
 *
 * <ul>
 *   <li>findById() - ID로 단건 조회
 *   <li>findByCode() - 코드로 단건 조회
 *   <li>findByCondition() - 검색 조건 목록 조회
 *   <li>findAllActive() - 활성 브랜드 목록 조회
 *   <li>countByCondition() - 검색 조건 개수 조회
 *   <li>existsById() - 존재 여부 확인
 *   <li>existsActiveById() - 활성 브랜드 존재 여부 확인
 * </ul>
 *
 * @author Development Team
 * @since 1.0.0
 */
@DisplayName("BrandQueryDslRepository Slice 테스트")
@Import(BrandQueryDslRepository.class)
class BrandQueryDslRepositoryTest extends JpaSliceTestSupport {

    @Autowired private BrandQueryDslRepository brandQueryDslRepository;

    private static final Instant NOW = Instant.now();

    @BeforeEach
    void setUp() {
        // 기본 테스트 데이터 설정
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
    }

    @Nested
    @DisplayName("findById 메서드")
    class FindById {

        @Test
        @DisplayName("성공 - ID로 브랜드 조회")
        void findById_success() {
            // Given
            BrandJpaEntity newBrand =
                    BrandJpaEntity.of(
                            null,
                            "TEST",
                            "테스트",
                            "Test",
                            "https://cdn.example.com/test.png",
                            "ACTIVE",
                            NOW,
                            NOW);
            BrandJpaEntity saved = persistAndFlush(newBrand);
            Long brandId = saved.getId();
            flushAndClear();

            // When
            Optional<BrandJpaEntity> result = brandQueryDslRepository.findById(brandId);

            // Then
            assertThat(result).isPresent();
            assertThat(result.get().getId()).isEqualTo(brandId);
            assertThat(result.get().getCode()).isEqualTo("TEST");
            assertThat(result.get().getNameKo()).isEqualTo("테스트");
        }

        @Test
        @DisplayName("실패 - 존재하지 않는 ID로 조회 시 빈 Optional 반환")
        void findById_notFound() {
            // Given
            Long nonExistentId = 999999L;

            // When
            Optional<BrandJpaEntity> result = brandQueryDslRepository.findById(nonExistentId);

            // Then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("findByCode 메서드")
    class FindByCode {

        @Test
        @DisplayName("성공 - 코드로 브랜드 조회")
        void findByCode_success() {
            // Given
            String code = "NIKE";

            // When
            Optional<BrandJpaEntity> result = brandQueryDslRepository.findByCode(code);

            // Then
            assertThat(result).isPresent();
            assertThat(result.get().getCode()).isEqualTo("NIKE");
            assertThat(result.get().getNameKo()).isEqualTo("나이키");
        }

        @Test
        @DisplayName("실패 - 존재하지 않는 코드로 조회 시 빈 Optional 반환")
        void findByCode_notFound() {
            // Given
            String nonExistentCode = "NON_EXISTENT";

            // When
            Optional<BrandJpaEntity> result = brandQueryDslRepository.findByCode(nonExistentCode);

            // Then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("findByCondition 메서드")
    class FindByCondition {

        @Test
        @DisplayName("성공 - 기본 조회 (ACTIVE 브랜드만)")
        void findByCondition_defaultQuery_returnsActiveOnly() {
            // Given
            BrandSearchCriteria criteria = BrandSearchCriteria.of(null, null, 0, 20);

            // When
            List<BrandJpaEntity> result = brandQueryDslRepository.findByCondition(criteria);

            // Then
            assertThat(result).hasSize(3);
            assertThat(result).allMatch(brand -> "ACTIVE".equals(brand.getStatus()));
        }

        @Test
        @DisplayName("성공 - 키워드 검색 (한글명)")
        void findByCondition_keywordSearchKorean() {
            // Given
            BrandSearchCriteria criteria = BrandSearchCriteria.ofKeyword("나이키", 0, 20);

            // When
            List<BrandJpaEntity> result = brandQueryDslRepository.findByCondition(criteria);

            // Then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getNameKo()).isEqualTo("나이키");
        }

        @Test
        @DisplayName("성공 - 키워드 검색 (영문명, 대소문자 무시)")
        void findByCondition_keywordSearchEnglish() {
            // Given
            BrandSearchCriteria criteria = BrandSearchCriteria.ofKeyword("nike", 0, 20);

            // When
            List<BrandJpaEntity> result = brandQueryDslRepository.findByCondition(criteria);

            // Then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getNameEn()).isEqualTo("Nike");
        }

        @Test
        @DisplayName("성공 - 상태 필터 (INACTIVE)")
        void findByCondition_statusFilter() {
            // Given
            BrandSearchCriteria criteria = BrandSearchCriteria.of(null, "INACTIVE", 0, 20);

            // When
            List<BrandJpaEntity> result = brandQueryDslRepository.findByCondition(criteria);

            // Then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getStatus()).isEqualTo("INACTIVE");
            assertThat(result.get(0).getCode()).isEqualTo("INACTIVE_BRAND");
        }

        @Test
        @DisplayName("성공 - 페이징 적용")
        void findByCondition_withPaging() {
            // Given
            BrandSearchCriteria criteria = BrandSearchCriteria.of(null, null, 0, 2);

            // When
            List<BrandJpaEntity> result = brandQueryDslRepository.findByCondition(criteria);

            // Then
            assertThat(result).hasSize(2);
        }

        @Test
        @DisplayName("성공 - 검색 결과 없음")
        void findByCondition_noResults() {
            // Given
            BrandSearchCriteria criteria = BrandSearchCriteria.ofKeyword("존재하지않는브랜드", 0, 20);

            // When
            List<BrandJpaEntity> result = brandQueryDslRepository.findByCondition(criteria);

            // Then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("성공 - 정렬 (nameKo 오름차순)")
        void findByCondition_sortedByNameKo() {
            // Given
            BrandSearchCriteria criteria = BrandSearchCriteria.of(null, null, 0, 20);

            // When
            List<BrandJpaEntity> result = brandQueryDslRepository.findByCondition(criteria);

            // Then
            assertThat(result).hasSize(3);
            // 나이키 < 아디다스 < 푸마 (가나다순)
            assertThat(result.get(0).getNameKo()).isEqualTo("나이키");
            assertThat(result.get(1).getNameKo()).isEqualTo("아디다스");
            assertThat(result.get(2).getNameKo()).isEqualTo("푸마");
        }
    }

    @Nested
    @DisplayName("findAllActive 메서드")
    class FindAllActive {

        @Test
        @DisplayName("성공 - 모든 활성 브랜드 조회")
        void findAllActive_success() {
            // When
            List<BrandJpaEntity> result = brandQueryDslRepository.findAllActive();

            // Then
            assertThat(result).hasSize(3);
            assertThat(result).allMatch(brand -> "ACTIVE".equals(brand.getStatus()));
        }

        @Test
        @DisplayName("성공 - nameKo 기준 정렬")
        void findAllActive_sortedByNameKo() {
            // When
            List<BrandJpaEntity> result = brandQueryDslRepository.findAllActive();

            // Then
            assertThat(result).hasSize(3);
            assertThat(result.get(0).getNameKo()).isEqualTo("나이키");
            assertThat(result.get(1).getNameKo()).isEqualTo("아디다스");
            assertThat(result.get(2).getNameKo()).isEqualTo("푸마");
        }
    }

    @Nested
    @DisplayName("countByCondition 메서드")
    class CountByCondition {

        @Test
        @DisplayName("성공 - 기본 조건 개수 (ACTIVE만)")
        void countByCondition_default() {
            // Given
            BrandSearchCriteria criteria = BrandSearchCriteria.of(null, null, 0, 20);

            // When
            long count = brandQueryDslRepository.countByCondition(criteria);

            // Then
            assertThat(count).isEqualTo(3);
        }

        @Test
        @DisplayName("성공 - 키워드 검색 개수")
        void countByCondition_withKeyword() {
            // Given
            BrandSearchCriteria criteria = BrandSearchCriteria.ofKeyword("다", 0, 20);

            // When
            long count = brandQueryDslRepository.countByCondition(criteria);

            // Then
            // "아디다스"만 포함
            assertThat(count).isEqualTo(1);
        }

        @Test
        @DisplayName("성공 - INACTIVE 상태 개수")
        void countByCondition_inactiveStatus() {
            // Given
            BrandSearchCriteria criteria = BrandSearchCriteria.of(null, "INACTIVE", 0, 20);

            // When
            long count = brandQueryDslRepository.countByCondition(criteria);

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
            BrandJpaEntity brand =
                    BrandJpaEntity.of(null, "EXISTS", "존재", "Exists", null, "ACTIVE", NOW, NOW);
            BrandJpaEntity saved = persistAndFlush(brand);
            Long brandId = saved.getId();
            flushAndClear();

            // When
            boolean exists = brandQueryDslRepository.existsById(brandId);

            // Then
            assertThat(exists).isTrue();
        }

        @Test
        @DisplayName("성공 - 존재하지 않는 브랜드")
        void existsById_notExists() {
            // Given
            Long nonExistentId = 999999L;

            // When
            boolean exists = brandQueryDslRepository.existsById(nonExistentId);

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
            BrandJpaEntity brand =
                    BrandJpaEntity.of(
                            null, "ACTIVE_TEST", "활성테스트", "ActiveTest", null, "ACTIVE", NOW, NOW);
            BrandJpaEntity saved = persistAndFlush(brand);
            Long brandId = saved.getId();
            flushAndClear();

            // When
            boolean exists = brandQueryDslRepository.existsActiveById(brandId);

            // Then
            assertThat(exists).isTrue();
        }

        @Test
        @DisplayName("성공 - 비활성 브랜드는 false 반환")
        void existsActiveById_inactiveReturnsFalse() {
            // Given
            BrandJpaEntity brand =
                    BrandJpaEntity.of(
                            null,
                            "INACTIVE_TEST",
                            "비활성테스트",
                            "InactiveTest",
                            null,
                            "INACTIVE",
                            NOW,
                            NOW);
            BrandJpaEntity saved = persistAndFlush(brand);
            Long brandId = saved.getId();
            flushAndClear();

            // When
            boolean exists = brandQueryDslRepository.existsActiveById(brandId);

            // Then
            assertThat(exists).isFalse();
        }

        @Test
        @DisplayName("성공 - 존재하지 않는 ID")
        void existsActiveById_notExists() {
            // Given
            Long nonExistentId = 999999L;

            // When
            boolean exists = brandQueryDslRepository.existsActiveById(nonExistentId);

            // Then
            assertThat(exists).isFalse();
        }
    }
}
