package com.connectly.partnerAdmin.module.image.aop;

import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.connectly.partnerAdmin.module.image.core.BaseImageContext;
import com.connectly.partnerAdmin.module.image.enums.ImagePath;
import com.connectly.partnerAdmin.module.image.service.ImageUploadService;
import com.connectly.partnerAdmin.module.product.dto.query.CreateProductImage;

/**
 * 상품 이미지 업로드 AOP.
 *
 * Create 시 Mapper를 통한 이미지 생성에만 적용됩니다.
 * Update 시에는 ProductImageUpdateService에서 직접 이미지 업로드를 호출합니다.
 */
@Aspect
@Component
public class ProductModuleImageUploadAspect extends ImageUploadAspect {

    public ProductModuleImageUploadAspect(ImageUploadService imageUploadService) {
        super(imageUploadService);
    }

    @Pointcut("execution(* com.connectly.partnerAdmin.module.product.mapper.image.ProductGroupImageMapperImpl.toProductGroupImage(..)) && args(productImageList)")
    private void productGroupImageUploadWhenCreateProductGroup(List<CreateProductImage> productImageList) {}

    @Pointcut("execution(* com.connectly.partnerAdmin.module.product.mapper.image.ProductGroupDetailDescriptionMapperImpl.toProductDetailDescription(..)) && args(detailDescription)")
    private void productGroupDetailDescriptionUploadWhenCreateProductGroup(String detailDescription) {}

    @Around(value = "productGroupImageUploadWhenCreateProductGroup(productImageList)", argNames = "pjp, productImageList")
    public Object uploadProductGroupImages(ProceedingJoinPoint pjp, List<CreateProductImage> productImageList) throws Throwable {
        for (CreateProductImage createProductImage : productImageList) {
            BaseImageContext baseImageContext = new BaseImageContext(ImagePath.PRODUCT, createProductImage.getImageUrl());
            String uploadedImageUrl = uploadImage(baseImageContext);
            createProductImage.setImageUrl(uploadedImageUrl);
        }
        return pjp.proceed();
    }

    @Around(value = "productGroupDetailDescriptionUploadWhenCreateProductGroup(detailDescription)", argNames = "pjp, detailDescription")
    public Object uploadProductDetailDescription(ProceedingJoinPoint pjp, String detailDescription) throws Throwable {
        String immediateUploadUrl = uploadImage(detailDescription);
        Object[] args = pjp.getArgs();
        args[0] = immediateUploadUrl;
        return pjp.proceed(args);
    }
}
