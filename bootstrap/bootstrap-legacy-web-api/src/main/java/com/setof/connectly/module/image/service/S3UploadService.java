package com.setof.connectly.module.image.service;

import com.setof.connectly.module.image.dto.PreSignedUrlRequest;
import com.setof.connectly.module.image.dto.PreSignedUrlResponse;
import com.setof.connectly.module.image.enums.ImagePath;
import com.setof.connectly.module.utils.FileUtils;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CopyObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3UploadService implements ImageUploadService {

    private final S3Client s3Client;
    private final S3Presigner presigner;

    @Value("${aws.assetUrl}")
    private String assetUrl;

    @Value("${aws.bucket}")
    private String bucket;

    @Value("${aws.tempUrl}")
    private String tempUrl;

    @Value("${aws.tempBucket}")
    private String tempBucket;

    private static final String DEFAULT_IMAGE_URL =
            "https://d3fej89xf1vai5.cloudfront.net/logo/setof-logo.png";
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final Duration PRESIGNED_URL_DURATION = Duration.ofDays(7);

    private static final String HTTPS = "https://";
    private static final String HTTP = "http://";
    private static final String SLASH = "/";
    private static final String DOT = ".";
    private static final String DIMS = "/_dims_/";

    private static final String SPACE = "%20";

    private CompletableFuture<String> uploadToS3(
            String objectKey, byte[] imageBytes, String contentType) {
        PutObjectRequest putObjectRequest =
                PutObjectRequest.builder()
                        .bucket(bucket)
                        .key(objectKey)
                        .contentType(contentType)
                        .build();

        try {
            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(imageBytes));
            return CompletableFuture.completedFuture(HTTPS + assetUrl + SLASH + objectKey);
        } catch (S3Exception e) {
            log.error("Failed to upload image to S3: {}", e.getMessage());
            return CompletableFuture.completedFuture(DEFAULT_IMAGE_URL);
        }
    }

    @Async
    @Override
    public CompletableFuture<String> uploadImage(ImagePath imagePath, String originalImageUrl) {

        String extension = getExtensionFromUrl(originalImageUrl);
        String fileName = UUID.randomUUID() + (extension.isEmpty() ? "" : DOT + extension);

        if (originalImageUrl.contains(assetUrl))
            return CompletableFuture.completedFuture(originalImageUrl);
        if (originalImageUrl.contains(tempUrl))
            return moveImageFromTempToTargetBucket(originalImageUrl, imagePath, fileName);

        String objectKey = generateObjectKey(imagePath, "", fileName);
        String decodedUrl = originalImageUrl.replaceAll(" ", SPACE);
        String contentType = FileUtils.getContentType(imagePath);

        try (InputStream imageStream = new URL(decodedUrl).openStream()) {
            byte[] imageBytes = IOUtils.toByteArray(imageStream);
            return uploadToS3(objectKey, imageBytes, contentType);
        } catch (IOException e) {
            log.error("Failed to upload image: {}", e.getMessage());
            return CompletableFuture.completedFuture(DEFAULT_IMAGE_URL);
        }
    }

    @Async
    public CompletableFuture<String> moveImageFromTempToTargetBucket(
            String originalImageUrl, ImagePath imagePath, String fileName) {
        return CompletableFuture.supplyAsync(
                () -> {
                    try {
                        URL url = new URL(originalImageUrl);

                        String sourceObjectKey = url.getPath().substring(1);

                        if (sourceObjectKey.isEmpty()) {
                            throw new IllegalStateException("Source object key cannot be empty");
                        }

                        String destinationObjectKey = generateObjectKey(imagePath, "", fileName);

                        copyImage(tempBucket, bucket, sourceObjectKey, destinationObjectKey).join();
                        // 복사가 완료되면 원본 이미지를 삭제합니다.
                        deleteImageFromBucket(tempBucket, sourceObjectKey);

                        // 새로운 버킷에 있는 이미지의 URL을 반환합니다.
                        return getFileURL(destinationObjectKey);
                    } catch (MalformedURLException e) {
                        throw new RuntimeException("Failed to move image", e);
                    }
                });
    }

    @Override
    public PreSignedUrlResponse getPreSignedUrl(PreSignedUrlRequest preSignedUrlRequest) {
        String fileName = preSignedUrlRequest.getFileName();

        String objectKey = generateObjectKey(preSignedUrlRequest.getImagePath(), "", fileName);

        String extension = getExtensionFromFileName(fileName);

        String contentType = FileUtils.getContentType(extension);

        PutObjectRequest objectRequest =
                PutObjectRequest.builder()
                        .bucket(tempBucket)
                        .key(objectKey)
                        .contentType(contentType)
                        .build();

        PresignedPutObjectRequest presignedPutObjectRequest =
                presigner.presignPutObject(
                        b ->
                                b.putObjectRequest(objectRequest)
                                        .signatureDuration(PRESIGNED_URL_DURATION));

        return PreSignedUrlResponse.builder()
                .preSignedUrl(presignedPutObjectRequest.url().toString())
                .objectKey(objectKey)
                .build();
    }

    private String getExtensionFromUrl(String url) {
        try {
            if (!url.startsWith(HTTP) && !url.startsWith(HTTPS)) {
                url = HTTPS + url;
            }
            String fileName = new URL(url).getPath();
            return getExtensionFromFileName(fileName);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid URL format", e);
        }
    }

    private String formatDate(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern(DATE_FORMAT));
    }

    private String getExtensionFromFileName(String fileName) {
        int dimsIndex = fileName.indexOf(DIMS);
        if (dimsIndex > 0) {
            fileName = fileName.substring(0, dimsIndex);
        }
        int lastDotIndex = fileName.lastIndexOf(DOT);
        if (lastDotIndex < 0) {
            return "";
        }
        return fileName.substring(lastDotIndex + 1);
    }

    private String generateObjectKey(ImagePath imagePath, String tempKey, String fileName) {
        String todayDate = formatDate(LocalDate.now());
        String extension = getExtensionFromFileName(fileName.isEmpty() ? tempKey : fileName);
        String cleanFileName = fileName.replaceAll("\\." + extension + "$", "");
        String baseKey =
                imagePath.getPath()
                        + todayDate
                        + SLASH
                        + (cleanFileName.isEmpty() ? UUID.randomUUID().toString() : cleanFileName);
        return extension.isEmpty() ? baseKey : baseKey + DOT + extension;
    }

    @Async
    public CompletableFuture<Void> copyImage(
            String sourceBucketName,
            String destinationBucketName,
            String sourceObjectKey,
            String destinationObjectKey) {
        return CompletableFuture.runAsync(
                () -> {
                    try {

                        CopyObjectRequest copyReq =
                                CopyObjectRequest.builder()
                                        .copySource(sourceObjectKey)
                                        .destinationBucket(destinationBucketName)
                                        .destinationKey(destinationObjectKey)
                                        .build();

                        s3Client.copyObject(copyReq);

                    } catch (Exception e) {
                        log.error(
                                "Error copying image from bucket {} to bucket {}, image name {}:"
                                        + " {}",
                                sourceBucketName,
                                destinationBucketName,
                                sourceObjectKey,
                                e.getMessage());
                        throw new RuntimeException("Failed to copy image in S3", e);
                    }
                });
    }

    public CompletableFuture<Void> deleteImageFromBucket(String bucketName, String objectKey) {
        return CompletableFuture.runAsync(
                () -> {
                    try {
                        DeleteObjectRequest deleteReq =
                                DeleteObjectRequest.builder()
                                        .bucket(bucketName)
                                        .key(objectKey)
                                        .build();

                        s3Client.deleteObject(deleteReq);
                    } catch (Exception e) {
                        log.error(
                                "Error deleting image from bucket {}: {}",
                                bucketName,
                                e.getMessage());
                        throw new RuntimeException("Failed to delete image from S3", e);
                    }
                });
    }

    private String getFileURL(String objectKey) {
        return HTTPS + assetUrl + SLASH + objectKey;
    }
}
