package com.ryuqq.setof.adapter.in.rest.v1.qna.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "QnA 생성 응답")
public record CreateQnaV1ApiResponse(@Schema(description = "리뷰 ID", example = "100") Long id) {}
