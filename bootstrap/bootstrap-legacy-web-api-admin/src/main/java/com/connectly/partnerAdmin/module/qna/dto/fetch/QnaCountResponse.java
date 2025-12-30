package com.connectly.partnerAdmin.module.qna.dto.fetch;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class QnaCountResponse {
    private Long orderQnaCount;
    private Long productQnaCount;

    @QueryProjection
    public QnaCountResponse(Long orderQnaCount, Long productQnaCount) {
        this.orderQnaCount = orderQnaCount;
        this.productQnaCount = productQnaCount;
    }
}
