package com.connectly.partnerAdmin.module.product.exception;


import com.connectly.partnerAdmin.module.product.dto.ProductAlertDto;
import com.connectly.partnerAdmin.module.product.enums.ProductErrorCode;

public class NotEnoughStockException extends ProductException{

    public NotEnoughStockException(long productId) {
        super(ProductErrorCode.PRODUCT_GROUP_NOT_ENOUGH_STOCK.getCode(), ProductErrorCode.PRODUCT_GROUP_NOT_ENOUGH_STOCK.getHttpStatus(), String.valueOf(productId));
    }

    public NotEnoughStockException(ProductAlertDto product) {
        super(ProductErrorCode.PRODUCT_GROUP_NOT_ENOUGH_STOCK.getCode(),
                ProductErrorCode.PRODUCT_GROUP_NOT_ENOUGH_STOCK.getHttpStatus(),
                buildMessage(product)
        );
    }

    private static String buildMessage(ProductAlertDto product){
        return String.format("%s 해당 제품, %s 의 재고가 %d 남아 있습니다. 이용에 불편드려 죄송합니다.", product.getProductGroupName(), product.getOption(), product.getQuantity());
    }

}

