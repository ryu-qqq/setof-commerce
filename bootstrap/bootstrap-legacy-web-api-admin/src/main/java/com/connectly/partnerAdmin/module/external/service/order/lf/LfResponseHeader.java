package com.connectly.partnerAdmin.module.external.service.order.lf;

public record LfResponseHeader(
    String format,
    String charset,
    String resultCode,
    String errorMessage
) {
}
