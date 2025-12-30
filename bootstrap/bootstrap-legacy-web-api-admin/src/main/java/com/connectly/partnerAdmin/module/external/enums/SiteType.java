package com.connectly.partnerAdmin.module.external.enums;

import com.connectly.partnerAdmin.module.common.enums.EnumType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SiteType implements EnumType {

    INTERLOCKING("외부 연동"),
    CRAWLING("크롤링"),
    EP("네이버 EP");

    private final String text;


    @Override
    public String getName() {
        return this.name(); // 열거형의 이름 반환
    }

    @Override
    public String getDescription() {
        return this.text; // 설명 반환
    }

}

