package com.ryuqq.setof.domain.productgroup.vo;

/** 상품그룹 이미지 유형. */
public enum ImageType {

    /** 썸네일 이미지 */
    THUMBNAIL,

    /** 상세 이미지 */
    DETAIL;

    public boolean isThumbnail() {
        return this == THUMBNAIL;
    }

    public boolean isDetail() {
        return this == DETAIL;
    }
}
