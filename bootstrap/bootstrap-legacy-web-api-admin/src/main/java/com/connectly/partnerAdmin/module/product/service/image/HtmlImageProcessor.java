package com.connectly.partnerAdmin.module.product.service.image;

import com.connectly.partnerAdmin.module.image.core.BaseImageContext;
import com.connectly.partnerAdmin.module.image.enums.ImagePath;
import com.connectly.partnerAdmin.module.image.service.ImageUploadService;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class HtmlImageProcessor {

    private final ImageUploadService imageUploadService;

    @Value("${aws.assetUrl}")
    private String assetUrl;

    /**
     * HTML에서 이미지를 처리합니다.
     * - 이미 S3에 있는 이미지는 그대로 유지
     * - 외부 URL 이미지는 S3에 업로드 후 URL 치환
     *
     * @param html 처리할 HTML
     * @return 처리된 HTML과 최종 이미지 URL 목록
     */
    public HtmlProcessResult processHtmlImages(String html) {
        if (html == null || html.isBlank()) {
            return new HtmlProcessResult(html, new HashSet<>());
        }

        // parseBodyFragment: body 래퍼 없이 HTML fragment로 파싱
        Document document = Jsoup.parseBodyFragment(html);
        Elements imgElements = document.select("img[src]");
        Set<String> finalImageUrls = new HashSet<>();

        for (Element img : imgElements) {
            String src = img.attr("src");
            if (src.isBlank()) {
                continue;
            }

            // 이미 S3에 업로드된 URL이면 그대로 유지
            if (isAlreadyInS3(src)) {
                finalImageUrls.add(src);
                continue;
            }

            // S3에 없는 외부 URL은 업로드
            String uploadedUrl = uploadImage(src);
            img.attr("src", uploadedUrl);
            finalImageUrls.add(uploadedUrl);
        }

        // body().html(): body 내부 HTML만 반환 (래퍼 태그 제외)
        String processedHtml = document.body().html();
        return new HtmlProcessResult(processedHtml, finalImageUrls);
    }

    public Set<String> extractImageUrls(String html) {
        if (html == null || html.isBlank()) {
            return new HashSet<>();
        }

        Document document = Jsoup.parse(html);
        Elements imgElements = document.select("img[src]");

        return imgElements.stream()
                .map(element -> element.attr("src"))
                .filter(src -> !src.isBlank())
                .collect(Collectors.toSet());
    }

    private boolean isAlreadyInS3(String url) {
        return url != null && url.contains(assetUrl);
    }

    private String uploadImage(String imageUrl) {
        BaseImageContext imageContext = new BaseImageContext(ImagePath.DESCRIPTION, imageUrl);
        return imageUploadService.uploadImage(imageContext);
    }

    public record HtmlProcessResult(String processedHtml, Set<String> imageUrls) {
    }
}
