package com.ryuqq.setof.adapter.in.rest.v1.qna.dto.command;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "QnA 수정 요청 (다형성)")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = UpdateProductQnaV1ApiRequest.class, name = "productQna"),
    @JsonSubTypes.Type(value = UpdateOrderQnaV1ApiRequest.class, name = "orderQna")
})
public sealed interface UpdateQnaV1ApiRequest
        permits UpdateProductQnaV1ApiRequest, UpdateOrderQnaV1ApiRequest {}
