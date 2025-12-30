package com.ryuqq.setof.adapter.in.rest.v2.board.mapper;

import com.ryuqq.setof.adapter.in.rest.v2.board.dto.query.SearchBoardV2ApiRequest;
import com.ryuqq.setof.application.board.dto.query.SearchBoardQuery;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import java.time.Instant;
import org.springframework.stereotype.Component;

/**
 * Board V2 API Mapper
 *
 * <p>API Request DTO → Application Query 변환을 담당합니다.
 *
 * @author development-team
 * @since 2.0.0
 */
@Component
public class BoardV2ApiMapper {

    private final ClockHolder clockHolder;

    public BoardV2ApiMapper(ClockHolder clockHolder) {
        this.clockHolder = clockHolder;
    }

    /**
     * SearchRequest → SearchQuery 변환 (Client용)
     *
     * <p>PUBLISHED 상태 + 표시 기간 내 게시글만 조회
     *
     * @param request API 요청
     * @return Application Query
     */
    public SearchBoardQuery toSearchQuery(SearchBoardV2ApiRequest request) {
        return SearchBoardQuery.forClient(
                request.boardType(),
                Instant.now(clockHolder.getClock()),
                null,
                request.getOffset(),
                request.getSize());
    }

    /**
     * 상단 고정 게시글 조회용 Query 생성
     *
     * @param boardType 게시판 타입
     * @param limit 조회 개수
     * @return Application Query
     */
    public SearchBoardQuery toPinnedQuery(String boardType, int limit) {
        return SearchBoardQuery.forClient(
                boardType, Instant.now(clockHolder.getClock()), true, 0, limit);
    }
}
