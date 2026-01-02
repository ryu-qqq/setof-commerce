package com.connectly.partnerAdmin.module.external.dto.qna;


import com.connectly.partnerAdmin.module.external.dto.SeWonRequestDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SewonQnaRequest extends SeWonRequestDto {

    private long id;
    private String note;

    public SewonQnaRequest(String customerId, String apiKey, long id, String note) {
        super(customerId, apiKey);
        this.id = id;
        this.note = note;
    }
}
