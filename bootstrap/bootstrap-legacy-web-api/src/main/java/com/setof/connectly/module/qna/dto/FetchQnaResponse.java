package com.setof.connectly.module.qna.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;
import com.setof.connectly.module.common.enums.UserType;
import com.setof.connectly.module.common.enums.Yn;
import com.setof.connectly.module.qna.dto.image.QnaImageDto;
import com.setof.connectly.module.qna.entity.embedded.QnaContents;
import com.setof.connectly.module.qna.enums.QnaDetailType;
import com.setof.connectly.module.qna.enums.QnaStatus;
import com.setof.connectly.module.qna.enums.QnaType;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FetchQnaResponse {
    private long qnaId;
    private QnaContents qnaContents;
    private Yn privateYn;
    private QnaStatus qnaStatus;
    private QnaType qnaType;
    private QnaDetailType qnaDetailType;
    private UserType userType;

    private List<QnaImageDto> qnaImages;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private QnaTarget qnaTarget;

    @JsonIgnore private long userId;
    private String userName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime insertDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateDate;

    @Builder
    @QueryProjection
    public FetchQnaResponse(
            long qnaId,
            QnaContents qnaContents,
            Yn privateYn,
            QnaStatus qnaStatus,
            QnaType qnaType,
            QnaDetailType qnaDetailType,
            UserType userType,
            long userId,
            String userName,
            LocalDateTime insertDate,
            LocalDateTime updateDate) {
        this.qnaId = qnaId;
        this.qnaContents = qnaContents;
        this.privateYn = privateYn;
        this.qnaStatus = qnaStatus;
        this.qnaType = qnaType;
        this.qnaDetailType = qnaDetailType;
        this.userType = userType;
        this.userId = userId;
        this.userName = userName;
        this.insertDate = insertDate;
        this.updateDate = updateDate;
        this.qnaImages = new ArrayList<>();
    }

    @QueryProjection
    public FetchQnaResponse(
            long qnaId,
            QnaContents qnaContents,
            Yn privateYn,
            QnaStatus qnaStatus,
            QnaType qnaType,
            QnaDetailType qnaDetailType,
            UserType userType,
            QnaTarget qnaTarget,
            long userId,
            String userName,
            LocalDateTime insertDate,
            LocalDateTime updateDate) {
        this.qnaId = qnaId;
        this.qnaContents = qnaContents;
        this.privateYn = privateYn;
        this.qnaStatus = qnaStatus;
        this.qnaType = qnaType;
        this.qnaDetailType = qnaDetailType;
        this.userType = userType;
        this.userId = userId;
        this.userName = userName;
        this.qnaTarget = qnaTarget;
        this.insertDate = insertDate;
        this.updateDate = updateDate;
        this.qnaImages = new ArrayList<>();
    }

    @JsonIgnore
    public boolean isPrivateQna() {
        return this.privateYn.isYes();
    }

    public void restrictContent() {
        this.qnaContents.setContent("비밀글 입니다.");
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setQnaImages(List<QnaImageDto> qnaImages) {
        this.qnaImages = qnaImages;
    }
}
