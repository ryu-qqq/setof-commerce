package com.ryuqq.setof.application.brand.manager.query;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ryuqq.setof.application.brand.port.out.query.BrandQueryPort;
import com.ryuqq.setof.domain.brand.BrandFixture;
import com.ryuqq.setof.domain.brand.aggregate.Brand;
import com.ryuqq.setof.domain.brand.exception.BrandNotFoundException;
import com.ryuqq.setof.domain.brand.query.criteria.BrandSearchCriteria;
import com.ryuqq.setof.domain.brand.vo.BrandCode;
import com.ryuqq.setof.domain.brand.vo.BrandId;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("BrandReadManager")
@ExtendWith(MockitoExtension.class)
class BrandReadManagerTest {

    @Mock private BrandQueryPort brandQueryPort;

    private BrandReadManager brandReadManager;

    @BeforeEach
    void setUp() {
        brandReadManager = new BrandReadManager(brandQueryPort);
    }

    @Nested
    @DisplayName("findById")
    class FindByIdTest {

        @Test
        @DisplayName("존재하는 브랜드 ID로 조회 성공")
        void shouldFindBrandById() {
            // Given
            Long brandId = 1L;
            Brand brand = BrandFixture.createWithId(brandId);
            when(brandQueryPort.findById(any(BrandId.class))).thenReturn(Optional.of(brand));

            // When
            Brand result = brandReadManager.findById(brandId);

            // Then
            assertNotNull(result);
            assertEquals(brandId, result.getIdValue());
            verify(brandQueryPort, times(1)).findById(any(BrandId.class));
        }

        @Test
        @DisplayName("존재하지 않는 브랜드 ID로 조회 시 예외 발생")
        void shouldThrowExceptionWhenBrandNotFound() {
            // Given
            Long brandId = 999L;
            when(brandQueryPort.findById(any(BrandId.class))).thenReturn(Optional.empty());

            // When & Then
            assertThrows(BrandNotFoundException.class, () -> brandReadManager.findById(brandId));
            verify(brandQueryPort, times(1)).findById(any(BrandId.class));
        }
    }

    @Nested
    @DisplayName("findByIdOptional")
    class FindByIdOptionalTest {

        @Test
        @DisplayName("존재하는 브랜드 ID로 조회 성공")
        void shouldFindBrandByIdOptional() {
            // Given
            Long brandId = 1L;
            Brand brand = BrandFixture.createWithId(brandId);
            when(brandQueryPort.findById(any(BrandId.class))).thenReturn(Optional.of(brand));

            // When
            Optional<Brand> result = brandReadManager.findByIdOptional(brandId);

            // Then
            assertTrue(result.isPresent());
            assertEquals(brandId, result.get().getIdValue());
        }

        @Test
        @DisplayName("존재하지 않는 브랜드 ID로 조회 시 빈 Optional 반환")
        void shouldReturnEmptyOptionalWhenBrandNotFound() {
            // Given
            Long brandId = 999L;
            when(brandQueryPort.findById(any(BrandId.class))).thenReturn(Optional.empty());

            // When
            Optional<Brand> result = brandReadManager.findByIdOptional(brandId);

            // Then
            assertTrue(result.isEmpty());
        }
    }

    @Nested
    @DisplayName("findByCode")
    class FindByCodeTest {

        @Test
        @DisplayName("존재하는 브랜드 코드로 조회 성공")
        void shouldFindBrandByCode() {
            // Given
            String brandCode = "NIKE";
            Brand brand = BrandFixture.create();
            when(brandQueryPort.findByCode(any(BrandCode.class))).thenReturn(Optional.of(brand));

            // When
            Brand result = brandReadManager.findByCode(brandCode);

            // Then
            assertNotNull(result);
            assertEquals(brandCode, result.getCodeValue());
        }

