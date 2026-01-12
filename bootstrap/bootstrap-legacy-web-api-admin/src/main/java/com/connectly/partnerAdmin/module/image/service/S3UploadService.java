package com.connectly.partnerAdmin.module.image.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.connectly.partnerAdmin.module.image.core.ImageContext;
import com.connectly.partnerAdmin.module.image.dto.PreSignedUrlRequest;
import com.connectly.partnerAdmin.module.image.dto.PreSignedUrlResponse;
import com.connectly.partnerAdmin.module.image.enums.ImagePath;
import com.connectly.partnerAdmin.module.image.enums.PathType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3UploadService implements ImageUploadService{

    private final ImageUploadProcessorProvider imageUploadProcessorProvider;
    private final S3Presigner presigner;

    @Value("${aws.tempBucket:temp-set-of.net}")
    private String tempBucket;

    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final Duration PRESIGNED_URL_DURATION = Duration.ofDays(7);
    private static final String SLASH = "/";
    private static final String DOT = ".";

    @Override
    public String uploadImage(ImageContext imageContext) {
        ImagePath imagePath = imageContext.getImagePath();
        PathType pathType = PathType.valueOf(imagePath.getName());

        ImageUploadProcessor processor = imageUploadProcessorProvider.findProcessor(pathType);
        return processor.uploadImage(null, imageContext.getImageUrl(), pathType);
    }

    @Override
    public PreSignedUrlResponse getPreSignedUrl(PreSignedUrlRequest request) {
        String fileName = request.getFileName();
        String objectKey = generateObjectKey(request.getImagePath(), fileName);
        String extension = getExtensionFromFileName(fileName);
        String contentType = getContentType(extension);

        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(tempBucket)
                .key(objectKey)
                .contentType(contentType)
                .build();

        PresignedPutObjectRequest presignedPutObjectRequest =
                presigner.presignPutObject(b ->
                        b.putObjectRequest(objectRequest)
                                .signatureDuration(PRESIGNED_URL_DURATION)
                );

        return PreSignedUrlResponse.builder()
                .preSignedUrl(presignedPutObjectRequest.url().toString())
                .objectKey(objectKey)
                .build();
    }

    @Override
    public void completeUpload(String sessionId, String etag) {
        // S3 직접 업로드 방식에서는 별도 완료 처리 불필요
        log.info("Upload complete - sessionId: {}, etag: {}", sessionId, etag);
    }

    private String generateObjectKey(ImagePath imagePath, String fileName) {
        String todayDate = LocalDate.now().format(DateTimeFormatter.ofPattern(DATE_FORMAT));
        String extension = getExtensionFromFileName(fileName);
        String cleanFileName = fileName.replaceAll("\\." + extension + "$", "");
        String baseKey = imagePath.getPath() + todayDate + SLASH +
                (cleanFileName.isEmpty() ? UUID.randomUUID().toString() : cleanFileName);
        return extension.isEmpty() ? baseKey : baseKey + DOT + extension;
    }

    private String getExtensionFromFileName(String fileName) {
        int lastDotIndex = fileName.lastIndexOf(DOT);
        if (lastDotIndex < 0) {
            return "";
        }
        return fileName.substring(lastDotIndex + 1);
    }

    private String getContentType(String extension) {
        return switch (extension.toLowerCase()) {
            case "jpeg", "jpg" -> "image/jpeg";
            case "png" -> "image/png";
            case "gif" -> "image/gif";
            case "bmp" -> "image/bmp";
            case "webp" -> "image/webp";
            default -> "application/octet-stream";
        };
    }

}
