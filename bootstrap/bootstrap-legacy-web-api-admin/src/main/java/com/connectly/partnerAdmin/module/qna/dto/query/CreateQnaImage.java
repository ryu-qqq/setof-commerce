package com.connectly.partnerAdmin.module.qna.dto.query;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CreateQnaImage {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long qnaImageId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long qnaId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long qnaAnswerId;

    @Setter
    private String imageUrl;

    @NotNull(message = "displayOrder는 필수입니다")
    private int displayOrder;

}
