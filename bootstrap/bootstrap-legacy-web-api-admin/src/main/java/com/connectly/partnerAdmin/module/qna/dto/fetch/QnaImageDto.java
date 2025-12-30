package com.connectly.partnerAdmin.module.qna.dto.fetch;

import com.connectly.partnerAdmin.module.qna.enums.QnaIssueType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class QnaImageDto {

    private QnaIssueType qnaIssueType;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long qnaImageId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long qnaId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long qnaAnswerId;

    private String imageUrl;

    private int displayOrder;

    @QueryProjection
    public QnaImageDto(QnaIssueType qnaIssueType, Long qnaImageId, Long qnaId, Long qnaAnswerId, String imageUrl, int displayOrder) {
        this.qnaIssueType = qnaIssueType;
        this.qnaImageId = qnaImageId;
        this.qnaId = qnaId;
        this.qnaAnswerId = qnaAnswerId;
        this.imageUrl = imageUrl;
        this.displayOrder = displayOrder;
    }
}
