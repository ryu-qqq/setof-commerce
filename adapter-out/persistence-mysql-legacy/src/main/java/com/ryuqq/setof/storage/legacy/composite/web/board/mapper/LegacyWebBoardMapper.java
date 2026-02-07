package com.ryuqq.setof.storage.legacy.composite.web.board.mapper;

import com.ryuqq.setof.application.legacy.board.dto.response.LegacyBoardPageResult;
import com.ryuqq.setof.application.legacy.board.dto.response.LegacyBoardResult;
import com.ryuqq.setof.storage.legacy.composite.web.board.dto.LegacyWebBoardQueryDto;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * LegacyWebBoardMapper - 레거시 게시판 Mapper.
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
public class LegacyWebBoardMapper {

    /**
     * QueryDto → LegacyBoardResult 변환.
     *
     * @param dto QueryDto
     * @return LegacyBoardResult
     */
    public LegacyBoardResult toResult(LegacyWebBoardQueryDto dto) {
        if (dto == null) {
            return null;
        }
        return LegacyBoardResult.of(dto.title(), dto.contents());
    }

    /**
     * QueryDto 목록 → LegacyBoardResult 목록 변환.
     *
     * @param dtos QueryDto 목록
     * @return LegacyBoardResult 목록
     */
    public List<LegacyBoardResult> toResults(List<LegacyWebBoardQueryDto> dtos) {
        if (dtos == null) {
            return List.of();
        }
        return dtos.stream().map(this::toResult).toList();
    }

    /**
     * 페이지 결과 생성.
     *
     * @param dtos QueryDto 목록
     * @param totalCount 전체 개수
     * @param pageNumber 현재 페이지 번호
     * @param pageSize 페이지 크기
     * @return LegacyBoardPageResult
     */
    public LegacyBoardPageResult toPageResult(
            List<LegacyWebBoardQueryDto> dtos, long totalCount, int pageNumber, int pageSize) {
        List<LegacyBoardResult> results = toResults(dtos);
        return LegacyBoardPageResult.of(results, totalCount, pageNumber, pageSize);
    }
}
