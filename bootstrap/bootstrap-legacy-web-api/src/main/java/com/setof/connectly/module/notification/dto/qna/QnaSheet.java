package com.setof.connectly.module.notification.dto.qna;

import com.querydsl.core.annotations.QueryProjection;
import com.setof.connectly.module.qna.enums.QnaDetailType;
import com.setof.connectly.module.qna.enums.QnaType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QnaSheet {

    private long qnaId;
    private QnaType qnaType;

    private QnaDetailType qnaDetailType;
    private String productGroupName;
    private long targetId;
    private String phoneNumber;

    public QnaSheet(long qnaId, QnaType qnaType) {
        this.qnaId = qnaId;
        this.qnaType = qnaType;
    }

    @QueryProjection
    public QnaSheet(long qnaId, QnaType qnaType, QnaDetailType qnaDetailType, String productGroupName, long targetId, String phoneNumber) {
        this.qnaId = qnaId;
        this.qnaType = qnaType;
        this.qnaDetailType = qnaDetailType;
        this.productGroupName = productGroupName;
        this.targetId = targetId;
        this.phoneNumber = phoneNumber;
    }

    public void setProductGroupName(String productGroupName) {
        this.productGroupName = productGroupName;
    }

    public void setTargetId(long targetId) {
        this.targetId = targetId;
    }

    public void setQnaDetailType(QnaDetailType qnaDetailType) {
        this.qnaDetailType = qnaDetailType;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    public String getSubStringProductGroupName(){
        if (productGroupName.length() > 14) {
            return productGroupName.substring(0, 10) + "...";
        }
        return productGroupName;
    }

}
