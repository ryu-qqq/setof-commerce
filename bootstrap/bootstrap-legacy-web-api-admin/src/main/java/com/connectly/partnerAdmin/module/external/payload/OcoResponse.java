package com.connectly.partnerAdmin.module.external.payload;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OcoResponse <T> implements ExternalResponse<T>{

    private T apiResult;
    private OcoPage pageSet;
    private OcoResponseStatus responseStatus;


    public OcoResponse(T apiResult, OcoPage pageSet, OcoResponseStatus responseStatus) {
        this.apiResult = apiResult;
        this.pageSet = pageSet;
        this.responseStatus = responseStatus;
    }


    @Override
    public T getData() {
        return apiResult;
    }

    @Override
    public String getResult() {
        return responseStatus.getReturnMessage();
    }

    @Override
    public String getMessage() {
        return responseStatus.getReturnMessage();
    }

}