        @Test
        @DisplayName("존재하지 않는 브랜드 코드로 조회 시 예외 발생")
        void shouldThrowExceptionWhenBrandCodeNotFound() {
            // Given
            String brandCode = "UNKNOWN";
            when(brandQueryPort.findByCode(any(BrandCode.class))).thenReturn(Optional.empty());

            // When & Then
            assertThrows(
                    BrandNotFoundException.class, () -> brandReadManager.findByCode(brandCode));
        }
    }

    @Nested
    @DisplayName("findAllActive")
    class FindAllActiveTest {

        @Test
        @DisplayName("활성 브랜드 목록 조회 성공")
        void shouldFindAllActiveBrands() {
            // Given
            List<Brand> brands = BrandFixture.createList();
            when(brandQueryPort.findAllActive()).thenReturn(brands);

            // When
            List<Brand> result = brandReadManager.findAllActive();

            // Then
            assertNotNull(result);
            assertEquals(brands.size(), result.size());
            verify(brandQueryPort, times(1)).findAllActive();
        }
    }

    @Nested
    @DisplayName("findByCriteria")
    class FindByCriteriaTest {

        @Test
        @DisplayName("검색 조건으로 브랜드 목록 조회 성공")
        void shouldFindBrandsByCriteria() {
            // Given
            BrandSearchCriteria criteria = BrandSearchCriteria.of(null, "ACTIVE", 0, 20);
            List<Brand> brands = BrandFixture.createList();
            when(brandQueryPort.findByCriteria(criteria)).thenReturn(brands);

            // When
            List<Brand> result = brandReadManager.findByCriteria(criteria);

            // Then
            assertNotNull(result);
            assertEquals(brands.size(), result.size());
            verify(brandQueryPort, times(1)).findByCriteria(criteria);
        }
    }

    @Nested
    @DisplayName("countByCriteria")
    class CountByCriteriaTest {

        @Test
        @DisplayName("검색 조건에 맞는 브랜드 개수 조회 성공")
        void shouldCountBrandsByCriteria() {
            // Given
            BrandSearchCriteria criteria = BrandSearchCriteria.of(null, "ACTIVE", 0, 20);
            long expectedCount = 10L;
            when(brandQueryPort.countByCriteria(criteria)).thenReturn(expectedCount);

            // When
            long result = brandReadManager.countByCriteria(criteria);

            // Then
            assertEquals(expectedCount, result);
            verify(brandQueryPort, times(1)).countByCriteria(criteria);
        }
    }

    @Nested
    @DisplayName("existsById")
    class ExistsByIdTest {

        @Test
        @DisplayName("존재하는 브랜드 ID는 true 반환")
        void shouldReturnTrueWhenBrandExists() {
            // Given
            Long brandId = 1L;
            when(brandQueryPort.existsById(any(BrandId.class))).thenReturn(true);

            // When
            boolean result = brandReadManager.existsById(brandId);

            // Then
            assertTrue(result);
        }

        @Test
        @DisplayName("존재하지 않는 브랜드 ID는 false 반환")
        void shouldReturnFalseWhenBrandNotExists() {
            // Given
            Long brandId = 999L;
            when(brandQueryPort.existsById(any(BrandId.class))).thenReturn(false);

            // When
            boolean result = brandReadManager.existsById(brandId);

            // Then
            assertFalse(result);
        }
    }

    @Nested
    @DisplayName("existsActiveById")
    class ExistsActiveByIdTest {

        @Test
        @DisplayName("활성 브랜드가 존재하면 true 반환")
        void shouldReturnTrueWhenActiveBrandExists() {
            // Given
            Long brandId = 1L;
            when(brandQueryPort.existsActiveById(brandId)).thenReturn(true);

            // When
            boolean result = brandReadManager.existsActiveById(brandId);

            // Then
            assertTrue(result);
        }

        @Test
        @DisplayName("활성 브랜드가 존재하지 않으면 false 반환")
        void shouldReturnFalseWhenActiveBrandNotExists() {
            // Given
            Long brandId = 999L;
            when(brandQueryPort.existsActiveById(brandId)).thenReturn(false);

            // When
            boolean result = brandReadManager.existsActiveById(brandId);

            // Then
            assertFalse(result);
        }
    }
}
