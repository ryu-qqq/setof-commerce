package com.ryuqq.setof.adapter.in.rest.admin.v1.content.error;

import com.ryuqq.setof.adapter.in.rest.admin.common.mapper.ErrorMapper;
import com.ryuqq.setof.domain.banner.exception.BannerException;
import com.ryuqq.setof.domain.common.exception.DomainException;
import com.ryuqq.setof.domain.navigation.exception.NavigationException;
import java.net.URI;
import java.util.Locale;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * ContentErrorMapper - 콘텐츠 도메인(배너, 네비게이션) 예외를 HTTP 응답으로 변환.
 *
 * <p>OCP(Open-Closed Principle) 준수를 위해 도메인별로 구현체를 생성합니다.
 *
 * <p>배너(BannerException)와 네비게이션(NavigationException)을 하나의 매퍼에서 처리합니다. 레거시에서는 배너/GNB가 모두 content 모듈에
 * 속하므로 통합 처리합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class ContentErrorMapper implements ErrorMapper {

    private static final String ERROR_TYPE_PREFIX = "/errors/content";

    @Override
    public boolean supports(DomainException ex) {
        return ex instanceof BannerException || ex instanceof NavigationException;
    }

    @Override
    public MappedError map(DomainException ex, Locale locale) {
        HttpStatus status = HttpStatus.valueOf(ex.httpStatus());
        String subDomain = resolveSubDomain(ex);
        return new MappedError(
                status,
                "Content Error",
                ex.getMessage(),
                URI.create(ERROR_TYPE_PREFIX + "/" + subDomain + "/" + ex.code().toLowerCase()));
    }

    private String resolveSubDomain(DomainException ex) {
        if (ex instanceof BannerException) {
            return "banner";
        }
        if (ex instanceof NavigationException) {
            return "navigation";
        }
        return "unknown";
    }
}
