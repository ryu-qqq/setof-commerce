package com.ryuqq.setof.storage.legacy.composite.web.user.adapter;

import com.ryuqq.setof.application.legacy.user.dto.response.LegacyShippingAddressResult;
import com.ryuqq.setof.domain.legacy.user.dto.query.LegacyShippingAddressSearchCondition;
import com.ryuqq.setof.storage.legacy.composite.web.user.mapper.LegacyWebShippingAddressMapper;
import com.ryuqq.setof.storage.legacy.composite.web.user.repository.LegacyWebShippingAddressQueryDslRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * 레거시 배송지 조회 Adapter.
 *
 * <p>Persistence Layer의 진입점입니다.
 *
 * <p>TODO: Application Layer의 LegacyShippingAddressQueryPort implements 추가.
 *
 * <p>PER-ADP-001: Adapter는 @Component로 등록.
 *
 * <p>PER-ADP-002: Repository와 Mapper 조합으로 결과 반환.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyWebShippingAddressQueryAdapter {

    private final LegacyWebShippingAddressQueryDslRepository repository;
    private final LegacyWebShippingAddressMapper mapper;

    public LegacyWebShippingAddressQueryAdapter(
            LegacyWebShippingAddressQueryDslRepository repository,
            LegacyWebShippingAddressMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    /**
     * 사용자의 배송지 목록 조회.
     *
     * @param condition 검색 조건
     * @return 배송지 목록
     */
    public List<LegacyShippingAddressResult> fetchShippingAddresses(
            LegacyShippingAddressSearchCondition condition) {
        return mapper.toResults(repository.fetchShippingAddresses(condition));
    }

    /**
     * 특정 배송지 단건 조회.
     *
     * @param condition 검색 조건
     * @return 배송지 Optional
     */
    public Optional<LegacyShippingAddressResult> fetchShippingAddress(
            LegacyShippingAddressSearchCondition condition) {
        return repository.fetchShippingAddress(condition).map(mapper::toResult);
    }
}
