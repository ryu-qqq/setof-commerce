package com.ryuqq.setof.adapter.in.rest.admin.v2.board.mapper;

import com.ryuqq.setof.adapter.in.rest.admin.v2.board.dto.command.CreateBoardV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.board.dto.command.UpdateBoardV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.board.dto.query.SearchBoardV2ApiRequest;
import com.ryuqq.setof.application.board.dto.command.CreateBoardCommand;
import com.ryuqq.setof.application.board.dto.command.UpdateBoardCommand;
import com.ryuqq.setof.application.board.dto.query.SearchBoardQuery;
import org.springframework.stereotype.Component;

/**
 * Board Admin V2 API Mapper
 *
 * <p>API Request DTO → Application Command/Query 변환을 담당합니다.
 *
 * @author development-team
 * @since 2.0.0
 */
@Component
public class BoardAdminV2ApiMapper {

    /**
     * CreateRequest → CreateCommand 변환
     *
     * @param request API 요청
     * @param createdBy 작성자 ID (인증 정보에서 추출)
     * @return Application Command
     */
    public CreateBoardCommand toCreateCommand(CreateBoardV2ApiRequest request, Long createdBy) {
        return new CreateBoardCommand(
                request.boardType(),
                request.title(),
                request.content(),
                request.summary(),
                request.thumbnailUrl(),
                request.displayStartAt(),
                request.displayEndAt(),
                createdBy);
    }

    /**
     * UpdateRequest → UpdateCommand 변환
     *
     * @param boardId 게시글 ID
     * @param request API 요청
     * @param updatedBy 수정자 ID (인증 정보에서 추출)
     * @return Application Command
     */
    public UpdateBoardCommand toUpdateCommand(
            Long boardId, UpdateBoardV2ApiRequest request, Long updatedBy) {
        return new UpdateBoardCommand(
                boardId,
                request.title(),
                request.content(),
                request.summary(),
                request.thumbnailUrl(),
                request.displayStartAt(),
                request.displayEndAt(),
                updatedBy);
    }

    /**
     * SearchRequest → SearchQuery 변환 (Admin용)
     *
     * @param request API 요청
     * @return Application Query
     */
    public SearchBoardQuery toSearchQuery(SearchBoardV2ApiRequest request) {
        return SearchBoardQuery.forAdmin(
                request.boardType(),
                request.status(),
                request.pinned(),
                request.getOffset(),
                request.getSize());
    }
}
