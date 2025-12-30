package com.connectly.partnerAdmin.module.external.dto.qna;

import com.connectly.partnerAdmin.module.external.core.ExMallQna;
import com.connectly.partnerAdmin.module.external.enums.oco.OcoQnaType;
import com.connectly.partnerAdmin.module.order.enums.SiteName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@JsonTypeName("ocoQna")
public class OcoQna implements ExMallQna {

    private int qid;
    private int pid;
    private String questionsubject;
    private String question;

    @JsonProperty("question_type")
    private OcoQnaType questionType;

    @Override
    public long getSiteId() {
        return SiteName.OCO.getSiteId();
    }

    @Override
    public SiteName getSiteName() {
        return SiteName.OCO;
    }

    @Override
    public long getExternalIdx() {
        return qid;
    }

    @Override
    public String getTitle() {
        return questionsubject;
    }

    @Override
    public String getContent() {
        return question;
    }

    @Override
    public long getTargetId() {
        return pid;
    }


}
