package com.setof.connectly.module.review.dto;

import com.setof.connectly.module.review.annotation.ValidRating;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class CreateReview {

    @NotNull(message = "주문 번호는 필수입니다")
    private long orderId;

    @NotNull(message = "상품 번호는 필수입니다")
    private long productGroupId;

    @NotNull(message = "리뷰 점수는 필수입니다")
    @ValidRating
    private double rating;

    @Length(max = 500, message = "리뷰 내용은 500자를 넘길 수 없습니다.")
    private String content;

    @Size(max = 3, message = "리뷰사진은 최대 3장 입니다.")
    private List<ReviewImageDto> reviewImages = new ArrayList<>();
}
