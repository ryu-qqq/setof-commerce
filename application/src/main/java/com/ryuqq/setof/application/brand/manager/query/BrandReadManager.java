package com.ryuqq.setof.application.brand.manager.query;

import com.ryuqq.setof.application.brand.port.out.query.BrandQueryPort;
import com.ryuqq.setof.domain.brand.aggregate.Brand;
import com.ryuqq.setof.domain.brand.exception.BrandNotFoundException;
import com.ryuqq.setof.domain.brand.query.criteria.BrandSearchCriteria;
import com.ryuqq.setof.domain.brand.vo.BrandCode;
import com.ryuqq.setof.domain.brand.vo.BrandId;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Brand Read Manager
 *
 * <p>단일 Query Port 조회를 담당하는 Read Manager
 *
 * <p>읽기 전용 트랜잭션 관리
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class BrandReadManager {

    private final BrandQueryPort brandQueryPort;

    public BrandReadManager(BrandQueryPort brandQueryPort) {
        this.brandQueryPort = brandQueryPort;
    }

    /**
     * Brand ID로 조회 (필수)
     *
     * @param brandId Brand ID (Long)
     * @return 조회된 Brand
     * @throws BrandNotFoundException Brand를 찾을 수 없는 경우
     */
    @Transactional(readOnly = true)
    public Brand findById(Long brandId) {
        BrandId id = BrandId.of(brandId);
        return brandQueryPort.findById(id).orElseThrow(() -> new BrandNotFoundException(brandId));
    }

    /**
     * Brand ID로 조회 (선택)
     *
     * @param brandId Brand ID (Long)
     * @return 조회된 Brand (Optional)
     */
    @Transactional(readOnly = true)
    public Optional<Brand> findByIdOptional(Long brandId) {
        BrandId id = BrandId.of(brandId);
        return brandQueryPort.findById(id);
    }

    /**
     * 브랜드 코드로 조회 (필수)
     *
     * @param brandCode 브랜드 코드
     * @return 조회된 Brand
     * @throws BrandNotFoundException Brand를 찾을 수 없는 경우
     */
    @Transactional(readOnly = true)
    public Brand findByCode(String brandCode) {
        BrandCode code = BrandCode.of(brandCode);
        return brandQueryPort
                .findByCode(code)
                .orElseThrow(() -> new BrandNotFoundException(brandCode));
    }

    /**
     * 활성화된 모든 브랜드 목록 조회
     *
     * @return 활성 Brand 목록
     */
    @Transactional(readOnly = true)
    public List<Brand> findAllActive() {
        return brandQueryPort.findAllActive();
    }

    /**
     * 검색 조건으로 브랜드 목록 조회
     *
     * @param criteria 검색 조건 (Domain Criteria)
     * @return Brand 목록
     */
    @Transactional(readOnly = true)
    public List<Brand> findByCriteria(BrandSearchCriteria criteria) {
        return brandQueryPort.findByCriteria(criteria);
    }

    /**
     * 검색 조건에 맞는 브랜드 총 개수 조회
     *
     * @param criteria 검색 조건 (Domain Criteria)
     * @return 브랜드 총 개수
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BrandSearchCriteria criteria) {
        return brandQueryPort.countByCriteria(criteria);
    }

    /**
     * Brand ID 존재 여부 확인
     *
     * @param brandId Brand ID (Long)
     * @return 존재하면 true
     */
    @Transactional(readOnly = true)
    public boolean existsById(Long brandId) {
        BrandId id = BrandId.of(brandId);
        return brandQueryPort.existsById(id);
    }

    /**
     * 활성 Brand 존재 여부 확인
     *
     * @param brandId Brand ID (Long)
     * @return 존재하면 true
     */
    @Transactional(readOnly = true)
    public boolean existsActiveById(Long brandId) {
        return brandQueryPort.existsActiveById(brandId);
    }
}
