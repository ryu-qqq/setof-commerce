package com.setof.connectly.module.qna.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.setof.connectly.module.common.BaseEntity;
import com.setof.connectly.module.qna.enums.QnaIssueType;

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

    @Column(name = "QNA_ID")
    private long qnaId;

    @Column(name = "QNA_ANSWER_ID")
    private Long qnaAnswerId;

    @Column(name = "IMAGE_URL")
    private String imageUrl;

    @Column(name = "DISPLAY_ORDER")
    private int displayOrder;


    public QnaImage(QnaIssueType qnaIssueType, long qnaId, Long qnaAnswerId, String imageUrl, int displayOrder) {
        this.qnaIssueType = qnaIssueType;
        this.qnaId = qnaId;
        this.qnaAnswerId = qnaAnswerId;
        this.imageUrl = imageUrl;
        this.displayOrder = displayOrder;
    }

    public boolean isQuestionImages(){
        return qnaIssueType.isQuestion();
    }

}
