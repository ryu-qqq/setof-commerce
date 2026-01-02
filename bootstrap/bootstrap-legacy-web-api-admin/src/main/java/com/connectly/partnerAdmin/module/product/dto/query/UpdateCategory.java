package com.connectly.partnerAdmin.module.product.dto.query;


import com.connectly.partnerAdmin.module.product.annotation.CategoryValidate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@CategoryValidate
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UpdateCategory {

    private long categoryId;

}
