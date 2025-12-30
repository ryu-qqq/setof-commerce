package com.connectly.partnerAdmin.module.external.service;

import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.connectly.partnerAdmin.module.common.dto.CustomPageable;
import com.connectly.partnerAdmin.module.external.CursorLastDomainIdManager;
import com.connectly.partnerAdmin.module.external.client.ProductHubClient;
import com.connectly.partnerAdmin.module.external.dto.ProductDeliveryRequestDto;
import com.connectly.partnerAdmin.module.external.dto.ProductGroupCommandContextRequestDto;
import com.connectly.partnerAdmin.module.external.dto.ProductGroupDetailDescriptionRequestDto;
import com.connectly.partnerAdmin.module.external.dto.ProductGroupImageRequestDto;
import com.connectly.partnerAdmin.module.external.dto.ProductGroupInsertRequestDto;
import com.connectly.partnerAdmin.module.external.dto.ProductGroupInsertResponseDto;
import com.connectly.partnerAdmin.module.external.dto.ProductInsertRequestDto;
import com.connectly.partnerAdmin.module.external.dto.ProductNoticeInsertRequestDto;
import com.connectly.partnerAdmin.module.external.dto.ProductOptionInsertRequestDto;
import com.connectly.partnerAdmin.module.payload.ApiResponse;
import com.connectly.partnerAdmin.module.product.dto.ProductFetchResponse;
import com.connectly.partnerAdmin.module.product.dto.ProductGroupDetailResponse;
import com.connectly.partnerAdmin.module.product.dto.ProductGroupFetchResponse;
import com.connectly.partnerAdmin.module.product.dto.image.ProductImageDto;
import com.connectly.partnerAdmin.module.product.dto.notice.ProductNoticeDto;
import com.connectly.partnerAdmin.module.product.dto.option.OptionDto;
import com.connectly.partnerAdmin.module.product.enums.group.ProductCondition;
import com.connectly.partnerAdmin.module.product.filter.ProductGroupFilter;
import com.connectly.partnerAdmin.module.product.service.group.ProductGroupFetchService;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductHubSyncService {

    private final ProductHubClient productHubClient;
    private final ProductGroupFetchService productGroupFetchService;
    private final CursorLastDomainIdManager cursorLastDomainIdManager;

    public void temp(long sellerId){
        boolean keep = true;
        while(keep){
            keep = syncProduct(sellerId);
        }
    }

    public boolean syncProduct(long sellerId){

        Long lastDomainId = cursorLastDomainIdManager.getLastDomainId(String.valueOf(sellerId));
        ProductGroupFilter filter = new ProductGroupFilter(lastDomainId, null, null, null, null, null, null, null, null, null, sellerId, Collections.emptySet());
        CustomPageable<ProductGroupDetailResponse> productGroupDetailResponses = productGroupFetchService.fetchProductGroups(filter, PageRequest.of(0, 100));

        productGroupDetailResponses.forEach(p -> {
            ProductGroupFetchResponse productGroupFetchResponse = productGroupFetchService.fetchProductGroup(p.getProductGroupId());
            ProductGroupCommandContextRequestDto productGroupCommandContextRequestDto = toProductGroupCommandContextRequestDto(productGroupFetchResponse);
            sync(p, productGroupCommandContextRequestDto);
        });

        if (!productGroupDetailResponses.isEmpty()) {
            Long newLastDomainId = productGroupDetailResponses.getLastDomainId();
            cursorLastDomainIdManager.saveLastDomainId(String.valueOf(sellerId), newLastDomainId);
            return true;
        } else {
            cursorLastDomainIdManager.saveLastDomainId(String.valueOf(sellerId), 0L);
            return false;
        }
    }

    public void sync(ProductGroupDetailResponse p, ProductGroupCommandContextRequestDto productGroupCommandContextRequestDto){
        try{
            ResponseEntity<ApiResponse<ProductGroupInsertResponseDto>> response = productHubClient.updateProductGroup(p.getProductGroupId(), productGroupCommandContextRequestDto);
            if(response.getStatusCode().is2xxSuccessful()){
                System.out.println("response = " + response.getBody().getData().productGroupId());
            }
        }catch (Exception e){
            System.out.println("e = " + e);
        }

    }

    public ProductGroupCommandContextRequestDto toProductGroupCommandContextRequestDto(ProductGroupFetchResponse productGroupFetchResponse){
        return new ProductGroupCommandContextRequestDto(
                toProductGroupInsertRequestDto(productGroupFetchResponse),
                toProductNoticeInsertRequestDto(productGroupFetchResponse.getProductNotices()),
                toProductDeliveryRequestDto(productGroupFetchResponse),
                toProductGroupImageRequestDto(productGroupFetchResponse.getProductGroupImages()),
                new ProductGroupDetailDescriptionRequestDto(productGroupFetchResponse.getDetailDescription()),
                toProductInsertRequestDto(productGroupFetchResponse.getProducts())
        );
    }

    public ProductGroupInsertRequestDto toProductGroupInsertRequestDto(ProductGroupFetchResponse productGroupDetailResponse){
        return new ProductGroupInsertRequestDto(
                productGroupDetailResponse.getProductGroupId(),
                productGroupDetailResponse.getProductGroup().getBrand().getBrandId(),
                productGroupDetailResponse.getProductGroup().getCategoryId(),
                productGroupDetailResponse.getSellerId(),
                productGroupDetailResponse.getProductGroup().getProductGroupName(),
                productGroupDetailResponse.getProductGroup().getClothesDetailInfo().getStyleCode(),
                ProductCondition.NEW,
                productGroupDetailResponse.getProductGroup().getManagementType(),
                productGroupDetailResponse.getProductGroup().getOptionType(),
                productGroupDetailResponse.getProductGroup().getPrice().getRegularPrice(),
                productGroupDetailResponse.getProductGroup().getPrice().getCurrentPrice(),
                productGroupDetailResponse.getProductGroup().getProductStatus().isSoldOut(),
                productGroupDetailResponse.getProductGroup().getProductStatus().getDisplayYn().isYes(),
                ""
        );
    }

    public ProductNoticeInsertRequestDto toProductNoticeInsertRequestDto(ProductNoticeDto productNoticeDto){
        return new ProductNoticeInsertRequestDto(
                productNoticeDto.getMaterial(),
                productNoticeDto.getColor(),
                productNoticeDto.getSize(),
                productNoticeDto.getMaker(),
                productNoticeDto.getOrigin(),
                productNoticeDto.getWashingMethod(),
                productNoticeDto.getAssuranceStandard(),
                productNoticeDto.getAsPhone(),
                productNoticeDto.getAsPhone()
        );
    }

    public ProductDeliveryRequestDto toProductDeliveryRequestDto(ProductGroupFetchResponse productGroupDetailResponse){

        return new ProductDeliveryRequestDto(
                productGroupDetailResponse.getProductGroup().getDeliveryNotice().getDeliveryArea(),
                BigDecimal.valueOf(productGroupDetailResponse.getProductGroup().getDeliveryNotice().getDeliveryFee()),
                productGroupDetailResponse.getProductGroup().getDeliveryNotice().getDeliveryPeriodAverage(),
                productGroupDetailResponse.getProductGroup().getRefundNotice().getReturnMethodDomestic(),
                productGroupDetailResponse.getProductGroup().getRefundNotice().getReturnCourierDomestic(),
                BigDecimal.valueOf(productGroupDetailResponse.getProductGroup().getRefundNotice().getReturnChargeDomestic()),
                productGroupDetailResponse.getProductGroup().getRefundNotice().getReturnExchangeAreaDomestic()
        );
    }

    public List<ProductGroupImageRequestDto> toProductGroupImageRequestDto(List<ProductImageDto> productGroupImages){
        return productGroupImages.stream()
                .map(i -> new ProductGroupImageRequestDto(
                        i.getType(),
                        i.getProductImageUrl()
                )).toList();
    }

    public List<ProductInsertRequestDto> toProductInsertRequestDto(Set<ProductFetchResponse> products){
        return products.stream()
                .map(p -> new ProductInsertRequestDto(
                        p.getProductStatus().isSoldOut(),
                        p.getProductStatus().getDisplayYn().isYes(),
                        p.getStockQuantity(),
                        p.getAdditionalPrice(),
                        toProductOptionInsertRequestDto(p.getOptions())
                ))
                .toList();
    }

    public List<ProductOptionInsertRequestDto> toProductOptionInsertRequestDto(Set<OptionDto> optionDtos){
        return optionDtos.stream()
                .map(o -> new ProductOptionInsertRequestDto(
                        o.getOptionName(),
                        o.getOptionValue()
                ))
                .toList();
    }

}
