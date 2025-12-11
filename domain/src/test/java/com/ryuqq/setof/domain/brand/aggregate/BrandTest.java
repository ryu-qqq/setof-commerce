package com.ryuqq.setof.domain.brand.aggregate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ryuqq.setof.domain.brand.vo.BrandCode;
import com.ryuqq.setof.domain.brand.vo.BrandId;
import com.ryuqq.setof.domain.brand.vo.BrandLogoUrl;
import com.ryuqq.setof.domain.brand.vo.BrandNameEn;
import com.ryuqq.setof.domain.brand.vo.BrandNameKo;
import com.ryuqq.setof.domain.brand.vo.BrandStatus;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Brand Aggregate 테스트
 *
 * <p>Brand는 조회 전용 엔티티로, reconstitute() 팩토리 메서드와 조회 관련 메서드만 테스트합니다.
 */
@DisplayName("Brand Aggregate")
class BrandTest {

    private static final Instant NOW = Instant.parse("2025-01-01T00:00:00Z");

    @Nested
    @DisplayName("reconstitute() - Persistence 복원")
    class Reconstitute {

        @Test
        @DisplayName("모든 필드를 복원할 수 있다")
        void shouldReconstituteBrandFromPersistence() {
            // given
            BrandId id = BrandId.of(1L);
            BrandCode code = BrandCode.of("NIKE001");
            BrandNameKo nameKo = BrandNameKo.of("나이키");
            BrandNameEn nameEn = BrandNameEn.of("Nike");
            BrandLogoUrl logoUrl = BrandLogoUrl.of("https://cdn.example.com/nike.png");
            BrandStatus status = BrandStatus.ACTIVE;
            Instant createdAt = NOW;
            Instant updatedAt = NOW;

            // when
            Brand brand =
                    Brand.reconstitute(
                            id, code, nameKo, nameEn, logoUrl, status, createdAt, updatedAt);

            // then
            assertNotNull(brand);
            assertEquals(1L, brand.getIdValue());
            assertEquals("NIKE001", brand.getCodeValue());
            assertEquals("나이키", brand.getNameKoValue());
            assertEquals("Nike", brand.getNameEnValue());
            assertEquals("https://cdn.example.com/nike.png", brand.getLogoUrlValue());
            assertEquals("ACTIVE", brand.getStatusValue());
            assertEquals(createdAt, brand.getCreatedAt());
            assertEquals(updatedAt, brand.getUpdatedAt());
        }

        @Test
        @DisplayName("영문명 없이 복원할 수 있다")
        void shouldReconstituteBrandWithoutNameEn() {
            // given
            BrandId id = BrandId.of(2L);
            BrandCode code = BrandCode.of("LOCAL001");
            BrandNameKo nameKo = BrandNameKo.of("로컬브랜드");
            BrandNameEn nameEn = BrandNameEn.empty();
            BrandLogoUrl logoUrl = BrandLogoUrl.empty();
            BrandStatus status = BrandStatus.ACTIVE;

            // when
            Brand brand = Brand.reconstitute(id, code, nameKo, nameEn, logoUrl, status, NOW, NOW);

            // then
            assertNotNull(brand);
            assertNull(brand.getNameEnValue());
            assertNull(brand.getLogoUrlValue());
            assertFalse(brand.hasNameEn());
            assertFalse(brand.hasLogoUrl());
        }

        @Test
        @DisplayName("비활성 브랜드를 복원할 수 있다")
        void shouldReconstituteInactiveBrand() {
            // given
            BrandStatus status = BrandStatus.INACTIVE;

            // when
            Brand brand = createBrandWithStatus(status);

            // then
            assertNotNull(brand);
            assertFalse(brand.isActive());
        }
    }

    @Nested
    @DisplayName("Law of Demeter Helper Methods")
    class HelperMethods {

        @Test
        @DisplayName("getIdValue()는 ID 값을 직접 반환한다")
        void shouldReturnIdValueDirectly() {
            // given
            Brand brand = createBrand();

            // then
            assertEquals(1L, brand.getIdValue());
        }

        @Test
        @DisplayName("getCodeValue()는 브랜드 코드를 직접 반환한다")
        void shouldReturnCodeValueDirectly() {
            // given
            Brand brand = createBrand();

            // then
            assertEquals("NIKE001", brand.getCodeValue());
        }

        @Test
        @DisplayName("getNameKoValue()는 한글 브랜드명을 직접 반환한다")
        void shouldReturnNameKoValueDirectly() {
            // given
            Brand brand = createBrand();

            // then
            assertEquals("나이키", brand.getNameKoValue());
        }

        @Test
        @DisplayName("getNameEnValue()는 영문 브랜드명을 직접 반환한다")
        void shouldReturnNameEnValueDirectly() {
            // given
            Brand brand = createBrand();

            // then
            assertEquals("Nike", brand.getNameEnValue());
        }

