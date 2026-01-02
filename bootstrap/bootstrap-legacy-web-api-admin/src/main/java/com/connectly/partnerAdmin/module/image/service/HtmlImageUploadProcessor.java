package com.connectly.partnerAdmin.module.image.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import com.connectly.partnerAdmin.module.image.enums.PathType;

@Component
public class HtmlImageUploadProcessor extends AbstractImageUploadProcessor {

    protected HtmlImageUploadProcessor(SqsMessageSender sqsMessageSender) {
        super(sqsMessageSender);
    }

    @Override
    public String uploadImage(Long id, String imageUrl, PathType pathType) {
        return processHtmlUpload(id, imageUrl, pathType);
    }

    public String processHtmlUpload(Long id, String html, PathType pathType) {
        // parseBodyFragment: body 래퍼 없이 HTML fragment로 파싱
        Document document = Jsoup.parseBodyFragment(html);
        Elements imgElements = document.select("img[src]");

        for (Element imgElement : imgElements) {
            String originUrl = imgElement.attr("src");
            if (!originUrl.startsWith("http") && !originUrl.startsWith("https")) {
                continue;
            }

            if(!originUrl.contains(assetUrl)) {
                String fileExtension = ImageFileTypeUtils.getFileExtension(originUrl);
                String imageUrl = ImageFileNameGenerator.generate(id, assetUrl, pathType, fileExtension);
                trigger(imageUrl, originUrl, ImageFileTypeUtils.getImageContentType(imageUrl));
                imgElement.attr("src", imageUrl);
            }
        }

        // body().html(): body 내부 HTML만 반환 (래퍼 태그 제외)
        return document.body().html();
    }



}
