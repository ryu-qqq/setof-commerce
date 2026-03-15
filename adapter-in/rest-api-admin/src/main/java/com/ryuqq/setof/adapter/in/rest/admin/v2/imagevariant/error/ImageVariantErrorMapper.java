package com.ryuqq.setof.adapter.in.rest.admin.v2.imagevariant.error;

import com.ryuqq.setof.adapter.in.rest.admin.common.mapper.ErrorMapper;
import com.ryuqq.setof.domain.common.exception.DomainException;
import com.ryuqq.setof.domain.imagevariant.exception.ImageVariantException;
import java.net.URI;
import java.util.Locale;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * ImageVariantErrorMapper - 이미지 Variant 도메인 예외를 HTTP 응답으로 변환.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class ImageVariantErrorMapper implements ErrorMapper {

    private static final String ERROR_TYPE_PREFIX = "/errors/image-variant";

    @Override
    public boolean supports(DomainException ex) {
        return ex instanceof ImageVariantException;
    }

    @Override
    public MappedError map(DomainException ex, Locale locale) {
        HttpStatus status = HttpStatus.valueOf(ex.httpStatus());
        return new MappedError(
                status,
                "Image Variant Error",
                ex.getMessage(),
                URI.create(ERROR_TYPE_PREFIX + "/" + ex.code().toLowerCase(Locale.ROOT)));
    }
}
