package com.connectly.partnerAdmin.module.qna.dto;

import com.connectly.partnerAdmin.module.qna.dto.fetch.QnaImageDto;
import com.connectly.partnerAdmin.module.qna.enums.QnaStatus;
import com.connectly.partnerAdmin.module.qna.enums.QnaType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class CreateQnaAnswerResponse {

    private long qnaId;
    private long qnaAnswerId;
    private QnaType qnaType;
    private QnaStatus qnaStatus;
    private List<QnaImageDto> qnaImages = new ArrayList<>();
}
