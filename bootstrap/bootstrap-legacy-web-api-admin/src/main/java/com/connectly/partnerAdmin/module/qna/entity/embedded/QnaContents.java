package com.connectly.partnerAdmin.module.qna.entity.embedded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class QnaContents {

    @Column(name = "TITLE", length = 100)
    private String title;

    @Setter
    @Column(name = "CONTENT", length = 500)
    private String content;

}
