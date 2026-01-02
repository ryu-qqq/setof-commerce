package com.ryuqq.setof.application.carrier.port.out.query;

import com.ryuqq.setof.domain.carrier.aggregate.Carrier;
import com.ryuqq.setof.domain.carrier.vo.CarrierCode;
import com.ryuqq.setof.domain.carrier.vo.CarrierId;
import java.util.List;
import java.util.Optional;

/**
 * Carrier Query Port (Query)
 *
 * <p>Carrier Aggregate를 조회하는 읽기 전용 Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface CarrierQueryPort {

    /**
     * ID로 Carrier 단건 조회
     *
     * @param id Carrier ID (Value Object)
     * @return Carrier Domain (Optional)
     */
    Optional<Carrier> findById(CarrierId id);

    /**
     * 택배사 코드로 Carrier 단건 조회
     *
     * @param code 택배사 코드 (Value Object)
     * @return Carrier Domain (Optional)
     */
    Optional<Carrier> findByCode(CarrierCode code);

    /**
     * 활성 상태 택배사 전체 조회
     *
     * @return 활성 Carrier 목록 (displayOrder 순)
     */
    List<Carrier> findAllActive();

    /**
     * 전체 택배사 조회 (활성/비활성 포함)
     *
     * @return 전체 Carrier 목록 (displayOrder 순)
     */
    List<Carrier> findAll();

    /**
     * Carrier ID 존재 여부 확인
     *
     * @param id Carrier ID
     * @return 존재 여부
     */
    boolean existsById(CarrierId id);

    /**
     * 택배사 코드 존재 여부 확인
     *
     * <p>택배사 코드 중복 검증에 사용됩니다.
     *
     * @param code 택배사 코드 (Value Object)
     * @return 존재 여부 (true: 이미 등록됨, false: 등록되지 않음)
     */
    boolean existsByCode(CarrierCode code);
}
