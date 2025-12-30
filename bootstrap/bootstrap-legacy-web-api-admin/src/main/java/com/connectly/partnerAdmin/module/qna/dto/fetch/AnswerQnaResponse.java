package com.connectly.partnerAdmin.module.qna.dto.fetch;

import com.connectly.partnerAdmin.module.qna.entity.embedded.QnaContents;
import com.connectly.partnerAdmin.module.qna.enums.QnaWriterType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AnswerQnaResponse {

    private long qnaAnswerId;

    private Long qnaAnswerParentId;

    private QnaWriterType qnaWriterType;

    private QnaContents qnaContents;

    @Setter
    private List<QnaImageDto> qnaImages = new ArrayList<>();

    private String insertOperator;
    private String updateOperator;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime insertDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateDate;

    @QueryProjection
    public AnswerQnaResponse(long qnaAnswerId, Long qnaAnswerParentId, QnaWriterType qnaWriterType, QnaContents qnaContents, String insertOperator, String updateOperator, LocalDateTime insertDate, LocalDateTime updateDate) {
        this.qnaAnswerId = qnaAnswerId;
        this.qnaAnswerParentId = qnaAnswerParentId;
        this.qnaWriterType = qnaWriterType;
        this.qnaContents = qnaContents;
        this.insertOperator = insertOperator;
        this.updateOperator = updateOperator;
        this.insertDate = insertDate;
        this.updateDate = updateDate;
    }


    @Override
    public int hashCode() {
        return  (String.valueOf(qnaAnswerId)).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof AnswerQnaResponse p) {
            return this.hashCode()==p.hashCode();
        }
        return false;
    }
}
