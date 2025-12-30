package com.connectly.partnerAdmin.module.qna.dto.query;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CreateQnaAnswer {

    @NotNull(message = "qnaId는 필수입니다")
    private long qnaId;

    @Valid
    private CreateQnaContents qnaContents;

    @Size(max = 3,  message = "질문 답변에 등록 할 수 있는 사진은 최대 3장입니다.")
    private List<CreateQnaImage> qnaImages = new ArrayList<>();


}
