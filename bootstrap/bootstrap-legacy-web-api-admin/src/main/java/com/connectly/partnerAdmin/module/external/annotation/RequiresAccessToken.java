package com.connectly.partnerAdmin.module.external.annotation;

import com.connectly.partnerAdmin.module.order.enums.SiteName;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresAccessToken {
    SiteName siteName();
}
