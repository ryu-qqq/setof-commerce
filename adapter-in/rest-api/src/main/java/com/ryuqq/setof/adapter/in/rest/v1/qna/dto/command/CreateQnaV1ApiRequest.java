
package com.ryuqq.setof.adapter.in.rest.v1.qna.dto.command;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "QnA 생성 요청 (다형성)")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({@JsonSubTypes.Type(value = CreateProductQnaV1ApiRequest.class, name = "productQna"),
        @JsonSubTypes.Type(value = CreateOrderQnaV1ApiRequest.class, name = "orderQna")})
public sealed interface CreateQnaV1ApiRequest
        permits CreateProductQnaV1ApiRequest, CreateOrderQnaV1ApiRequest {
}
