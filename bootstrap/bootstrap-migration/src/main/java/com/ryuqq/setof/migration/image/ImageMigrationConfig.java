package com.ryuqq.setof.migration.image;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 이미지 마이그레이션 설정
 *
 * <p>application.yml의 migration.image.* 설정을 바인딩합니다.
 *
 * <p><strong>설정 예시:</strong>
 *
 * <pre>
 * migration:
 *   image:
 *     s3-bucket-name: your-bucket-name
 *     s3-region: ap-northeast-2
 *     batch-size: 100
 *     enabled: true
 * </pre>
 *
 * @author development-team
 * @since 1.0.0
 */
@Configuration
@ConfigurationProperties(prefix = "migration.image")
public class ImageMigrationConfig {

    /** 기존 S3 버킷 이름 */
    private String s3BucketName = "setof-legacy-images";

    /** S3 리전 */
    private String s3Region = "ap-northeast-2";

    /** 배치 처리 크기 (상품그룹 단위) */
    private int batchSize = 100;

    /** 마이그레이션 활성화 여부 */
    private boolean enabled = false;

    /** 기존 CloudFront 도메인 */
    private String oldCdnDomain = "https://d3fej89xf1vai5.cloudfront.net";

    /** 새 CDN 도메인 */
    private String newCdnDomain = "https://cdn.set-of.com";

    // Getters and Setters

    public String getS3BucketName() {
        return s3BucketName;
    }

    public void setS3BucketName(String s3BucketName) {
        this.s3BucketName = s3BucketName;
    }

    public String getS3Region() {
        return s3Region;
    }

    public void setS3Region(String s3Region) {
        this.s3Region = s3Region;
    }

    public int getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getOldCdnDomain() {
        return oldCdnDomain;
    }

    public void setOldCdnDomain(String oldCdnDomain) {
        this.oldCdnDomain = oldCdnDomain;
    }

    public String getNewCdnDomain() {
        return newCdnDomain;
    }

    public void setNewCdnDomain(String newCdnDomain) {
        this.newCdnDomain = newCdnDomain;
    }

    @Override
    public String toString() {
        return "ImageMigrationConfig{"
                + "s3BucketName='"
                + s3BucketName
                + '\''
                + ", s3Region='"
                + s3Region
                + '\''
                + ", batchSize="
                + batchSize
                + ", enabled="
                + enabled
                + ", oldCdnDomain='"
                + oldCdnDomain
                + '\''
                + ", newCdnDomain='"
                + newCdnDomain
                + '\''
                + '}';
    }
}
