package com.connectly.partnerAdmin.module.crawl.enums;

import com.connectly.partnerAdmin.module.common.enums.EnumType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CrawlErrorCode implements EnumType {

    //404 NOT FOUND
    CRAWL_PRODUCT_GROUP_NOT_FOUND("PRODUCT_002",HttpStatus.NOT_FOUND),

    ;



    private final String code;

    private final HttpStatus httpStatus;

    @Override
    public String getName() {
        return code;
    }

    @Override
    public String getDescription() {
        return name();
    }

}