        @Test
        @DisplayName("getLogoUrlValue()는 로고 URL을 직접 반환한다")
        void shouldReturnLogoUrlValueDirectly() {
            // given
            Brand brand = createBrand();

            // then
            assertEquals("https://cdn.example.com/nike.png", brand.getLogoUrlValue());
        }

        @Test
        @DisplayName("getStatusValue()는 상태 이름을 직접 반환한다")
        void shouldReturnStatusValueDirectly() {
            // given
            Brand brand = createBrand();

            // then
            assertEquals("ACTIVE", brand.getStatusValue());
        }
    }

    @Nested
    @DisplayName("비즈니스 메서드")
    class BusinessMethods {

        @Test
        @DisplayName("isActive()는 활성 브랜드이면 true를 반환한다")
        void shouldReturnTrueForActiveBrand() {
            // given
            Brand brand = createBrandWithStatus(BrandStatus.ACTIVE);

            // then
            assertTrue(brand.isActive());
        }

        @Test
        @DisplayName("isActive()는 비활성 브랜드이면 false를 반환한다")
        void shouldReturnFalseForInactiveBrand() {
            // given
            Brand brand = createBrandWithStatus(BrandStatus.INACTIVE);

            // then
            assertFalse(brand.isActive());
        }

        @Test
        @DisplayName("hasNameEn()은 영문명이 있으면 true를 반환한다")
        void shouldReturnTrueWhenNameEnExists() {
            // given
            Brand brand = createBrand();

            // then
            assertTrue(brand.hasNameEn());
        }

        @Test
        @DisplayName("hasNameEn()은 영문명이 없으면 false를 반환한다")
        void shouldReturnFalseWhenNameEnNotExists() {
            // given
            Brand brand = createBrandWithoutOptionalFields();

            // then
            assertFalse(brand.hasNameEn());
        }

        @Test
        @DisplayName("hasLogoUrl()은 로고 URL이 있으면 true를 반환한다")
        void shouldReturnTrueWhenLogoUrlExists() {
            // given
            Brand brand = createBrand();

            // then
            assertTrue(brand.hasLogoUrl());
        }

        @Test
        @DisplayName("hasLogoUrl()은 로고 URL이 없으면 false를 반환한다")
        void shouldReturnFalseWhenLogoUrlNotExists() {
            // given
            Brand brand = createBrandWithoutOptionalFields();

            // then
            assertFalse(brand.hasLogoUrl());
        }
    }

    @Nested
    @DisplayName("Getter 메서드")
    class Getters {

        @Test
        @DisplayName("모든 Getter 메서드가 올바른 값을 반환한다")
        void shouldReturnCorrectValuesFromGetters() {
            // given
            BrandId id = BrandId.of(88L);
            BrandCode code = BrandCode.of("ADIDAS001");
            BrandNameKo nameKo = BrandNameKo.of("아디다스");
            BrandNameEn nameEn = BrandNameEn.of("Adidas");
            BrandLogoUrl logoUrl = BrandLogoUrl.of("https://cdn.example.com/adidas.png");
            BrandStatus status = BrandStatus.ACTIVE;
            Instant createdAt = Instant.parse("2024-01-01T00:00:00Z");
            Instant updatedAt = Instant.parse("2024-06-01T00:00:00Z");

            Brand brand =
                    Brand.reconstitute(
                            id, code, nameKo, nameEn, logoUrl, status, createdAt, updatedAt);

            // then
            assertEquals(id, brand.getId());
            assertEquals(code, brand.getCode());
            assertEquals(nameKo, brand.getNameKo());
            assertEquals(nameEn, brand.getNameEn());
            assertEquals(logoUrl, brand.getLogoUrl());
            assertEquals(status, brand.getStatus());
            assertEquals(createdAt, brand.getCreatedAt());
            assertEquals(updatedAt, brand.getUpdatedAt());
        }
    }

    // ========== Helper Methods ==========

    private Brand createBrand() {
        return Brand.reconstitute(
                BrandId.of(1L),
                BrandCode.of("NIKE001"),
                BrandNameKo.of("나이키"),
                BrandNameEn.of("Nike"),
                BrandLogoUrl.of("https://cdn.example.com/nike.png"),
                BrandStatus.ACTIVE,
                NOW,
                NOW);
    }

    private Brand createBrandWithStatus(BrandStatus status) {
        return Brand.reconstitute(
                BrandId.of(1L),
                BrandCode.of("NIKE001"),
                BrandNameKo.of("나이키"),
                BrandNameEn.of("Nike"),
                BrandLogoUrl.of("https://cdn.example.com/nike.png"),
                status,
                NOW,
                NOW);
    }

    private Brand createBrandWithoutOptionalFields() {
        return Brand.reconstitute(
                BrandId.of(1L),
                BrandCode.of("LOCAL001"),
                BrandNameKo.of("로컬브랜드"),
                BrandNameEn.empty(),
                BrandLogoUrl.empty(),
                BrandStatus.ACTIVE,
                NOW,
                NOW);
    }
}
