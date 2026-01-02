package com.connectly.partnerAdmin.module.coreServer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.connectly.partnerAdmin.module.coreServer.request.product.ProductGroupContextCommandRequestDto;
import com.connectly.partnerAdmin.module.coreServer.response.ProductGroupInsertResponseDto;
import com.connectly.partnerAdmin.module.payload.ApiResponse;
import com.connectly.partnerAdmin.module.product.dto.ProductGroupFetchResponse;
import com.connectly.partnerAdmin.module.product.dto.query.CreateProductGroup;
import com.connectly.partnerAdmin.module.product.dto.query.UpdateProductGroup;
import com.connectly.partnerAdmin.module.product.service.group.ProductGroupFetchService;

@Slf4j
@Service
@RequiredArgsConstructor
public class CoreServerProductService {
    private final ProductGroupFetchService productGroupFetchService;
    private final CoreServerProductMapper coreServerProductMapper;
    private final ProductMapper productMapper;
    private final CoreServerClient coreServerClient;
    private final ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();


    public void register(long productGroupId){
        String traceIdHeader = MDC.get("traceId");

        CompletableFuture.runAsync(() -> {
            ProductGroupFetchResponse productGroupFetchResponse = productGroupFetchService.fetchProductGroup(productGroupId);

            CreateProductGroup productGroupContextCommandRequestDto = productMapper.toProductGroupContextCommandRequestDto(
                productGroupFetchResponse);

            try {
                ResponseEntity<ApiResponse<ProductGroupInsertResponseDto>> register = coreServerClient.register(
                    productGroupFetchResponse.getProductGroup().getExternalProductUuId(),
                    "36bddb74-043c-4fcd-a17d-d7089bf65b90",
                    traceIdHeader,
                    productGroupContextCommandRequestDto);

                if (!register.getStatusCode().is2xxSuccessful()) {
                    log.error("Failed to register product group To Core Server: {}", productGroupFetchResponse.getProductGroupId());
                }
            } catch (Exception e) {
                log.error("Failed to register product group To Core Server: {}", productGroupFetchResponse.getProductGroupId(), e);
            }
        }, executorService);

    }

    public void update(long productGroupId){
        String traceIdHeader = MDC.get("traceId");

        CompletableFuture.runAsync(() -> {
            ProductGroupFetchResponse productGroupFetchResponse = productGroupFetchService.fetchProductGroup(productGroupId);
            ProductGroupContextCommandRequestDto command = coreServerProductMapper.toCommand(productGroupFetchResponse);

            try {
                ResponseEntity<ApiResponse<Long>> apiResponseResponseEntity = coreServerClient.updateCore(
                    productGroupFetchResponse.getProductGroup().getExternalProductUuId(),
                    "36bddb74-043c-4fcd-a17d-d7089bf65b90",
                    productGroupId,
                    traceIdHeader,
                    command);

                if (!apiResponseResponseEntity.getStatusCode().is2xxSuccessful()) {
                    log.error("Failed to register product group To Core Server: {}", productGroupFetchResponse.getProductGroupId());
                }
            } catch (Exception e) {
                log.error("Failed to register product group To Core Server: {}", productGroupFetchResponse.getProductGroupId(), e);
            }
        }, executorService);

    }


    public void registerCore(long productGroupId){
        String traceIdHeader = MDC.get("traceId");

        ProductGroupFetchResponse productGroupFetchResponse = productGroupFetchService.fetchProductGroup(productGroupId);

        ProductGroupContextCommandRequestDto command = coreServerProductMapper.toCommand(productGroupFetchResponse);

        ResponseEntity<ApiResponse<ProductGroupInsertResponseDto>> register = coreServerClient.registerCoreProduct(
            productGroupFetchResponse.getProductGroup().getExternalProductUuId(),
            "36bddb74-043c-4fcd-a17d-d7089bf65b90",
            traceIdHeader,
            command);

        if (!register.getStatusCode().is2xxSuccessful()) {
            log.error("Failed to register product group To Core Server: {}", productGroupFetchResponse.getProductGroupId());
        }

    }




    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void update(long productGroupId, UpdateProductGroup updateProductGroup){
        String traceIdHeader = MDC.get("traceId");

        CompletableFuture.runAsync(() -> {

            try {
                ResponseEntity<Object> register = coreServerClient.update(
                    productGroupId,
                    "36bddb74-043c-4fcd-a17d-d7089bf65b90",
                    traceIdHeader,
                    updateProductGroup);

                if (!register.getStatusCode().is2xxSuccessful()) {
                    log.error("Failed to update product group To Core Server: {}", productGroupId);
                }
            } catch (Exception e) {
                log.error("Failed to update product group To Core Server: {}", productGroupId, e);
            }
        }, executorService);

    }

}
