package com.setof.connectly.module.product.aop;

import com.setof.connectly.module.display.dto.component.ComponentDetailResponse;
import com.setof.connectly.module.display.dto.component.SubComponent;
import com.setof.connectly.module.display.dto.content.ContentGroupResponse;
import com.setof.connectly.module.display.enums.component.BadgeType;
import com.setof.connectly.module.product.dto.ProductGroupResponse;
import com.setof.connectly.module.product.dto.ProductGroupThumbnail;
import com.setof.connectly.module.user.service.favorite.fetch.UserFavoriteFindService;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class FavoriteAop {

    private final UserFavoriteFindService userFavoriteFindService;

    @Pointcut(
            value =
                    "execution(*"
                        + " com.setof.connectly.module.product.service.group.fetch.ProductGroupFindService.fetchProductGroup(..))")
    private void isFavoriteProduct() {}

    @Pointcut(
            value =
                    "execution(*"
                        + " com.setof.connectly.module.display.service.content.ContentFindServiceImpl.fetchContent(..))")
    private void isFavoriteProductInComponent() {}

    @Around(value = "isFavoriteProduct()")
    public Object isFavoriteProduct(ProceedingJoinPoint pjp) throws Throwable {
        Object proceed = pjp.proceed();

        if (proceed instanceof ProductGroupResponse) {
            ProductGroupResponse productGroupResponse = (ProductGroupResponse) proceed;
            boolean favorite =
                    isFavorite(productGroupResponse.getProductGroup().getProductGroupId());
            if (favorite) productGroupResponse.setFavorite(true);
        }

        return proceed;
    }

    @Around(value = "isFavoriteProductInComponent()")
    public Object isFavoriteProductInComponent(ProceedingJoinPoint pjp) throws Throwable {
        Object proceed = pjp.proceed();

        if (proceed instanceof ContentGroupResponse) {
            ContentGroupResponse contentGroupResponse = (ContentGroupResponse) proceed;

            Set<Long> favorite = isFavorite(extractProductGroupIds(contentGroupResponse));

            if (!favorite.isEmpty()) {
                contentGroupResponse.getComponentDetails().stream()
                        .map(ComponentDetailResponse::getComponent)
                        .map(SubComponent::getProductGroupThumbnails)
                        .flatMap(List::stream)
                        .forEach(
                                thumbnail ->
                                        thumbnail.setFavorite(
                                                favorite.contains(thumbnail.getProductGroupId())));
            }
        }

        return proceed;
    }

    private Set<Long> isFavorite(Set<Long> productGroupIds) {
        List<Long> userFavoriteIds = userFavoriteFindService.fetchUserFavoriteIds();
        return new HashSet<>(userFavoriteIds)
                .stream().filter(productGroupIds::contains).collect(Collectors.toSet());
    }

    private boolean isFavorite(long productGroupId) {
        return userFavoriteFindService.hasUserFavoriteProduct(productGroupId);
    }

    private Set<Long> extractProductGroupIds(ContentGroupResponse contentGroupResponse) {
        return contentGroupResponse.getComponentDetails().stream()
                .filter(
                        componentDetailResponse ->
                                componentDetailResponse
                                        .getComponentType()
                                        .isProductRelatedContents())
                .filter(
                        componentDetailResponse ->
                                componentDetailResponse.getBadgeType().equals(BadgeType.LIKE))
                .map(ComponentDetailResponse::getComponent)
                .map(SubComponent::getProductGroupThumbnails)
                .flatMap(List::stream)
                .map(ProductGroupThumbnail::getProductGroupId)
                .collect(Collectors.toSet());
    }
}
