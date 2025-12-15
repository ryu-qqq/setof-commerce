package com.setof.connectly.module.display.entity.embedded;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Embeddable
public class TitleDetails {

    private String title1;
    private String title2;
    private String subTitle1;
    private String subTitle2;

    public TitleDetails(String title1, String title2, String subTitle1, String subTitle2) {
        this.title1 = title1;
        this.title2 = title2;
        this.subTitle1 = subTitle1;
        this.subTitle2 = subTitle2;
    }
}
