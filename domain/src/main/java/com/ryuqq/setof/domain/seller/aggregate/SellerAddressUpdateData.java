package com.ryuqq.setof.domain.seller.aggregate;

import com.ryuqq.setof.domain.common.vo.Address;
import com.ryuqq.setof.domain.seller.vo.AddressName;
import com.ryuqq.setof.domain.seller.vo.ContactInfo;

/**
 * 셀러 주소 수정 데이터 Value Object.
 *
 * <p>Aggregate의 update() 메서드에 전달할 수정 데이터를 묶는 역할을 합니다.
 *
 * <p>VO 객체들은 이미 null 검증이 완료된 객체이므로, UpdateData에서는 추가 검증을 하지 않습니다.
 */
public record SellerAddressUpdateData(
        AddressName addressName, Address address, ContactInfo contactInfo) {

    public static SellerAddressUpdateData of(
            AddressName addressName, Address address, ContactInfo contactInfo) {
        return new SellerAddressUpdateData(addressName, address, contactInfo);
    }
}
