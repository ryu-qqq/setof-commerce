package com.ryuqq.setof.storage.legacy.composite.web.brand.mapper;

import com.ryuqq.setof.application.legacy.brand.dto.response.LegacyBrandResult;
import com.ryuqq.setof.storage.legacy.composite.web.brand.dto.LegacyWebBrandQueryDto;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * LegacyWebBrandMapper - 레거시 Web 브랜드 Mapper.
 *
 * <p>QueryDto → Application Result 변환을 담당합니다.
 *
 * <p>PER-MAP-001: Mapper는 @Component로 등록.
 *
 * <p>PER-MAP-002: 수동 매핑 구현 (MapStruct 사용 안함).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyWebBrandMapper {

    /**
     * LegacyWebBrandQueryDto → LegacyBrandResult 변환.
     *
     * @param dto QueryDto
     * @return LegacyBrandResult
     */
    public LegacyBrandResult toResult(LegacyWebBrandQueryDto dto) {
        if (dto == null) {
            return null;
        }
        return LegacyBrandResult.of(
                dto.brandId(), dto.brandName(), dto.korBrandName(), dto.brandIconImageUrl());
    }

    /**
     * LegacyWebBrandQueryDto 목록 → LegacyBrandResult 목록 변환.
     *
     * @param dtos QueryDto 목록
     * @return LegacyBrandResult 목록
     */
    public List<LegacyBrandResult> toResults(List<LegacyWebBrandQueryDto> dtos) {
        if (dtos == null) {
            return List.of();
        }
        return dtos.stream().map(this::toResult).toList();
    }
}
