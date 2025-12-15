package com.setof.connectly.module.qna.dto.query;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.setof.connectly.module.common.enums.Yn;
import com.setof.connectly.module.qna.entity.embedded.QnaContents;
import com.setof.connectly.module.qna.enums.QnaDetailType;
import com.setof.connectly.module.qna.enums.QnaType;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public abstract class AbstractCreateQna implements CreateQna {

    protected QnaContents qnaContents;
    protected Yn privateYn;
    protected QnaType qnaType;
    protected QnaDetailType qnaDetailType;

    @NotNull(message = "셀러 아이디는 필수입니다.")
    protected Long sellerId;

    @JsonIgnore
    @Override
    public boolean isPrivate() {
        return privateYn.isYes();
    }
}
