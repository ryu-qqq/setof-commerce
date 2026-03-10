package com.ryuqq.setof.application.product.factory;

import com.ryuqq.setof.application.common.dto.command.StatusChangeContext;
import com.ryuqq.setof.application.common.time.TimeProvider;
import com.ryuqq.setof.application.product.dto.command.ProductDiffUpdateEntry;
import com.ryuqq.setof.application.product.dto.command.RegisterProductsCommand;
import com.ryuqq.setof.application.product.dto.command.SelectedOption;
import com.ryuqq.setof.application.product.dto.command.UpdateProductPriceCommand;
import com.ryuqq.setof.application.product.dto.command.UpdateProductStockCommand;
import com.ryuqq.setof.application.product.dto.command.UpdateProductsCommand;
import com.ryuqq.setof.application.selleroption.dto.command.UpdateSellerOptionGroupsCommand;
import com.ryuqq.setof.domain.common.vo.Money;
import com.ryuqq.setof.domain.product.id.ProductId;
import com.ryuqq.setof.domain.product.vo.ProductCreationData;
import com.ryuqq.setof.domain.product.vo.ProductUpdateData;
import com.ryuqq.setof.domain.product.vo.SkuCode;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import com.ryuqq.setof.domain.productgroup.id.SellerOptionValueId;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class ProductCommandFactory {

    private final TimeProvider timeProvider;

    public ProductCommandFactory(TimeProvider timeProvider) {
        this.timeProvider = timeProvider;
    }

    public StatusChangeContext<ProductId> createPriceUpdateContext(
            UpdateProductPriceCommand command) {
        return new StatusChangeContext<>(ProductId.of(command.productId()), timeProvider.now());
    }

    public StatusChangeContext<ProductId> createStockUpdateContext(
            UpdateProductStockCommand command) {
        return new StatusChangeContext<>(ProductId.of(command.productId()), timeProvider.now());
    }

    public List<ProductCreationData> toCreationDataList(
            List<RegisterProductsCommand.ProductData> products,
            List<UpdateSellerOptionGroupsCommand.OptionGroupCommand> optionGroups,
            List<SellerOptionValueId> allOptionValueIds) {
        Map<String, Map<String, SellerOptionValueId>> nameMap =
                buildOptionNameMap(optionGroups, allOptionValueIds);

        return products.stream()
                .map(
                        entry -> {
                            List<SellerOptionValueId> resolvedIds =
                                    resolveOptionIds(entry.selectedOptions(), nameMap);
                            return new ProductCreationData(
                                    SkuCode.of(entry.skuCode()),
                                    Money.of(entry.regularPrice()),
                                    Money.of(entry.currentPrice()),
                                    entry.stockQuantity(),
                                    entry.sortOrder(),
                                    resolvedIds);
                        })
                .toList();
    }

    public ProductUpdateData toUpdateData(
            ProductGroupId pgId,
            List<ProductDiffUpdateEntry> entries,
            List<UpdateSellerOptionGroupsCommand.OptionGroupCommand> optionGroups,
            List<SellerOptionValueId> resolvedActiveValueIds,
            Instant updatedAt) {
        Map<String, Map<String, SellerOptionValueId>> nameMap =
                buildOptionNameMap(optionGroups, resolvedActiveValueIds);

        List<ProductUpdateData.Entry> domainEntries =
                entries.stream()
                        .map(
                                entry -> {
                                    List<SellerOptionValueId> resolvedIds =
                                            entry.productId() == null
                                                    ? resolveOptionIds(
                                                            entry.selectedOptions(), nameMap)
                                                    : List.of();
                                    SkuCode skuCode =
                                            entry.skuCode() != null && !entry.skuCode().isBlank()
                                                    ? SkuCode.of(entry.skuCode())
                                                    : null;
                                    return new ProductUpdateData.Entry(
                                            entry.productId(),
                                            skuCode,
                                            Money.of(entry.regularPrice()),
                                            Money.of(entry.currentPrice()),
                                            entry.stockQuantity(),
                                            entry.sortOrder(),
                                            resolvedIds);
                                })
                        .toList();

        return new ProductUpdateData(pgId, domainEntries, updatedAt);
    }

    public UpdateSellerOptionGroupsCommand toOptionCommand(UpdateProductsCommand command) {
        List<UpdateSellerOptionGroupsCommand.OptionGroupCommand> groups =
                command.optionGroups().stream()
                        .map(
                                g ->
                                        new UpdateSellerOptionGroupsCommand.OptionGroupCommand(
                                                g.sellerOptionGroupId(),
                                                g.optionGroupName(),
                                                g.sortOrder(),
                                                g.optionValues().stream()
                                                        .map(
                                                                v ->
                                                                        new UpdateSellerOptionGroupsCommand
                                                                                .OptionValueCommand(
                                                                                v
                                                                                        .sellerOptionValueId(),
                                                                                v.optionValueName(),
                                                                                v.sortOrder()))
                                                        .toList()))
                        .toList();
        return new UpdateSellerOptionGroupsCommand(command.productGroupId(), groups);
    }

    public List<ProductDiffUpdateEntry> toEntries(
            List<UpdateProductsCommand.ProductData> products) {
        return products.stream()
                .map(
                        p ->
                                new ProductDiffUpdateEntry(
                                        p.productId(),
                                        p.skuCode(),
                                        p.regularPrice(),
                                        p.currentPrice(),
                                        p.stockQuantity(),
                                        p.sortOrder(),
                                        p.selectedOptions()))
                .toList();
    }

    private Map<String, Map<String, SellerOptionValueId>> buildOptionNameMap(
            List<UpdateSellerOptionGroupsCommand.OptionGroupCommand> optionGroups,
            List<SellerOptionValueId> allValueIds) {
        Map<String, Map<String, SellerOptionValueId>> nameMap = new LinkedHashMap<>();
        int index = 0;
        for (UpdateSellerOptionGroupsCommand.OptionGroupCommand group : optionGroups) {
            Map<String, SellerOptionValueId> valueMap = new LinkedHashMap<>();
            for (UpdateSellerOptionGroupsCommand.OptionValueCommand value : group.optionValues()) {
                valueMap.put(value.optionValueName(), allValueIds.get(index++));
            }
            nameMap.put(group.optionGroupName(), valueMap);
        }
        return nameMap;
    }

    private List<SellerOptionValueId> resolveOptionIds(
            List<SelectedOption> selectedOptions,
            Map<String, Map<String, SellerOptionValueId>> optionNameMap) {
        return selectedOptions.stream()
                .map(
                        so -> {
                            Map<String, SellerOptionValueId> valueMap =
                                    optionNameMap.get(so.optionGroupName());
                            if (valueMap == null) {
                                throw new IllegalArgumentException(
                                        "존재하지 않는 옵션 그룹: " + so.optionGroupName());
                            }
                            SellerOptionValueId valueId = valueMap.get(so.optionValueName());
                            if (valueId == null) {
                                throw new IllegalArgumentException(
                                        "존재하지 않는 옵션 값: "
                                                + so.optionGroupName()
                                                + " > "
                                                + so.optionValueName());
                            }
                            return valueId;
                        })
                .toList();
    }
}
