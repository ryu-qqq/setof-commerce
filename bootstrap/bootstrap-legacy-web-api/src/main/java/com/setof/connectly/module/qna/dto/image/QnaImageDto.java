package com.setof.connectly.module.qna.dto.image;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.setof.connectly.module.qna.enums.QnaIssueType;
import lombok.*;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
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

}
