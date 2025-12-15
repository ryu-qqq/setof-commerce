package com.setof.connectly.module.qna.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.querydsl.core.annotations.QueryProjection;
import com.setof.connectly.module.common.mapper.LastDomainIdProvider;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class QnaResponse implements LastDomainIdProvider {
    private FetchQnaResponse qna;
    private Set<AnswerQnaResponse> answerQnas;

    @QueryProjection
    public QnaResponse(FetchQnaResponse qna, Set<AnswerQnaResponse> answerQnas) {
        this.qna = qna;
        if (answerQnas == null || answerQnas.isEmpty() || answerQnas.stream().allMatch(this::isEmptyAnswer)) {
            this.answerQnas = new HashSet<>(); // 비어 있을 경우 빈 Set 할당
        } else {
            this.answerQnas = answerQnas;
        }
    }

    private boolean isEmptyAnswer(AnswerQnaResponse answer) {
        return answer.getQnaAnswerId() == 0 && answer.getQnaAnswerParentId() == null && answer.getQnaContents() == null;
    }

    public void notPermissionReadQna(){
        answerQnas = new HashSet<>();
        qna.restrictContent();
    }

    @JsonIgnore
    public boolean isOrderQna(){
        return qna.getQnaType().isOrderQna();
    }

    @Override
    public Long getId() {
        return qna.getQnaId();
    }
}
