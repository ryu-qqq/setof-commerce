package com.ryuqq.setof.application.brand.service.query;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ryuqq.setof.application.brand.assembler.BrandAssembler;
import com.ryuqq.setof.application.brand.dto.query.BrandSearchQuery;
import com.ryuqq.setof.application.brand.dto.response.BrandResponse;
import com.ryuqq.setof.application.brand.dto.response.BrandSummaryResponse;
import com.ryuqq.setof.application.brand.factory.query.BrandQueryFactory;
import com.ryuqq.setof.application.brand.manager.query.BrandReadManager;
import com.ryuqq.setof.application.common.response.PageResponse;
import com.ryuqq.setof.domain.brand.BrandFixture;
import com.ryuqq.setof.domain.brand.aggregate.Brand;
import com.ryuqq.setof.domain.brand.query.criteria.BrandSearchCriteria;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("BrandQueryService")
@ExtendWith(MockitoExtension.class)
class BrandQueryServiceTest {

    @Mock private BrandReadManager brandReadManager;
    @Mock private BrandQueryFactory brandQueryFactory;

    private BrandAssembler brandAssembler;
    private BrandQueryService brandQueryService;

    @BeforeEach
    void setUp() {
        brandAssembler = new BrandAssembler();
        brandQueryService =
                new BrandQueryService(brandReadManager, brandQueryFactory, brandAssembler);
    }

    @Nested
    @DisplayName("execute(Long brandId)")
    class ExecuteByIdTest {

        @Test
        @DisplayName("브랜드 ID로 단일 브랜드 조회 성공")
        void shouldReturnBrandById() {
            // Given
            Long brandId = 1L;
            Brand brand = BrandFixture.createWithId(brandId);
            when(brandReadManager.findById(brandId)).thenReturn(brand);

            // When
            BrandResponse result = brandQueryService.execute(brandId);

            // Then
            assertNotNull(result);
            assertEquals(brandId, result.id());
            assertEquals(brand.getCodeValue(), result.code());
            assertEquals(brand.getNameKoValue(), result.nameKo());
            verify(brandReadManager, times(1)).findById(brandId);
        }

        @Test
        @DisplayName("변환된 응답에 올바른 브랜드 정보 포함")
        void shouldContainCorrectBrandInfoInResponse() {
            // Given
            Brand brand = BrandFixture.create();
            when(brandReadManager.findById(brand.getIdValue())).thenReturn(brand);

            // When
            BrandResponse result = brandQueryService.execute(brand.getIdValue());

            // Then
            assertEquals(brand.getIdValue(), result.id());
            assertEquals(brand.getCodeValue(), result.code());
            assertEquals(brand.getNameKoValue(), result.nameKo());
            assertEquals(brand.getNameEnValue(), result.nameEn());
            assertEquals(brand.getLogoUrlValue(), result.logoUrl());
            assertEquals(brand.getStatusValue(), result.status());
        }
    }

    @Nested
    @DisplayName("execute(BrandSearchQuery query)")
    class ExecuteByQueryTest {

        @Test
        @DisplayName("검색 조건으로 브랜드 목록 조회 성공")
        void shouldReturnBrandsByQuery() {
            // Given
            BrandSearchQuery query = BrandSearchQuery.ofActive(0, 20);
            BrandSearchCriteria criteria = BrandSearchCriteria.of(null, "ACTIVE", 0, 20);
            List<Brand> brands = BrandFixture.createList();
            long totalCount = 3L;

            when(brandQueryFactory.createCriteria(query)).thenReturn(criteria);
            when(brandReadManager.findByCriteria(criteria)).thenReturn(brands);
            when(brandReadManager.countByCriteria(criteria)).thenReturn(totalCount);

            // When
            PageResponse<BrandSummaryResponse> result = brandQueryService.execute(query);

            // Then
            assertNotNull(result);
            assertEquals(brands.size(), result.content().size());
            assertEquals(totalCount, result.totalElements());
            assertEquals(0, result.page());
            assertEquals(20, result.size());
            verify(brandQueryFactory, times(1)).createCriteria(query);
            verify(brandReadManager, times(1)).findByCriteria(criteria);
            verify(brandReadManager, times(1)).countByCriteria(criteria);
        }

        @Test
        @DisplayName("첫 페이지 조회 시 isFirst가 true")
        void shouldReturnIsFirstTrueOnFirstPage() {
            // Given
            BrandSearchQuery query = BrandSearchQuery.ofActive(0, 20);
            BrandSearchCriteria criteria = BrandSearchCriteria.of(null, "ACTIVE", 0, 20);
            List<Brand> brands = BrandFixture.createList();
            long totalCount = 100L;

            when(brandQueryFactory.createCriteria(query)).thenReturn(criteria);
            when(brandReadManager.findByCriteria(criteria)).thenReturn(brands);
            when(brandReadManager.countByCriteria(criteria)).thenReturn(totalCount);

            // When
            PageResponse<BrandSummaryResponse> result = brandQueryService.execute(query);

            // Then
            assertTrue(result.first());
            assertFalse(result.last());
        }

        @Test
        @DisplayName("마지막 페이지 조회 시 isLast가 true")
        void shouldReturnIsLastTrueOnLastPage() {
            // Given
            BrandSearchQuery query = BrandSearchQuery.ofActive(4, 20);
            BrandSearchCriteria criteria = BrandSearchCriteria.of(null, "ACTIVE", 4, 20);
            List<Brand> brands = BrandFixture.createList();
            long totalCount = 100L;

            when(brandQueryFactory.createCriteria(query)).thenReturn(criteria);
            when(brandReadManager.findByCriteria(criteria)).thenReturn(brands);
            when(brandReadManager.countByCriteria(criteria)).thenReturn(totalCount);

            // When
            PageResponse<BrandSummaryResponse> result = brandQueryService.execute(query);

            // Then
            assertFalse(result.first());
            assertTrue(result.last());
        }

        @Test
        @DisplayName("브랜드가 없을 때 빈 페이지 응답 반환")
        void shouldReturnEmptyPageWhenNoBrands() {
            // Given
            BrandSearchQuery query = BrandSearchQuery.ofActive(0, 20);
            BrandSearchCriteria criteria = BrandSearchCriteria.of(null, "ACTIVE", 0, 20);

            when(brandQueryFactory.createCriteria(query)).thenReturn(criteria);
            when(brandReadManager.findByCriteria(criteria)).thenReturn(List.of());
            when(brandReadManager.countByCriteria(criteria)).thenReturn(0L);

            // When
            PageResponse<BrandSummaryResponse> result = brandQueryService.execute(query);

            // Then
            assertNotNull(result);
            assertTrue(result.content().isEmpty());
            assertEquals(0L, result.totalElements());
            assertEquals(0, result.totalPages());
        }
    }
}
