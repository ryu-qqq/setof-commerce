package com.connectly.partnerAdmin.module.display.exception;

import com.connectly.partnerAdmin.module.display.enums.ContentErrorCode;

public class ContentNotFoundException extends ContentException {


    public ContentNotFoundException(String message) {
        super(ContentErrorCode.CONTENT_NOT_FOUND.getCode(), ContentErrorCode.CONTENT_NOT_FOUND.getHttpStatus(), message);
    }

}
