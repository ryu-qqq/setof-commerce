package com.connectly.partnerAdmin.module.image.aop;

import java.util.List;
import java.util.function.Consumer;

import org.apache.commons.lang3.tuple.Pair;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.connectly.partnerAdmin.module.display.dto.banner.CreateBannerItem;
import com.connectly.partnerAdmin.module.display.dto.component.DisplayProductGroupThumbnail;
import com.connectly.partnerAdmin.module.display.dto.component.SubComponent;
import com.connectly.partnerAdmin.module.display.dto.component.image.ImageComponentLink;
import com.connectly.partnerAdmin.module.display.dto.component.tab.TabDetail;
import com.connectly.partnerAdmin.module.display.dto.content.query.CreateContent;
import com.connectly.partnerAdmin.module.display.entity.component.ComponentTarget;
import com.connectly.partnerAdmin.module.display.enums.SortType;
import com.connectly.partnerAdmin.module.image.core.BaseImageContext;
import com.connectly.partnerAdmin.module.image.enums.ImagePath;
import com.connectly.partnerAdmin.module.image.service.ImageUploadService;

@Aspect
@Component

public class DisplayModuleImageUploadAspect extends ImageUploadAspect {

    public DisplayModuleImageUploadAspect(ImageUploadService imageUploadService) {
        super(imageUploadService);
    }

    @Pointcut(value = "execution(* com.connectly.partnerAdmin.module.display.service.content.ContentQueryServiceImpl.enrollContent(..)) && args(createContent)")
    private void contentImageUpload(CreateContent createContent) {}

    @Pointcut(value = "execution(* com.connectly.partnerAdmin.module.display.service.component.query.banner.BannerQueryServiceImpl.enrollBannerItems(..)) && args(createBannerItems)")
    private void bannerImageUpload(List<CreateBannerItem> createBannerItems) {}


    @Pointcut(value = "execution(* com.connectly.partnerAdmin.module.display.mapper.image.ImageComponentMapperImpl.toImageComponentItem(..)) && args(link)")
    private void imageComponentItemSingleParam(ImageComponentLink link) {}


    @Pointcut(value = "execution(* com.connectly.partnerAdmin.module.display.mapper.image.ImageComponentMapperImpl.toImageComponentItem(..)) && args(imageComponentId, imageComponentLinks)", argNames = "imageComponentId,imageComponentLinks")
    private void imageComponentItemMultipleParams(long imageComponentId, List<ImageComponentLink> imageComponentLinks) {}



    @Pointcut(value = "execution(* com.connectly.partnerAdmin.module.display.mapper.item.ComponentItemMapperImpl.toEntities(..)) && args(componentTarget, productGroupThumbnails)", argNames = "componentTarget, productGroupThumbnails")
    private void componentItemImageUpload(ComponentTarget componentTarget, List<DisplayProductGroupThumbnail> productGroupThumbnails) {}


    @Pointcut(value = "execution(* com.connectly.partnerAdmin.module.display.service.component.query.item.SortItemUpdateProcessorImpl.processUpdates(..)) && args(existingSubcomponent, newComponent, onAdd, onUpdate, onRemove)", argNames = "existingSubcomponent, newComponent, onAdd, onUpdate, onRemove")
    private void sortItemImageUpload(SubComponent existingSubcomponent, SubComponent newComponent,
                                     Consumer<Pair<SortType, DisplayProductGroupThumbnail>> onAdd,
                                     Consumer<DisplayProductGroupThumbnail> onUpdate,
                                     Consumer<Long> onRemove) {}


    @Pointcut(value = "execution(* com.connectly.partnerAdmin.module.display.service.component.query.item.SortItemUpdateProcessorImpl.processUpdates(..)) && args(existingTabDetail, newTabDetail, onAdd, onUpdate, onRemove)", argNames = "existingTabDetail, newTabDetail, onAdd, onUpdate, onRemove")
    private void sortTabItemImageUpload(TabDetail existingTabDetail, TabDetail newTabDetail,
                                        Consumer<Pair<SortType, DisplayProductGroupThumbnail>> onAdd,
                                        Consumer<DisplayProductGroupThumbnail> onUpdate,
                                        Consumer<Long> onRemove) {}



    @Around(value = "sortTabItemImageUpload(existingTabDetail, newTabDetail, onAdd, onUpdate, onRemove)", argNames = "pjp, existingTabDetail, newTabDetail, onAdd, onUpdate, onRemove")
    public Object uploadTabSortItemImage(ProceedingJoinPoint pjp, TabDetail existingTabDetail, TabDetail newTabDetail,
                                         Consumer<Pair<SortType, DisplayProductGroupThumbnail>> onAdd,
                                         Consumer<DisplayProductGroupThumbnail> onUpdate,
                                         Consumer<Long> onRemove) throws Throwable {
        newTabDetail.getSortItems()
                .forEach(sortItem -> uploadImageUrl(sortItem.getProductGroups()));
        return pjp.proceed();
    }


