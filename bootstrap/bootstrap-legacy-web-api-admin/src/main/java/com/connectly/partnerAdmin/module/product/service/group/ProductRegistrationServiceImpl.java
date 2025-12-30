package com.connectly.partnerAdmin.module.product.service.group;

import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.connectly.partnerAdmin.module.product.dto.ProductFetchResponse;
import com.connectly.partnerAdmin.module.product.dto.query.CreateProductGroup;
import com.connectly.partnerAdmin.module.product.dto.query.CreateProductGroupResponse;
import com.connectly.partnerAdmin.module.product.dto.query.UpdateProductGroup;
import com.connectly.partnerAdmin.module.product.entity.group.ProductGroup;
import com.connectly.partnerAdmin.module.product.entity.option.OptionDetail;
import com.connectly.partnerAdmin.module.product.entity.option.OptionGroup;
import com.connectly.partnerAdmin.module.product.entity.option.ProductOption;
import com.connectly.partnerAdmin.module.product.entity.stock.Product;
import com.connectly.partnerAdmin.module.product.mapper.group.ProductGroupMapper;
import com.connectly.partnerAdmin.module.product.repository.group.ProductGroupRepository;
import com.connectly.partnerAdmin.module.product.repository.option.OptionDetailRepository;
import com.connectly.partnerAdmin.module.product.repository.option.OptionGroupRepository;
import com.connectly.partnerAdmin.module.product.repository.stock.ProductOptionRepository;
import com.connectly.partnerAdmin.module.product.service.sync.ExternalMallSyncService;

@Transactional
@RequiredArgsConstructor
@Service
public class ProductRegistrationServiceImpl implements ProductRegistrationService{
    private final ProductGroupUpdateService productGroupUpdateService;
    private final ProductGroupMapper productGroupMapper;
    private final ProductGroupRepository productGroupRepository;
    private final ProductOptionRepository productOptionRepository;
    private final OptionGroupRepository optionGroupRepository;
    private final OptionDetailRepository optionDetailRepository;
    private final ExternalMallSyncService externalMallSyncService;

    @Override
    public CreateProductGroupResponse registerProduct(String externalProductId, CreateProductGroup createProductGroup) {
        ProductGroup productGroup = productGroupMapper.toProductGroup(externalProductId, createProductGroup);
        ProductGroup savedProductGroup = productGroupRepository.save(productGroup);
        Set<Product> products = savedProductGroup.getProducts();

        saveOptions(products);

        Set<ProductFetchResponse> createProductResponses = savedProductGroup.getProducts().stream()
                .map(p -> new ProductFetchResponse(
                        savedProductGroup.getId(),
                        p.getId(),
                        p.getStockQuantity(),
                        p.getProductStatus(),
                        p.getOption(),
                        p.toOptions(),
                        BigDecimal.ZERO
                        ))
                .collect(Collectors.toSet());

        // 트랜잭션 커밋 후 외부몰 동기화 (비동기)
        Long productGroupId = savedProductGroup.getId();
        registerAfterCommitSync(productGroupId, true);

        return new CreateProductGroupResponse(
                savedProductGroup.getId(),
                savedProductGroup.getProductGroupDetails().getSellerId(),
                createProductResponses
        );
    }

    @Override
    public List<Long> registerProducts(String externalProductId, List<CreateProductGroup> createProductGroups) {
        List<Long> ids = new ArrayList<>();
        if(createProductGroups.size() > 100) {
            throw new IllegalArgumentException("Register up to 100 units. Current Request Size: " + createProductGroups.size());
        }

        createProductGroups.forEach(createProductGroup -> {
            if(createProductGroup.getProductGroupId() != null){
                ids.add(createProductGroup.getProductGroupId());
                UpdateProductGroup updateProductGroup = productGroupMapper.toUpdateProductGroup(createProductGroup);
                productGroupUpdateService.updateProductGroup(createProductGroup.getProductGroupId(), updateProductGroup);
            }
            ids.add(registerProduct(externalProductId, createProductGroup).productGroupId());
        });

        return ids;
    }


    private void saveOptions(Set<Product> products) {
        for (Product product : products) {
            Set<ProductOption> productOptions = product.getProductOptions();
            for (ProductOption productOption : productOptions) {
                OptionGroup optionGroup = productOption.getOptionGroup();

                if(optionGroup.getId() ==0) {
                    optionGroupRepository.save(optionGroup);
                }

                OptionDetail optionDetail = productOption.getOptionDetail();

                if(optionDetail.getId() ==0){
                    optionDetailRepository.save(optionDetail);
                }

                if(productOption.getId() ==0){
                    productOptionRepository.save(productOption);
                }
            }
        }
    }

    private void registerAfterCommitSync(Long productGroupId, boolean isCreate) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                if (isCreate) {
                    externalMallSyncService.syncProductCreated(productGroupId);
                } else {
                    externalMallSyncService.syncProductUpdated(productGroupId);
                }
            }
        });
    }

}
