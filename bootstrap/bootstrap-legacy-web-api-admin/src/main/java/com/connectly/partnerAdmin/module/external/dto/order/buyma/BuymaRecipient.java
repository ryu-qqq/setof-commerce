package com.connectly.partnerAdmin.module.external.dto.order.buyma;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BuymaRecipient {

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("first_name_kana")
    private String firstNameKana;

    @JsonProperty("last_name_kana")
    private String lastNameKana;

    @JsonProperty("first_name_kanji")
    private String firstNameKanji;

    @JsonProperty("last_name_kanji")
    private String lastNameKanji;

    @JsonProperty("country")
    private String country;

    @JsonProperty("country_kanji")
    private String countryKanji;

    @JsonProperty("zip_code")
    private String zipCode;

    @JsonProperty("address_1")
    private String address1;

    @JsonProperty("address_2")
    private String address2;

    @JsonProperty("address_3")
    private String address3;

    @JsonProperty("address_4")
    private String address4;

    @JsonProperty("address_kanji_1")
    private String addressKanji1;

    @JsonProperty("address_kanji_2")
    private String addressKanji2;

    @JsonProperty("address_kanji_3")
    private String addressKanji3;

    @JsonProperty("phone_number")
    private String phoneNumber;

    public String getReceiverName(){
        return firstName + " " + lastName;
    }


    public String getDeliveryAddress(){
        return address1 + " " + address2 + " " + address3 + " " + address4;
    }
}
