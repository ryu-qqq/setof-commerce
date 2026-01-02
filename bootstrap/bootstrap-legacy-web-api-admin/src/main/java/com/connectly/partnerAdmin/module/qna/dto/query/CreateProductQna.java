package com.connectly.partnerAdmin.module.qna.dto.query;

import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.qna.enums.QnaDetailType;
import com.connectly.partnerAdmin.module.qna.enums.QnaType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonTypeName("productQna")
public class CreateProductQna extends AbstractCreateQna{

    @Min(value = 1, message = "상품 번호는 0보다 커야합니다.")
    private long productGroupId;

    @JsonIgnore
    @Override
    public long getTargetId() {
        return productGroupId;
    }

    @Builder
    public CreateProductQna(CreateQnaContents qnaContents, Yn privateYn, QnaType qnaType, QnaDetailType qnaDetailType, long sellerId, long productGroupId) {
        super(qnaContents, privateYn, qnaType, qnaDetailType, sellerId);
        this.productGroupId = productGroupId;
    }

    public CreateProductQna(CreateQnaContents qnaContents, QnaType qnaType) {
        this.qnaContents = qnaContents;
        this.qnaType = qnaType;
    }

}
