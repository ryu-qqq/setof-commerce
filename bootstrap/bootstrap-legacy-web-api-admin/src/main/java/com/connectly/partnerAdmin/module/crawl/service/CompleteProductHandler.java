package com.connectly.partnerAdmin.module.crawl.service;

import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutorService;

import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.connectly.partnerAdmin.module.coreServer.CoreServerProductService;
import com.connectly.partnerAdmin.module.crawl.dto.request.CrawlProductGroupInsertRequestDto;
import com.connectly.partnerAdmin.module.crawl.entity.CrawlProduct;
import com.connectly.partnerAdmin.module.product.dto.query.CreateProductGroup;
import com.connectly.partnerAdmin.module.product.dto.query.CreateProductGroupResponse;
import com.connectly.partnerAdmin.module.product.dto.query.UpdateProductGroup;
import com.connectly.partnerAdmin.module.product.mapper.group.ProductGroupMapper;
import com.connectly.partnerAdmin.module.product.service.group.ProductGroupUpdateService;
import com.connectly.partnerAdmin.module.product.service.group.ProductRegistrationService;

@Slf4j
@Component
public class CompleteProductHandler  {

    private final CrawlProductFinder crawlProductFinder;
    private final CrawlProductRegister crawlProductRegister;
    private final CrawlProductConverter crawlProductConverter;
    private final ProductRegistrationService productRegistrationService;
    private final ProductGroupUpdateService productGroupUpdateService;
    private final ExecutorService executorService;
    private final CoreServerProductService coreServerProductService;
    private final ProductGroupMapper productGroupMapper;


    public CompleteProductHandler(CrawlProductFinder crawlProductFinder, CrawlProductRegister crawlProductRegister,
                                  CrawlProductConverter crawlProductConverter,
                                  ProductRegistrationService productRegistrationService,
                                  ProductGroupUpdateService productGroupUpdateService, ExecutorService executorService,
                                  CoreServerProductService coreServerProductService,
                                  ProductGroupMapper productGroupMapper) {
        this.crawlProductFinder = crawlProductFinder;
        this.crawlProductRegister = crawlProductRegister;
        this.crawlProductConverter = crawlProductConverter;
        this.productRegistrationService = productRegistrationService;
        this.productGroupUpdateService = productGroupUpdateService;
        this.executorService = executorService;
        this.coreServerProductService = coreServerProductService;
        this.productGroupMapper = productGroupMapper;
    }


    public long handle(CrawlProductGroupInsertRequestDto requestDto) {
        long crawlProductId = requestDto.getItemNo();
        boolean exist = crawlProductFinder.existById(crawlProductId);
        if (!exist) {
            CrawlProduct crawlProduct = toCrawlProduct(requestDto);

            CreateProductGroup createProductGroup = ProductGroupRequestDtoConverter.toRequestDto(requestDto,
                crawlProduct.getBrandId(), crawlProduct.getCategoryId());

            CreateProductGroupResponse createProductGroupResponse = productRegistrationService.registerProduct(
                UUID.randomUUID().toString(), createProductGroup);

            crawlProduct.sync(createProductGroupResponse.productGroupId());
            crawlProductRegister.register(crawlProduct);


            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    coreServerProductService.register(createProductGroupResponse.productGroupId());
                }
            });

            return createProductGroupResponse.productGroupId();
        }else{
            Optional<Long> productGroupIdById = crawlProductFinder.findProductGroupIdById(
                requestDto.getItemNo());

            productGroupIdById.ifPresentOrElse(productGroupId -> {

                UpdateProductGroup updateProductGroup = toUpdateProductGroup(requestDto);
                productGroupUpdateService.updateProductGroup(productGroupId, updateProductGroup);


                TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        coreServerProductService.update(productGroupId, updateProductGroup);
                    }
                });
            }, () -> {
                CrawlProduct crawlProduct = toCrawlProduct(requestDto);


                CreateProductGroup createProductGroup = ProductGroupRequestDtoConverter.toRequestDto(requestDto,
                    crawlProduct.getBrandId(), crawlProduct.getCategoryId());

                CreateProductGroupResponse createProductGroupResponse = productRegistrationService.registerProduct(
                    UUID.randomUUID().toString(), createProductGroup);


                crawlProduct.sync(createProductGroupResponse.productGroupId());
                crawlProductRegister.update(crawlProduct);

                TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        coreServerProductService.register(createProductGroupResponse.productGroupId());
                    }
                });

            });

            return crawlProductId;
        }
    }

    private CrawlProduct toCrawlProduct(CrawlProductGroupInsertRequestDto requestDto) {
        return crawlProductConverter.convert(requestDto);
    }

    public UpdateProductGroup toUpdateProductGroup(CrawlProductGroupInsertRequestDto requestDto){
        CrawlProduct crawlProduct = toCrawlProduct(requestDto);

        CreateProductGroup createProductGroup = ProductGroupRequestDtoConverter.toRequestDto(requestDto,
            crawlProduct.getBrandId(), crawlProduct.getCategoryId());

        return productGroupMapper.toUpdateProductGroup(createProductGroup);
    }

}