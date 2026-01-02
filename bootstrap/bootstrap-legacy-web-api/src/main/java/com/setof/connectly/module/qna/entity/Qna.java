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
import com.setof.connectly.module.common.enums.UserType;
import com.setof.connectly.module.common.enums.Yn;
import com.setof.connectly.module.qna.entity.embedded.QnaContents;
import com.setof.connectly.module.qna.enums.QnaDetailType;
import com.setof.connectly.module.qna.enums.QnaStatus;
import com.setof.connectly.module.qna.enums.QnaType;
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Getter
@Table(name = "qna")
@Entity
public class Qna extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "QNA_ID")
    private long id;
    private QnaContents qnaContents;
    @Enumerated(EnumType.STRING)
    private Yn privateYn;
    @Enumerated(EnumType.STRING)
    private QnaStatus qnaStatus;
    @Enumerated(EnumType.STRING)
    private QnaType qnaType;
    @Enumerated(EnumType.STRING)
    private QnaDetailType qnaDetailType;
    private long sellerId;
    private long userId;
    @Enumerated(EnumType.STRING)
    private UserType userType;

    public void reply(){
        this.qnaStatus = QnaStatus.CLOSED;
    }

    public boolean isClosed(){
        return !qnaStatus.isOpen();
    }

    public boolean isOpen(){
        return this.qnaStatus.isOpen();
    }


    public void updateContents(QnaContents qnaContents){
        this.qnaContents = qnaContents;
    }


}
