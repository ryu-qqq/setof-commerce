package com.ryuqq.setof.migration.image;

/**
 * 레거시 이미지 정보 DTO
 *
 * <p>product_group_image 또는 product_group_detail_description 테이블의 이미지 정보를 담습니다.
 *
 * @param id 이미지 ID (product_group_image_id 또는 product_group_id)
 * @param productGroupId 상품그룹 ID
 * @param imageType 이미지 타입 (MAIN, SUB, DETAIL 등)
 * @param imageUrl 현재 이미지 URL (CloudFront URL)
 * @param sourceType 소스 타입 (IMAGE: product_group_image, DESCRIPTION:
 *     product_group_detail_description)
 * @author development-team
 * @since 1.0.0
 */
public record LegacyImageDto(
        Long id, Long productGroupId, String imageType, String imageUrl, SourceType sourceType) {

    /** 이미지 소스 타입 */
    public enum SourceType {
        /** product_group_image 테이블 */
        IMAGE,
        /** product_group_detail_description 테이블 */
        DESCRIPTION
    }

    /** product_group_image 테이블용 생성자 */
    public static LegacyImageDto ofImage(
            Long imageId, Long productGroupId, String imageType, String imageUrl) {
        return new LegacyImageDto(imageId, productGroupId, imageType, imageUrl, SourceType.IMAGE);
    }

    /** product_group_detail_description 테이블용 생성자 */
    public static LegacyImageDto ofDescription(
            Long productGroupId, String imageType, String imageUrl) {
        return new LegacyImageDto(
                productGroupId, productGroupId, imageType, imageUrl, SourceType.DESCRIPTION);
    }

    /** CloudFront URL인지 확인 */
    public boolean isCloudFrontUrl() {
        return imageUrl != null && imageUrl.contains("d3fej89xf1vai5.cloudfront.net");
    }

    /**
     * CloudFront URL을 S3 직접 URL로 변환
     *
     * @param s3BucketName S3 버킷 이름
     * @param s3Region S3 리전
     * @return S3 직접 URL
     */
    public String toS3DirectUrl(String s3BucketName, String s3Region) {
        if (!isCloudFrontUrl()) {
            return imageUrl;
        }
        // https://d3fej89xf1vai5.cloudfront.net/path/to/image.jpg
        // → https://bucket-name.s3.region.amazonaws.com/path/to/image.jpg
        String path = imageUrl.replace("https://d3fej89xf1vai5.cloudfront.net", "");
        return String.format("https://%s.s3.%s.amazonaws.com%s", s3BucketName, s3Region, path);
    }
}
