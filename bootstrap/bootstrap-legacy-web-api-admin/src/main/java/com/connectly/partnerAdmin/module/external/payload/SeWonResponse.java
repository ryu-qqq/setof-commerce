package com.connectly.partnerAdmin.module.external.payload;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SeWonResponse <T> implements ExternalResponse<T>{

    private T datas;
    private String result;
    private String message;

    public SeWonResponse(T datas, String result, String message) {
        this.datas = datas;
        this.result = result;
        this.message = message;
    }

    @Override
    public T getData() {
        return datas;
    }
    @Override
    public String getResult() {
        return result;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
