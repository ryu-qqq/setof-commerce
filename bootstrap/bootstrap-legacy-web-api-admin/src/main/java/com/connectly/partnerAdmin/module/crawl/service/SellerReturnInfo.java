package com.connectly.partnerAdmin.module.crawl.service;

import java.util.Arrays;

import com.connectly.partnerAdmin.module.product.dto.query.CreateRefundNotice;
import com.connectly.partnerAdmin.module.product.enums.delivery.ReturnMethod;

public enum SellerReturnInfo {

    SELLER_BINO(4, new CreateRefundNotice(ReturnMethod.RETURN_CONSUMER, "SHIP145", 20000, "경북 포항시")),
    SELLER_CCAPSLUE(7, new CreateRefundNotice(ReturnMethod.RETURN_SELLER, "SHIP145", 10000, "경기 고양시")),
    SELLER_FIXEDONE(9, new CreateRefundNotice(ReturnMethod.RETURN_CONSUMER, "SHIP145", 20000, "경기 성남시")),
    SELLER_IGOM(10, new CreateRefundNotice(ReturnMethod.RETURN_CONSUMER, "SHIP04", 10000, "서울 영등포구")),
    SELLER_LIKEASTAR(12, new CreateRefundNotice(ReturnMethod.RETURN_SELLER, "SHIP06", 10000, "경기 고양시")),
    SELLER_RUNNERSELL(18, new CreateRefundNotice(ReturnMethod.RETURN_CONSUMER, "SHIP145", 16000, "경기 성남시")),
    SELLER_SUBIR(19, new CreateRefundNotice(ReturnMethod.RETURN_CONSUMER, "SHIP145", 10000, "서울 강서구")),
    SELLER_THEFACTOR(20, new CreateRefundNotice(ReturnMethod.RETURN_CONSUMER, "SHIP04", 20000, "서울 강남구")),
    SELLER_VIAITALIA(23, new CreateRefundNotice(ReturnMethod.RETURN_CONSUMER, "SHIP145", 8000, "울산 동구")),
    SELLER_WDROBE(25, new CreateRefundNotice(ReturnMethod.RETURN_CONSUMER, "SHIP04", 8000, "서울 강남구")),
    SELLER_WINUS(27, new CreateRefundNotice(ReturnMethod.RETURN_SELLER, "SHIP01", 20000, "경기 성남시"))
    ;

    private final long sellerId;
    private final CreateRefundNotice refundNoticeDto;

    SellerReturnInfo(int sellerId, CreateRefundNotice refundNoticeDto) {
        this.sellerId = sellerId;
        this.refundNoticeDto = refundNoticeDto;
    }

    public long getSellerId() {
        return sellerId;
    }

    public CreateRefundNotice getRefundNoticeDto() {
        return refundNoticeDto;
    }

    public static CreateRefundNotice getRefundNotice(long sellerId) {
        return Arrays.stream(values())
            .filter(entry -> entry.sellerId == sellerId)
            .findFirst()
            .map(SellerReturnInfo::getRefundNoticeDto)
            .orElseThrow(() -> new IllegalArgumentException("Refund information not found for seller_id: " + sellerId));
    }


}
