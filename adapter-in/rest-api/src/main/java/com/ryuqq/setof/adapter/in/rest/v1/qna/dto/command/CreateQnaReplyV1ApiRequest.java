package com.ryuqq.setof.adapter.in.rest.v1.qna.dto.command;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "QnA 답변 생성 요청 (다형성)")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = CreateProductQnaReplyV1ApiRequest.class, name = "productQna"),
    @JsonSubTypes.Type(value = CreateOrderQnaReplyV1ApiRequest.class, name = "orderQna")
})
public sealed interface CreateQnaReplyV1ApiRequest
        permits CreateProductQnaReplyV1ApiRequest, CreateOrderQnaReplyV1ApiRequest {}
