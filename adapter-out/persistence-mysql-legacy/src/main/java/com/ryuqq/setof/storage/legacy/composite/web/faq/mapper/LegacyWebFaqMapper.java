package com.ryuqq.setof.storage.legacy.composite.web.faq.mapper;

import com.ryuqq.setof.application.legacy.faq.dto.response.LegacyFaqResult;
import com.ryuqq.setof.storage.legacy.composite.web.faq.dto.LegacyWebFaqQueryDto;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * LegacyWebFaqMapper - 레거시 Web FAQ Mapper.
 *
 * <p>QueryDto -> Application Result 변환을 담당합니다.
 *
 * <p>PER-MAP-001: Mapper는 @Component로 등록.
 *
 * <p>PER-MAP-002: 수동 매핑 구현 (MapStruct 사용 안함).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyWebFaqMapper {

    /**
     * QueryDto -> LegacyFaqResult 변환.
     *
     * @param dto QueryDto
     * @return LegacyFaqResult
     */
    public LegacyFaqResult toResult(LegacyWebFaqQueryDto dto) {
        if (dto == null) {
            return null;
        }
        return LegacyFaqResult.of(dto.faqType(), dto.title(), dto.contents());
    }

    /**
     * QueryDto 목록 -> LegacyFaqResult 목록 변환.
     *
     * @param dtos QueryDto 목록
     * @return LegacyFaqResult 목록
     */
    public List<LegacyFaqResult> toResults(List<LegacyWebFaqQueryDto> dtos) {
        if (dtos == null) {
            return List.of();
        }
        return dtos.stream().map(this::toResult).toList();
    }
}
