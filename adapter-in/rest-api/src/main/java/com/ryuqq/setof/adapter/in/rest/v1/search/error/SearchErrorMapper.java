package com.ryuqq.setof.adapter.in.rest.v1.search.error;

import com.ryuqq.setof.adapter.in.rest.common.mapper.ErrorMapper;
import com.ryuqq.setof.domain.common.exception.DomainException;
import java.net.URI;
import java.util.Locale;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * SearchErrorMapper - 검색 도메인 예외를 HTTP 응답으로 변환.
 *
 * <p>API-ERR-001: 도메인별 ErrorMapper를 구현하여 DomainException을 HTTP 응답으로 매핑.
 *
 * <p>검색은 별도 도메인 예외가 없으므로, 검색 관련 에러 코드(SEARCH- prefix)를 처리합니다. 상품그룹 관련 예외는 ProductGroupErrorMapper가
 * 처리합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class SearchErrorMapper implements ErrorMapper {

    private static final String ERROR_TYPE_PREFIX = "/errors/search";

    @Override
    public boolean supports(DomainException ex) {
        String code = ex.code();
        return code != null && code.startsWith("SEARCH-");
    }

    @Override
    public MappedError map(DomainException ex, Locale locale) {
        HttpStatus status = HttpStatus.valueOf(ex.httpStatus());
        return new MappedError(
                status,
                "Search Error",
                ex.getMessage(),
                URI.create(ERROR_TYPE_PREFIX + "/" + ex.code().toLowerCase()));
    }
}
