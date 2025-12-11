package com.ryuqq.setof.application.brand.assembler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ryuqq.setof.application.brand.dto.response.BrandResponse;
import com.ryuqq.setof.application.brand.dto.response.BrandSummaryResponse;
import com.ryuqq.setof.domain.brand.BrandFixture;
import com.ryuqq.setof.domain.brand.aggregate.Brand;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("BrandAssembler")
class BrandAssemblerTest {

    private BrandAssembler brandAssembler;

    @BeforeEach
    void setUp() {
        brandAssembler = new BrandAssembler();
    }

    @Nested
    @DisplayName("toBrandResponse")
    class ToBrandResponseTest {

        @Test
        @DisplayName("Brand 도메인을 BrandResponse로 변환 성공")
        void shouldConvertBrandToBrandResponse() {
            // Given
            Brand brand = BrandFixture.create();

            // When
            BrandResponse result = brandAssembler.toBrandResponse(brand);

            // Then
            assertNotNull(result);
            assertEquals(brand.getIdValue(), result.id());
            assertEquals(brand.getCodeValue(), result.code());
            assertEquals(brand.getNameKoValue(), result.nameKo());
            assertEquals(brand.getNameEnValue(), result.nameEn());
            assertEquals(brand.getLogoUrlValue(), result.logoUrl());
            assertEquals(brand.getStatusValue(), result.status());
        }

        @Test
        @DisplayName("영문명 없는 브랜드 변환 성공")
        void shouldConvertBrandWithoutEnglishName() {
            // Given
            Brand brand = BrandFixture.createWithoutEnglishName();

            // When
            BrandResponse result = brandAssembler.toBrandResponse(brand);

            // Then
            assertNotNull(result);
            assertEquals(brand.getIdValue(), result.id());
            assertTrue(result.nameEn() == null || result.nameEn().isEmpty());
        }

        @Test
        @DisplayName("로고 없는 브랜드 변환 성공")
        void shouldConvertBrandWithoutLogo() {
            // Given
            Brand brand = BrandFixture.createWithoutLogo();

            // When
            BrandResponse result = brandAssembler.toBrandResponse(brand);

            // Then
            assertNotNull(result);
            assertEquals(brand.getIdValue(), result.id());
            assertTrue(result.logoUrl() == null || result.logoUrl().isEmpty());
        }
    }

    @Nested
    @DisplayName("toBrandSummaryResponse")
    class ToBrandSummaryResponseTest {

        @Test
        @DisplayName("Brand 도메인을 BrandSummaryResponse로 변환 성공")
        void shouldConvertBrandToBrandSummaryResponse() {
            // Given
            Brand brand = BrandFixture.create();

            // When
            BrandSummaryResponse result = brandAssembler.toBrandSummaryResponse(brand);

            // Then
            assertNotNull(result);
            assertEquals(brand.getIdValue(), result.id());
            assertEquals(brand.getCodeValue(), result.code());
            assertEquals(brand.getNameKoValue(), result.nameKo());
            assertEquals(brand.getLogoUrlValue(), result.logoUrl());
        }
    }

    @Nested
    @DisplayName("toBrandSummaryResponses")
    class ToBrandSummaryResponsesTest {

        @Test
        @DisplayName("Brand 목록을 BrandSummaryResponse 목록으로 변환 성공")
        void shouldConvertBrandListToBrandSummaryResponseList() {
            // Given
            List<Brand> brands = BrandFixture.createList();

            // When
            List<BrandSummaryResponse> results = brandAssembler.toBrandSummaryResponses(brands);

            // Then
            assertNotNull(results);
            assertEquals(brands.size(), results.size());

            for (int i = 0; i < brands.size(); i++) {
                assertEquals(brands.get(i).getIdValue(), results.get(i).id());
                assertEquals(brands.get(i).getCodeValue(), results.get(i).code());
                assertEquals(brands.get(i).getNameKoValue(), results.get(i).nameKo());
            }
        }

        @Test
        @DisplayName("빈 목록 변환 시 빈 목록 반환")
        void shouldReturnEmptyListWhenInputIsEmpty() {
            // Given
            List<Brand> emptyList = List.of();

            // When
            List<BrandSummaryResponse> results = brandAssembler.toBrandSummaryResponses(emptyList);

            // Then
            assertNotNull(results);
            assertTrue(results.isEmpty());
        }
    }
}