    @Around(value = "sortItemImageUpload(existingSubcomponent, newComponent, onAdd, onUpdate, onRemove)", argNames = "pjp, existingSubcomponent, newComponent, onAdd, onUpdate, onRemove")
    public Object uploadSortItemImage(ProceedingJoinPoint pjp, SubComponent existingSubcomponent, SubComponent newComponent,
                                      Consumer<Pair<SortType, DisplayProductGroupThumbnail>> onAdd,
                                      Consumer<DisplayProductGroupThumbnail> onUpdate,
                                      Consumer<Long> onRemove) throws Throwable {

        newComponent.getSortItems()
                .forEach(sortItem -> uploadImageUrl(sortItem.getProductGroups()));
        return pjp.proceed();
    }


    @Around(value = "componentItemImageUpload(componentTarget, productGroupThumbnails)", argNames = "pjp, componentTarget, productGroupThumbnails")
    public Object uploadComponentItemImage(ProceedingJoinPoint pjp, ComponentTarget componentTarget, List<DisplayProductGroupThumbnail> productGroupThumbnails) throws Throwable {
        uploadImageUrl(productGroupThumbnails);
        return pjp.proceed();
    }

    @Around(value = "contentImageUpload(createContent)", argNames = "pjp,createContent")
    public Object uploadContentImage(ProceedingJoinPoint pjp, CreateContent createContent) throws Throwable {
        if(StringUtils.hasText(createContent.getImageUrl())) {
            uploadImageUrl(createContent);
        }
        return pjp.proceed();
    }

    @Around(value = "bannerImageUpload(createBannerItems)", argNames = "pjp,createBannerItems")
    public Object uploadContentImage(ProceedingJoinPoint pjp, List<CreateBannerItem> createBannerItems) throws Throwable {
        createBannerItems.forEach(this::uploadImageUrl);
        return pjp.proceed();
    }


    @Around(value = "imageComponentItemSingleParam(link)", argNames = "pjp,link")
    public Object uploadImageComponentSingle(ProceedingJoinPoint pjp, ImageComponentLink link) throws Throwable {
        uploadImageUrl(link);
        return pjp.proceed();
    }

    @Around(value = "imageComponentItemMultipleParams(imageComponentId, imageComponentLinks)", argNames = "pjp,imageComponentId, imageComponentLinks")
    public Object uploadImageComponentMulti(ProceedingJoinPoint pjp, long imageComponentId, List<ImageComponentLink> imageComponentLinks) throws Throwable {
        imageComponentLinks.forEach(this::uploadImageUrl);
        return pjp.proceed();
    }




    private void uploadImageUrl(List<DisplayProductGroupThumbnail> productGroupThumbnails) {
        for(DisplayProductGroupThumbnail productGroupThumbnail : productGroupThumbnails) {
            BaseImageContext baseImageContext = new BaseImageContext(ImagePath.CONTENT, productGroupThumbnail.getProductImageUrl());
            String uploadedImageUrl = uploadImage(baseImageContext);
            productGroupThumbnail.setProductImageUrl(uploadedImageUrl);
        }
    }


    private void uploadImageUrl(ImageComponentLink imageComponentLink){
        if(StringUtils.hasText(imageComponentLink.getImageUrl())){
            BaseImageContext baseImageContext = new BaseImageContext(ImagePath.IMAGE_COMPONENT, imageComponentLink.getImageUrl());
            String uploadedImageUrl = uploadImage(baseImageContext);
            imageComponentLink.setImageUrl(uploadedImageUrl);
        }
    }


    private void uploadImageUrl(CreateContent createContent){
        if(StringUtils.hasText(createContent.getImageUrl())){
            BaseImageContext baseImageContext = new BaseImageContext(ImagePath.CONTENT, createContent.getImageUrl());
            String uploadedImageUrl = uploadImage(baseImageContext);
            createContent.setImageUrl(uploadedImageUrl);
        }
    }

    private void uploadImageUrl(CreateBannerItem createBannerItem){
        if(StringUtils.hasText(createBannerItem.getImageUrl())) {
            BaseImageContext baseImageContext = new BaseImageContext(ImagePath.IMAGE_COMPONENT, createBannerItem.getImageUrl());
            String uploadedImageUrl = uploadImage(baseImageContext);
            createBannerItem.setImageUrl(uploadedImageUrl);
        }
    }

}
