package com.setof.connectly.module.qna.dto.image;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateQnaImage {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long qnaImageId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long qnaId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long qnaAnswerId;

    private String imageUrl;

    @NotNull(message = "displayOrder는 필수입니다")
    private int displayOrder;

    @Builder
    @QueryProjection
    public CreateQnaImage(
            Long qnaImageId, Long qnaId, Long qnaAnswerId, String imageUrl, int displayOrder) {
        this.qnaImageId = qnaImageId;
        this.qnaId = qnaId;
        this.qnaAnswerId = qnaAnswerId;
        this.imageUrl = imageUrl;
        this.displayOrder = displayOrder;
    }

    @Builder
    @QueryProjection
    public CreateQnaImage(Long qnaImageId, String imageUrl, int displayOrder) {
        this.qnaImageId = qnaImageId;
        this.imageUrl = imageUrl;
        this.displayOrder = displayOrder;
    }
}
