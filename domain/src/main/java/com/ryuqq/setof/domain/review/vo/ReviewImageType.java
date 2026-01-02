package com.ryuqq.setof.domain.review.vo;

public enum ReviewImageType {
    PHOTO("사진"),
    VIDEO_THUMBNAIL("동영상 썸네일");

    private final String description;

    ReviewImageType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public boolean isPhoto() {
        return this == PHOTO;
    }

    public boolean isVideoThumbnail() {
        return this == VIDEO_THUMBNAIL;
    }
}
