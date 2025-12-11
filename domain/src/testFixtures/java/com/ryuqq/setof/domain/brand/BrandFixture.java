package com.ryuqq.setof.domain.brand;

import com.ryuqq.setof.domain.brand.aggregate.Brand;
import com.ryuqq.setof.domain.brand.vo.BrandCode;
import com.ryuqq.setof.domain.brand.vo.BrandId;
import com.ryuqq.setof.domain.brand.vo.BrandLogoUrl;
import com.ryuqq.setof.domain.brand.vo.BrandNameEn;
import com.ryuqq.setof.domain.brand.vo.BrandNameKo;
import com.ryuqq.setof.domain.brand.vo.BrandStatus;
import java.time.Instant;
import java.util.List;

/**
 * Brand TestFixture - Object Mother Pattern
 *
 * <p>테스트에서 Brand 인스턴스 생성을 위한 팩토리 클래스
 */
public final class BrandFixture {

    private static final Instant DEFAULT_CREATED_AT = Instant.parse("2024-01-01T00:00:00Z");
    private static final Instant DEFAULT_UPDATED_AT = Instant.parse("2024-01-01T00:00:00Z");

    private BrandFixture() {
        // Utility class - 인스턴스 생성 방지
    }

    /**
     * 기본 브랜드 생성 (나이키)
     *
     * @return Brand 인스턴스
     */
    public static Brand create() {
        return Brand.reconstitute(
                BrandId.of(1L),
                BrandCode.of("NIKE"),
                BrandNameKo.of("나이키"),
                BrandNameEn.of("Nike"),
                BrandLogoUrl.of("https://cdn.example.com/brand/nike.png"),
                BrandStatus.ACTIVE,
                DEFAULT_CREATED_AT,
                DEFAULT_UPDATED_AT);
    }

    /**
     * ID 지정하여 브랜드 생성
     *
     * @param id 브랜드 ID
     * @return Brand 인스턴스
     */
    public static Brand createWithId(Long id) {
        return Brand.reconstitute(
                BrandId.of(id),
                BrandCode.of("NIKE"),
                BrandNameKo.of("나이키"),
                BrandNameEn.of("Nike"),
                BrandLogoUrl.of("https://cdn.example.com/brand/nike.png"),
                BrandStatus.ACTIVE,
                DEFAULT_CREATED_AT,
                DEFAULT_UPDATED_AT);
    }

    /**
     * 비활성 브랜드 생성
     *
     * @return Brand 인스턴스 (비활성)
     */
    public static Brand createInactive() {
        return Brand.reconstitute(
                BrandId.of(99L),
                BrandCode.of("INACTIVE"),
                BrandNameKo.of("비활성브랜드"),
                BrandNameEn.empty(),
                BrandLogoUrl.empty(),
                BrandStatus.INACTIVE,
                DEFAULT_CREATED_AT,
                DEFAULT_UPDATED_AT);
    }

    /**
     * 영문명 없는 브랜드 생성
     *
     * @return Brand 인스턴스
     */
    public static Brand createWithoutEnglishName() {
        return Brand.reconstitute(
                BrandId.of(2L),
                BrandCode.of("KOREAN"),
                BrandNameKo.of("한국브랜드"),
                BrandNameEn.empty(),
                BrandLogoUrl.of("https://cdn.example.com/brand/korean.png"),
                BrandStatus.ACTIVE,
                DEFAULT_CREATED_AT,
                DEFAULT_UPDATED_AT);
    }

    /**
     * 로고 없는 브랜드 생성
     *
     * @return Brand 인스턴스
     */
    public static Brand createWithoutLogo() {
        return Brand.reconstitute(
                BrandId.of(3L),
                BrandCode.of("NOLOGO"),
                BrandNameKo.of("로고없음"),
                BrandNameEn.of("NoLogo"),
                BrandLogoUrl.empty(),
                BrandStatus.ACTIVE,
                DEFAULT_CREATED_AT,
                DEFAULT_UPDATED_AT);
    }

    /**
     * 여러 브랜드 목록 생성
     *
     * @return Brand 목록
     */
    public static List<Brand> createList() {
        return List.of(
                Brand.reconstitute(
                        BrandId.of(1L),
                        BrandCode.of("NIKE"),
                        BrandNameKo.of("나이키"),
                        BrandNameEn.of("Nike"),
                        BrandLogoUrl.of("https://cdn.example.com/brand/nike.png"),
                        BrandStatus.ACTIVE,
                        DEFAULT_CREATED_AT,
                        DEFAULT_UPDATED_AT),
                Brand.reconstitute(
                        BrandId.of(2L),
                        BrandCode.of("ADIDAS"),
                        BrandNameKo.of("아디다스"),
                        BrandNameEn.of("Adidas"),
                        BrandLogoUrl.of("https://cdn.example.com/brand/adidas.png"),
                        BrandStatus.ACTIVE,
                        DEFAULT_CREATED_AT,
                        DEFAULT_UPDATED_AT),
                Brand.reconstitute(
                        BrandId.of(3L),
                        BrandCode.of("PUMA"),
                        BrandNameKo.of("푸마"),
                        BrandNameEn.of("Puma"),
                        BrandLogoUrl.of("https://cdn.example.com/brand/puma.png"),
                        BrandStatus.ACTIVE,
                        DEFAULT_CREATED_AT,
                        DEFAULT_UPDATED_AT));
    }

    /**
     * 커스텀 브랜드 생성
     *
     * @param id 브랜드 ID
     * @param code 브랜드 코드
     * @param nameKo 한글명
     * @param nameEn 영문명 (null 가능)
     * @param logoUrl 로고 URL (null 가능)
     * @param active 활성 상태
     * @return Brand 인스턴스
     */
    public static Brand createCustom(
            Long id, String code, String nameKo, String nameEn, String logoUrl, boolean active) {
        return Brand.reconstitute(
                BrandId.of(id),
                BrandCode.of(code),
                BrandNameKo.of(nameKo),
                nameEn != null ? BrandNameEn.of(nameEn) : BrandNameEn.empty(),
                logoUrl != null ? BrandLogoUrl.of(logoUrl) : BrandLogoUrl.empty(),
                active ? BrandStatus.ACTIVE : BrandStatus.INACTIVE,
                DEFAULT_CREATED_AT,
                DEFAULT_UPDATED_AT);
    }
}
