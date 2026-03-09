package com.ryuqq.setof.adapter.in.rest.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Cookie 설정 프로퍼티.
 *
 * <p>rest-api.yml의 security.cookie.* 프로퍼티를 바인딩합니다.
 *
 * @author ryu-qqq
 * @since 1.2.0
 */
@ConfigurationProperties(prefix = "security.cookie")
public class CookieProperties {

    private String domain = "localhost";
    private boolean secure = false;
    private String sameSite = "Lax";

    public CookieProperties() {}

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public boolean isSecure() {
        return secure;
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    public String getSameSite() {
        return sameSite;
    }

    public void setSameSite(String sameSite) {
        this.sameSite = sameSite;
    }
}
