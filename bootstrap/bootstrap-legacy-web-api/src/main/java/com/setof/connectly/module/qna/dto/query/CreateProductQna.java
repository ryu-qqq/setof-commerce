package com.setof.connectly.module.qna.dto.query;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.setof.connectly.module.common.enums.Yn;
import com.setof.connectly.module.qna.entity.embedded.QnaContents;
import com.setof.connectly.module.qna.enums.QnaDetailType;
import com.setof.connectly.module.qna.enums.QnaType;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonTypeName("productQna")
public class CreateProductQna extends AbstractCreateQna {

    @Min(value = 1, message = "상품 번호는 0보다 커야합니다.")
    private long productGroupId;

    @JsonIgnore
    @Override
    public long getTargetId() {
        return productGroupId;
    }

    public CreateProductQna(
            QnaContents qnaContents,
            Yn privateYn,
            QnaType qnaType,
            QnaDetailType qnaDetailType,
            long sellerId,
            long productGroupId) {
        super(qnaContents, privateYn, qnaType, qnaDetailType, sellerId);
        this.productGroupId = productGroupId;
    }

    public CreateProductQna(long productGroupId) {
        this.productGroupId = productGroupId;
    }
}
