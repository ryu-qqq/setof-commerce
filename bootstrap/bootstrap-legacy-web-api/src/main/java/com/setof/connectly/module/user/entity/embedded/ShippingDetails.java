package com.setof.connectly.module.user.entity.embedded;

import com.querydsl.core.annotations.QueryProjection;
import com.setof.connectly.module.product.enums.group.Origin;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class ShippingDetails {

    @Length(max = 10, message = "수취인 성함은 10자 이내 입니다.")
    @NotNull(message = "수취인 성함은 필수입니다")
    @Column(name = "RECEIVER_NAME")
    private String receiverName;

    @Length(max = 30, message = "배송지 명은  30자 이내 입니다.")
    @NotNull(message = "배송지 명은 필수입니다")
    @Column(name = "SHIPPING_ADDRESS_NAME")
    private String shippingAddressName;

    @Length(max = 100, message = "상세 주소는 100자 이내 입니다.")
    @NotNull(message = "상세 주소는 필수입니다")
    @Column(name = "ADDRESS_LINE1")
    private String addressLine1;

    @Length(max = 100, message = "상세 주소는 100자 이내 입니다.")
    @NotNull(message = "상세 주소는 필수입니다")
    @Column(name = "ADDRESS_LINE2")
    private String addressLine2;

    @Length(max = 10, message = "우편번호는 10자 이내 입니다.")
    @NotNull(message = "우편번호는 필수입니다")
    @Column(name = "ZIP_CODE")
    private String zipCode;

    @Column(name = "COUNTRY")
    @Enumerated(EnumType.STRING)
    private Origin country;

    @Length(max = 100, message = "배송 요청 사항은 150자 이내 입니다.")
    @Column(name = "DELIVERY_REQUEST")
    private String deliveryRequest;

    @Pattern(regexp = "010[0-9]{8}", message = "유효하지 않은 전화번호 형식입니다.")
    private String phoneNumber;

    @QueryProjection
    public ShippingDetails(
            String receiverName,
            String shippingAddressName,
            String addressLine1,
            String addressLine2,
            String zipCode,
            String deliveryRequest,
            String phoneNumber) {
        this.receiverName = receiverName;
        this.shippingAddressName = shippingAddressName;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.zipCode = zipCode;
        this.country = Origin.KR;
        this.deliveryRequest = deliveryRequest;
        this.phoneNumber = phoneNumber;
    }

    public Origin getCountry() {
        if (this.country == null) return Origin.KR;
        else return country;
    }
}
