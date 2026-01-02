package com.connectly.partnerAdmin.module.payment.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum VBankCode {

    V007("007","수협", "BK07"),
    V090("090","카카오뱅크", "BK90"),
    V089("089","K뱅크", "BK89"),
    V027("027","한국씨티은행(한미은행)","BK27"),
    V081("081","하나은행(서울은행)","BK81"),
    V012("012","축협","BK12"),
    V035("035","제주은행", "BK12"),
    V037("037","전북은행","BK37"),
    V071("071","우체국", "BK71"),
    V020("020","우리은행","BK20"),
    V048("048","신협","BK48"),
    V088("088","신한은행","BK88"),
    V002("002","산업은행","BK02"),
    V032("032","부산은행","BK32"),
    V031("031","대구은행","BK31"),
    V011("011","농협","BK11"),
    V003("003","기업은행","BK03"),
    V034("034","광주은행","BK34"),
    V039("039","경남은행","BK39"),
    V023("023","SC제일은행","BK23"),
    V004("004","KB국민은행","BK04");

    private final String code;
    private final String displayName;
    private final String kcpCode;


    public static VBankCode ofCode(String code){
        return Arrays.stream(values())
                .filter(v -> v.getCode().equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("해당하는 가상 계좌가 존재하지 않습니다. code =>" + code));
    }


    public static VBankCode ofDisplayName(String displayName){
        if(displayName.equals("제주은행")) throw new IllegalArgumentException("해당하는 가상 계좌가 존재하지 않습니다. code =>" + displayName);
        return Arrays.stream(values())
                .filter(v -> v.getDisplayName().equals(displayName))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("해당하는 가상 계좌가 존재하지 않습니다. code =>" + displayName));
    }
}
