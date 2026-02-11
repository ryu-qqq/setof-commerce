package com.ryuqq.setof.adapter.in.rest.v1.category.error;

import com.ryuqq.setof.adapter.in.rest.common.mapper.ErrorMapper;
import com.ryuqq.setof.domain.category.exception.CategoryException;
import com.ryuqq.setof.domain.common.exception.DomainException;
import java.net.URI;
import java.util.Locale;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * CategoryErrorMapper - 카테고리 도메인 예외를 HTTP 응답으로 변환.
 *
 * <p>API-ERR-001: 도메인별 ErrorMapper를 구현하여 DomainException을 HTTP 응답으로 매핑.
 *
 * <p>CategoryNotFoundException → 404 NOT_FOUND
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Component
public class CategoryErrorMapper implements ErrorMapper {

    private static final String ERROR_TYPE_PREFIX = "/errors/category";

    @Override
    public boolean supports(DomainException ex) {
        return ex instanceof CategoryException;
    }

    @Override
    public MappedError map(DomainException ex, Locale locale) {
        HttpStatus status = HttpStatus.valueOf(ex.httpStatus());
        return new MappedError(
                status,
                "Category Error",
                ex.getMessage(),
                URI.create(ERROR_TYPE_PREFIX + "/" + ex.code().toLowerCase()));
    }
}
