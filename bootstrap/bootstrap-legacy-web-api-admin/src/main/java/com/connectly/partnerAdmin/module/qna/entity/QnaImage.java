package com.connectly.partnerAdmin.module.qna.entity;

import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import com.connectly.partnerAdmin.module.qna.enums.QnaIssueType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;




@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Getter
@Table(name = "QNA_IMAGE")
@Entity
public class QnaImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "QNA_IMAGE_ID")
    private long id;

    @Enumerated(EnumType.STRING)
    private QnaIssueType qnaIssueType;

    @Setter
    @Column(name = "IMAGE_URL")
    private String imageUrl;

    @Setter
    @Column(name = "DISPLAY_ORDER")
    private int displayOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "QNA_ID")
    @JsonBackReference
    private Qna qna;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "QNA_ANSWER_ID")
    private QnaAnswer qnaAnswer;


    public void setQna(Qna qna) {
        if (this.qna != null) {
            this.qna.getQnaImages().remove(this);
        }

        this.qna = qna;

        if (qna != null && !qna.getQnaImages().contains(this)) {
            qna.getQnaImages().add(this);
        }
    }

    public void setQnaAnswer(QnaAnswer qnaAnswer) {
        if (this.qnaAnswer != null) {
            this.qnaAnswer.getQnaImages().remove(this);
        }

        this.qnaAnswer = qnaAnswer;

        if (qnaAnswer != null && !qnaAnswer.getQnaImages().contains(this)) {
            qnaAnswer.getQnaImages().add(this);
        }
    }



}
