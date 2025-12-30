package com.connectly.partnerAdmin.module.qna.dto.fetch;

import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.qna.entity.embedded.QnaContents;
import com.connectly.partnerAdmin.module.qna.enums.QnaDetailType;
import com.connectly.partnerAdmin.module.qna.enums.QnaStatus;
import com.connectly.partnerAdmin.module.qna.enums.QnaType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FetchQnaResponse {

    private long qnaId;
    private QnaContents qnaContents;
    private Yn privateYn;
    private QnaStatus qnaStatus;
    private QnaType qnaType;
    private QnaDetailType qnaDetailType;
    private String sellerName;
    private UserInfoQnaDto userInfo;
    private QnaTarget qnaTarget;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime insertDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateDate;

    @Setter
    private List<QnaImageDto> qnaImages = new ArrayList<>();

    @Builder
    @QueryProjection
    public FetchQnaResponse(long qnaId, QnaContents qnaContents, Yn privateYn, QnaStatus qnaStatus, QnaType qnaType, QnaDetailType qnaDetailType, String sellerName, UserInfoQnaDto userInfo, QnaTarget qnaTarget, LocalDateTime insertDate, LocalDateTime updateDate) {
        this.qnaId = qnaId;
        this.qnaContents = qnaContents;
        this.privateYn = privateYn;
        this.qnaStatus = qnaStatus;
        this.qnaType = qnaType;
        this.qnaDetailType = qnaDetailType;
        this.sellerName = sellerName;
        this.userInfo = userInfo;
        this.qnaTarget = qnaTarget;
        this.insertDate = insertDate;
        this.updateDate = updateDate;
    }

}
