package com.ryuqq.setof.domain.carrier.exception;

import com.ryuqq.setof.domain.carrier.vo.CarrierCode;
import com.ryuqq.setof.domain.carrier.vo.CarrierId;
import com.ryuqq.setof.domain.common.exception.DomainException;

/**
 * 택배사를 찾을 수 없는 경우 예외
 *
 * <p>주어진 ID 또는 코드에 해당하는 택배사가 존재하지 않는 경우 발생합니다.
 */
public class CarrierNotFoundException extends DomainException {

    public CarrierNotFoundException(CarrierId carrierId) {
        super(CarrierErrorCode.CARRIER_NOT_FOUND, "택배사 ID: " + carrierId.value());
    }

    public CarrierNotFoundException(CarrierCode carrierCode) {
        super(CarrierErrorCode.CARRIER_NOT_FOUND, "택배사 코드: " + carrierCode.value());
    }

    public CarrierNotFoundException(Long carrierId) {
        super(CarrierErrorCode.CARRIER_NOT_FOUND, "택배사 ID: " + carrierId);
    }

    public CarrierNotFoundException(String carrierCode) {
        super(CarrierErrorCode.CARRIER_NOT_FOUND, "택배사 코드: " + carrierCode);
    }
}
