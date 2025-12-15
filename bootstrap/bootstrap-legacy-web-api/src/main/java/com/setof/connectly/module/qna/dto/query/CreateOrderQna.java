package com.setof.connectly.module.qna.dto.query;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.setof.connectly.module.common.enums.Yn;
import com.setof.connectly.module.qna.dto.image.CreateQnaImage;
import com.setof.connectly.module.qna.entity.embedded.QnaContents;
import com.setof.connectly.module.qna.enums.QnaDetailType;
import com.setof.connectly.module.qna.enums.QnaType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonTypeName("orderQna")
public class CreateOrderQna extends AbstractCreateQna {

    @Min(value = 1, message = "주문 번호는 0보다 커야합니다.")
    private long orderId;

    @Size(max = 3, message = "질문 답변에 등록 할 수 있는 사진은 최대 3장입니다.")
    private List<CreateQnaImage> qnaImages = new ArrayList<>();

    @JsonIgnore
    @Override
    public long getTargetId() {
        return orderId;
    }

    public CreateOrderQna(
            QnaContents qnaContents,
            Yn privateYn,
            QnaType qnaType,
            QnaDetailType qnaDetailType,
            long sellerId,
            long orderId,
            List<CreateQnaImage> qnaImages) {
        super(qnaContents, privateYn, qnaType, qnaDetailType, sellerId);
        this.orderId = orderId;
        this.qnaImages = qnaImages;
    }

    public CreateOrderQna(long orderId, List<CreateQnaImage> qnaImages) {
        this.orderId = orderId;
        this.qnaImages = qnaImages;
    }
}
