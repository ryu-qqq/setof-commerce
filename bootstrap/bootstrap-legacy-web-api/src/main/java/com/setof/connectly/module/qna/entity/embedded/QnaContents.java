package com.setof.connectly.module.qna.entity.embedded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.hibernate.validator.constraints.Length;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class QnaContents {

    @Column(name = "TITLE")
    @Length(max = 100, message = "질문/답변 제목은 100자를 넘길 수 없습니다.")
    private String title;

    @Column(name = "CONTENT")
    @Length(max = 500, message = "질문/답변 내용은 500자를 넘길 수 없습니다.")
    private String content;

    public void setContent(String content) {
        this.content = content;
    }
}
