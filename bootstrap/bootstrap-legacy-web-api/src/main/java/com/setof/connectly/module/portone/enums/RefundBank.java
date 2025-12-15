package com.setof.connectly.module.portone.enums;

import com.setof.connectly.module.common.enums.EnumType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RefundBank implements EnumType {
    BK04("KB국민은행"),
    BK23("SC제일은행"),
    BK39("경남은행"),
    BK34("광주은행"),
    BK03("기업은행"),
    BK11("농협"),
    BK31("대구은행"),
    BK32("부산은행"),
    BK02("산업은행"),
    BK45("새마을금고"),
    BK07("수협"),
    BK88("신한은행"),
    BK48("신협"),
    BK81("하나(외환)은행"),
    BK20("우리은행"),
    BK71("우체국"),
    BK37("전북은행"),
    BK12("축협"),
    BK90("카카오뱅크"),
    BK89("케이뱅크"),
    BK27("한국씨티은행"),
    BK92("토스뱅크");

    private final String displayName;

    @Override
    public String getName() {
        return name();
    }

    @Override
    public String getDescription() {
        return getDisplayName();
    }
}
