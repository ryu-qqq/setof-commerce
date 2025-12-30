package com.connectly.partnerAdmin.module.qna.dto.query;

import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.qna.enums.QnaDetailType;
import com.connectly.partnerAdmin.module.qna.enums.QnaType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public abstract class AbstractCreateQna implements CreateQna{

    @Valid
    protected CreateQnaContents qnaContents;

    @NotNull(message = "비밀글 여부는 필수 값 입니다.")
    protected Yn privateYn;

    @NotNull(message = "질문 유형은 필수 값 입니다.")
    protected QnaType qnaType;

    @NotNull(message = "질문 상세 유형은 필수 값 입니다.")
    protected QnaDetailType qnaDetailType;

    @NotNull(message = "판매자 아이디는 필수입니다.")
    protected long sellerId;

    @JsonIgnore
    @Override
    public boolean isPrivate() {
        return privateYn.isYes();
    }



}
