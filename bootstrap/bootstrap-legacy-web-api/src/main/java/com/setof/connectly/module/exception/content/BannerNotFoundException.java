package com.setof.connectly.module.exception.content;

import com.setof.connectly.module.display.enums.BannerType;
import org.springframework.http.HttpStatus;

public class BannerNotFoundException extends ContentException {

    public static final String CODE = "BANNER-404";
    public static final String MESSAGE = "해당 배너가 존재하지 않습니다";

    public BannerNotFoundException(BannerType bannerType) {
        super(CODE, HttpStatus.NOT_FOUND, MESSAGE + bannerType);
    }
}
