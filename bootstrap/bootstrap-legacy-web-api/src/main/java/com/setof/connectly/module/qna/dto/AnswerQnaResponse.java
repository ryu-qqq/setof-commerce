package com.setof.connectly.module.qna.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import com.setof.connectly.module.qna.dto.image.QnaImageDto;
import com.setof.connectly.module.qna.entity.embedded.QnaContents;
import com.setof.connectly.module.qna.enums.QnaWriterType;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class AnswerQnaResponse {

    private long qnaAnswerId;
    private Long qnaAnswerParentId;
    private QnaWriterType qnaWriterType;
    private QnaContents qnaContents;
    private List<QnaImageDto> qnaImages;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime insertDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateDate;

    @QueryProjection
    public AnswerQnaResponse(
            long qnaAnswerId,
            Long qnaAnswerParentId,
            QnaWriterType qnaWriterType,
            QnaContents qnaContents,
            LocalDateTime insertDate,
            LocalDateTime updateDate) {
        this.qnaAnswerId = qnaAnswerId;
        this.qnaAnswerParentId = qnaAnswerParentId;
        this.qnaWriterType = qnaWriterType;
        this.qnaContents = qnaContents;
        this.insertDate = insertDate;
        this.updateDate = updateDate;
        this.qnaImages = new ArrayList<>();
    }

    public void setQnaImages(List<QnaImageDto> qnaImages) {
        this.qnaImages = qnaImages;
    }

    @Override
    public int hashCode() {
        return String.valueOf(qnaAnswerId).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AnswerQnaResponse) {
            AnswerQnaResponse p = (AnswerQnaResponse) obj;
            return this.hashCode() == p.hashCode();
        }
        return false;
    }
}
