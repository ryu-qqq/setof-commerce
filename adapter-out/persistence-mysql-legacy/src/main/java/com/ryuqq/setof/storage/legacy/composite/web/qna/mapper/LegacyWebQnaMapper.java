package com.ryuqq.setof.storage.legacy.composite.web.qna.mapper;

import com.ryuqq.setof.application.legacy.qna.dto.response.LegacyQnaResult;
import com.ryuqq.setof.storage.legacy.composite.web.qna.dto.LegacyWebQnaQueryDto;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * LegacyWebQnaMapper - 레거시 Web Q&A Mapper.
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
public class LegacyWebQnaMapper {

    /**
     * QueryDto -> LegacyQnaResult 변환.
     *
     * @param dto QueryDto
     * @return LegacyQnaResult
     */
    public LegacyQnaResult toResult(LegacyWebQnaQueryDto dto) {
        if (dto == null) {
            return null;
        }
        return LegacyQnaResult.of(
                dto.qnaId(),
                dto.title(),
                dto.content(),
                dto.privateYn(),
                dto.qnaStatus(),
                dto.qnaType(),
                dto.qnaDetailType(),
                dto.userType(),
                dto.userId(),
                dto.userName(),
                dto.sellerId(),
                dto.productGroupId(),
                dto.insertDate(),
                dto.updateDate());
    }

    /**
     * QueryDto 목록 -> LegacyQnaResult 목록 변환.
     *
     * @param dtos QueryDto 목록
     * @return LegacyQnaResult 목록
     */
    public List<LegacyQnaResult> toResults(List<LegacyWebQnaQueryDto> dtos) {
        if (dtos == null) {
            return List.of();
        }
        return dtos.stream().map(this::toResult).toList();
    }
}
