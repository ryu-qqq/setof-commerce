package com.connectly.partnerAdmin.module.qna.dto.query;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CreateQnaContents {

    @NotBlank(message = "질문/답변 제목은 비워둘 수 없습니다.")
    @Length(min = 1, max = 100, message = "질문/답변 제목은 최소 1자 최대 100자를 넘길 수 없습니다.")
    private String title;

    @NotBlank(message = "질문/답변 제목은 비워둘 수 없습니다.")
    @Length(min = 1, max = 500, message = "질문/답변 내용은 최소 1자 최대 500자를 넘길 수 없습니다.")
    private String content;

}

