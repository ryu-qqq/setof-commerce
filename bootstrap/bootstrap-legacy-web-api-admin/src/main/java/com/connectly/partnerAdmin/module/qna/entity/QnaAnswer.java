package com.connectly.partnerAdmin.module.qna.entity;


import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import com.connectly.partnerAdmin.module.qna.dto.query.CreateQnaAnswer;
import com.connectly.partnerAdmin.module.qna.dto.query.CreateQnaContents;
import com.connectly.partnerAdmin.module.qna.entity.embedded.QnaContents;
import com.connectly.partnerAdmin.module.qna.enums.QnaStatus;
import com.connectly.partnerAdmin.module.qna.enums.QnaWriterType;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Getter
@Table(name = "qna_answer")
@Entity
public class QnaAnswer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "qna_answer_id")
    private long id;

    @Column(name = "qna_writer_type")
    @Enumerated(EnumType.STRING)
    private QnaWriterType qnaWriterType;

    @Enumerated(EnumType.STRING)
    private QnaStatus qnaStatus;

    @Setter
    @Embedded
    private QnaContents qnaContents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "QNA_ID", nullable = false)
    private Qna qna;


    private Long qnaParentId;

    @OneToMany(mappedBy = "qnaAnswer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<QnaImage> qnaImages = new ArrayList<>();


    public QnaAnswer(Qna qna, CreateQnaAnswer createQnaAnswer, Optional<Long> qnaAnswerIdOpt) {
        this.qnaWriterType = QnaWriterType.SELLER;
        this.qnaStatus = QnaStatus.OPEN;
        doAnswer(createQnaAnswer.getQnaContents());
        this.qna = qna;
        qnaAnswerIdOpt.ifPresent(aLong -> this.qnaParentId = aLong);
    }

    public void addQnaImages(QnaImage image) {
        qnaImages.add(image);
        image.setQnaAnswer(this);
    }

    public void deleteQnaImages(QnaImage image) {
        this.qnaImages.remove(image);
    }

    public void reply(){
        this.qnaStatus = QnaStatus.CLOSED;
    }


    public void doAnswer(CreateQnaContents createQnaContents){
        this.qnaContents = new QnaContents(createQnaContents.getTitle(), createQnaContents.getContent());
    }

}
