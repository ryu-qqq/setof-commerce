package com.connectly.partnerAdmin.module.qna.dto.query;

import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.qna.entity.embedded.QnaContents;
import com.connectly.partnerAdmin.module.qna.enums.QnaDetailType;
import com.connectly.partnerAdmin.module.qna.enums.QnaType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonTypeName("orderQna")
public class CreateOrderQna extends AbstractCreateQna{

    @Min(value = 1, message = "주문 번호는 0보다 커야합니다.")
    private long orderId;

    @Size(max = 3,  message = "사진 이미지는 최대 3장만 등록 가능합니다.")
    private List<CreateQnaImage> qnaImages = new ArrayList<>();


    @JsonIgnore
    @Override
    public long getTargetId() {
        return orderId;
    }

    public CreateOrderQna(CreateQnaContents qnaContents, Yn privateYn, QnaType qnaType, QnaDetailType qnaDetailType, long sellerId, long orderId, List<CreateQnaImage> qnaImages) {
        super(qnaContents, privateYn, qnaType, qnaDetailType, sellerId);
        this.orderId = orderId;
        this.qnaImages = qnaImages;
    }

    public CreateOrderQna(long orderId, List<CreateQnaImage> qnaImages) {
        this.orderId = orderId;
        this.qnaImages = qnaImages;
    }
}
