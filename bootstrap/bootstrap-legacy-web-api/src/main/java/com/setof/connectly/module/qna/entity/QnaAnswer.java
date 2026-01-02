package com.setof.connectly.module.qna.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
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
import com.setof.connectly.module.qna.entity.embedded.QnaContents;
import com.setof.connectly.module.qna.enums.QnaStatus;
import com.setof.connectly.module.qna.enums.QnaWriterType;


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

    @Column(name = "qna_id")
    private long qnaId;

    @Column(name = "qna_parent_id")
    private Long qnaParentId;

    @Column(name = "qna_writer_type")
    @Enumerated(EnumType.STRING)
    private QnaWriterType qnaWriterType;

    @Enumerated(EnumType.STRING)
    private QnaStatus qnaStatus;

    @Embedded
    private QnaContents qnaContents;

    public void update(QnaContents qnaContents){
        this.qnaContents = qnaContents;
    }

    public void reply(){
        this.qnaStatus = QnaStatus.CLOSED;
    }

    public boolean isClosed(){
        return !this.qnaStatus.isOpen();
    }

    public boolean isOpen(){
        return this.qnaStatus.isOpen();
    }
}
