package com.connectly.partnerAdmin.module.qna.dto.query;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;



@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UpdateQnaAnswer extends CreateQnaAnswer {

    @NotNull(message = "qnaAnswerId는 필수입니다")
    private Long qnaAnswerId;

}
