package com.connectly.partnerAdmin.module.product.service.image;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.image.core.BaseImageContext;
import com.connectly.partnerAdmin.module.image.enums.ImagePath;
import com.connectly.partnerAdmin.module.image.service.ImageUploadService;
import com.connectly.partnerAdmin.module.product.dto.query.CreateProductImage;
import com.connectly.partnerAdmin.module.product.dto.query.UpdateProductDescription;
import com.connectly.partnerAdmin.module.product.entity.group.ProductGroup;
import com.connectly.partnerAdmin.module.product.entity.image.ProductGroupDetailDescription;
import com.connectly.partnerAdmin.module.product.entity.image.ProductGroupImage;
import com.connectly.partnerAdmin.module.product.entity.image.embedded.ImageDetail;
import com.connectly.partnerAdmin.module.product.enums.image.ProductGroupImageType;

@Transactional
@RequiredArgsConstructor
@Service
public class ProductImageUpdateService {

    private static final String EMPTY_BODY = "<html>\n" +
            " <head></head>\n" +
            " <body></body>\n" +
            "</html>";

    private final ImageUploadService imageUploadService;
    private final HtmlImageProcessor htmlImageProcessor;

    @Value("${aws.assetUrl}")
    private String assetUrl;

    @Transactional
    public void updateProductImages(ProductGroup productGroup, List<CreateProductImage> createProductImages) {
        Map<String, ProductGroupImage> existingByOriginUrl = buildExistingImageMap(productGroup);
        Set<String> requestedOriginUrls = extractOriginUrls(createProductImages);

        softDeleteRemovedImages(existingByOriginUrl, requestedOriginUrls);
        processImageRequests(productGroup, createProductImages, existingByOriginUrl);
    }

    public void updateProductDetailDescription(ProductGroup productGroup, UpdateProductDescription updateProductDescription) {
        String newHtml = updateProductDescription.getDetailDescription();

        if (newHtml == null || newHtml.equals(EMPTY_BODY)) {
            return;
        }

        ProductGroupDetailDescription detailDescription = productGroup.getDetailDescription();

        HtmlImageProcessor.HtmlProcessResult result = htmlImageProcessor.processHtmlImages(newHtml);

        detailDescription.setImageDetail(new ImageDetail(ProductGroupImageType.DESCRIPTION, result.processedHtml()));
        detailDescription.updateImageUrls(String.join(",", result.imageUrls()));
    }

    private Map<String, ProductGroupImage> buildExistingImageMap(ProductGroup productGroup) {
        return productGroup.getImages().stream()
                .filter(img -> img.getDeleteYn() == Yn.N)
                .collect(Collectors.toMap(
                        img -> {
                            String originUrl = img.getImageDetail().getOriginUrl();
                            return originUrl != null ? originUrl : img.getImageDetail().getImageUrl();
                        },
                        Function.identity(),
                        (existing, replacement) -> existing
                ));
    }

    private Set<String> extractOriginUrls(List<CreateProductImage> createProductImages) {
        return createProductImages.stream()
                .map(img -> img.getOriginUrl() != null ? img.getOriginUrl() : img.getImageUrl())
                .collect(Collectors.toSet());
    }

    private void softDeleteRemovedImages(Map<String, ProductGroupImage> existingByOriginUrl, Set<String> requestedOriginUrls) {
        existingByOriginUrl.forEach((originUrl, image) -> {
            if (!requestedOriginUrls.contains(originUrl)) {
                image.delete();
            }
        });
    }

    private void processImageRequests(ProductGroup productGroup, List<CreateProductImage> createProductImages,
                                      Map<String, ProductGroupImage> existingByOriginUrl) {
        for (int i = 0; i < createProductImages.size(); i++) {
            CreateProductImage request = createProductImages.get(i);
            String originUrl = request.getOriginUrl() != null ? request.getOriginUrl() : request.getImageUrl();
            ProductGroupImage existing = existingByOriginUrl.get(originUrl);

            if (existing != null) {
                existing.getImageDetail().updateOriginUrl(originUrl);
                existing.getImageDetail().updateDisplayOrder(i);
            } else {
                String uploadedUrl = uploadImageIfNeeded(request.getImageUrl());
                ProductGroupImage newImage = ProductGroupImage.builder()
                        .imageDetail(new ImageDetail(
                                request.getProductImageType(),
                                uploadedUrl,
                                originUrl,
                                i
                        ))
                        .build();
                productGroup.addImage(newImage);
            }
        }
    }

    private String uploadImageIfNeeded(String imageUrl) {
        if (imageUrl.contains(assetUrl)) {
            return imageUrl;
        }
        BaseImageContext imageContext = new BaseImageContext(ImagePath.PRODUCT, imageUrl);
        return imageUploadService.uploadImage(imageContext);
    }
}
