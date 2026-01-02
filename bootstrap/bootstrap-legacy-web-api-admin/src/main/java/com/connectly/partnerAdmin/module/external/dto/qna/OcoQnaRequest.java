package com.connectly.partnerAdmin.module.external.dto.qna;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class OcoQnaRequest {
    private long qid;
    private String answerSubject;
    private String answer;
}
