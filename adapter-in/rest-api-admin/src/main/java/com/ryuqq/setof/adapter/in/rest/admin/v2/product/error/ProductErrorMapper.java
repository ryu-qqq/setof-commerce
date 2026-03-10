package com.ryuqq.setof.adapter.in.rest.admin.v2.product.error;

import com.ryuqq.setof.adapter.in.rest.admin.common.mapper.ErrorMapper;
import com.ryuqq.setof.domain.common.exception.DomainException;
import com.ryuqq.setof.domain.product.exception.ProductException;
import com.ryuqq.setof.domain.product.exception.ProductInvalidPriceException;
import com.ryuqq.setof.domain.product.exception.ProductInvalidStatusTransitionException;
import com.ryuqq.setof.domain.product.exception.ProductNotFoundException;
import java.net.URI;
import java.util.Locale;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * ProductErrorMapper - 상품 도메인 예외를 HTTP 응답으로 변환.
 *
 * <p>OCP(Open-Closed Principle) 준수를 위해 도메인별로 구현체를 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Component
public class ProductErrorMapper implements ErrorMapper {

    private static final String ERROR_TYPE_PREFIX = "/errors/product";

    @Override
    public boolean supports(DomainException ex) {
        return ex instanceof ProductException;
    }

    @Override
    public MappedError map(DomainException ex, Locale locale) {
        HttpStatus status = HttpStatus.valueOf(ex.httpStatus());
        String title;
        if (ex instanceof ProductNotFoundException) {
            title = "Product Not Found";
        } else if (ex instanceof ProductInvalidPriceException) {
            title = "Product Invalid Price";
        } else if (ex instanceof ProductInvalidStatusTransitionException) {
            title = "Product Invalid Status Transition";
        } else {
            title = "Product Error";
        }
        return new MappedError(
                status,
                title,
                ex.getMessage(),
                URI.create(ERROR_TYPE_PREFIX + "/" + ex.code().toLowerCase(Locale.ROOT)));
    }
}
