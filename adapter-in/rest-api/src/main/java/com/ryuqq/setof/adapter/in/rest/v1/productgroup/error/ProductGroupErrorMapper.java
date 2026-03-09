package com.ryuqq.setof.adapter.in.rest.v1.productgroup.error;

import com.ryuqq.setof.adapter.in.rest.common.mapper.ErrorMapper;
import com.ryuqq.setof.domain.common.exception.DomainException;
import com.ryuqq.setof.domain.productgroup.exception.ProductGroupException;
import java.net.URI;
import java.util.Locale;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * ProductGroupErrorMapper - 상품그룹 도메인 예외를 HTTP 응답으로 변환.
 *
 * <p>API-ERR-001: 도메인별 ErrorMapper를 구현하여 DomainException을 HTTP 응답으로 매핑.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class ProductGroupErrorMapper implements ErrorMapper {

    private static final String ERROR_TYPE_PREFIX = "/errors/product-group";

    @Override
    public boolean supports(DomainException ex) {
        return ex instanceof ProductGroupException;
    }

    @Override
    public MappedError map(DomainException ex, Locale locale) {
        HttpStatus status = HttpStatus.valueOf(ex.httpStatus());
        return new MappedError(
                status,
                "ProductGroup Error",
                ex.getMessage(),
                URI.create(ERROR_TYPE_PREFIX + "/" + ex.code().toLowerCase()));
    }
}
