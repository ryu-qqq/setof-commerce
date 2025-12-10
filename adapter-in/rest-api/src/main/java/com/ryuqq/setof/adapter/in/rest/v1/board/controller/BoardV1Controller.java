package com.ryuqq.setof.adapter.in.rest.v1.board.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ryuqq.setof.adapter.in.rest.auth.paths.ApiPaths;
import com.ryuqq.setof.adapter.in.rest.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.common.dto.PageApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.board.dto.query.BoardV1SearchApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.board.dto.response.BoardV1ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Board (Legacy V1)", description = "레거시 Board API - V2로 마이그레이션 권장")
@Deprecated
@RestController
@RequestMapping
public class BoardV1Controller {

    @Deprecated
    @Operation(summary = "[Legacy] Board 목록 조회", description = "Board 목록을 조회합니다.")
    @GetMapping(ApiPaths.News.BOARD)
    public ResponseEntity<ApiResponse<PageApiResponse<List<BoardV1ApiResponse>>>> getBoards(
            @ModelAttribute BoardV1SearchApiRequest request) {
        throw new UnsupportedOperationException("Board 목록 조회 기능은 아직 지원되지 않습니다.");
    }

}
