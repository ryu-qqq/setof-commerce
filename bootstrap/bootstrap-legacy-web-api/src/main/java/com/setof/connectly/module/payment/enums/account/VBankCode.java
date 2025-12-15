package com.setof.connectly.module.payment.enums.account;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum VBankCode {
    V007("007", "수협"),
    V090("090", "카카오뱅크"),
    V089("089", "K뱅크"),
    V027("027", "한국씨티은행(한미은행)"),
    V081("081", "하나은행(서울은행)"),
    V012("012", "축협"),
    V035("035", "제주은행"),
    V037("037", "전북은행"),
    V071("071", "우체국"),
    V020("020", "우리은행"),
    V048("048", "신협"),
    V088("088", "신한은행"),
    V002("002", "산업은행"),
    V032("032", "부산은행"),
    V031("031", "대구은행"),
    V011("011", "농협"),
    V003("003", "기업은행"),
    V034("034", "광주은행"),
    V039("039", "경남은행"),
    V023("023", "SC제일은행"),
    V004("004", "KB국민은행"),
    NONE("", "");
    private final String code;
    private final String displayName;

    public static VBankCode ofCode(String code) {
        return Arrays.stream(values()).filter(v -> v.getCode().equals(code)).findAny().orElse(NONE);
    }

    public static VBankCode ofDisplayName(String displayName) {
        return Arrays.stream(values())
                .filter(v -> v.getDisplayName().equals(displayName))
                .findAny()
                .orElseThrow(
                        () ->
                                new IllegalArgumentException(
                                        "해당하는 가상 계좌가 존재하지 않습니다. code =>" + displayName));
    }
}
