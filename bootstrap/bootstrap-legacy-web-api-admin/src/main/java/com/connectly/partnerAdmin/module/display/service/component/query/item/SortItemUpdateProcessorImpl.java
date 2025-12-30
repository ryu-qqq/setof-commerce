package com.connectly.partnerAdmin.module.display.service.component.query.item;

import com.connectly.partnerAdmin.module.display.dto.component.DisplayProductGroupThumbnail;
import com.connectly.partnerAdmin.module.display.dto.component.SortItem;
import com.connectly.partnerAdmin.module.display.dto.component.SubComponent;
import com.connectly.partnerAdmin.module.display.dto.component.tab.TabDetail;
import com.connectly.partnerAdmin.module.display.enums.SortType;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;



@Component
@RequiredArgsConstructor
public class SortItemUpdateProcessorImpl implements SortItemUpdateProcessor{

    @Override
    public void processUpdates(SubComponent existingSubcomponent, SubComponent newComponent,
                               Consumer<Pair<SortType, DisplayProductGroupThumbnail>> onAdd,
                               Consumer<Pair<SortType, DisplayProductGroupThumbnail>> onUpdate,
                               Consumer<Pair<SortType, DisplayProductGroupThumbnail>> onDelete) {
        processItemUpdates(
                () -> extractSortItemsWithSortTypeSubComponent(existingSubcomponent, onDelete),
                () -> extractSortItemsWithSortTypeSubComponent(newComponent, onDelete),
                onAdd,
                onUpdate,
                onDelete
        );
    }

    @Override
    public void processUpdates(TabDetail existingTabDetail, TabDetail newTabDetail,
                               Consumer<Pair<SortType, DisplayProductGroupThumbnail>> onAdd,
                               Consumer<Pair<SortType, DisplayProductGroupThumbnail>> onUpdate,
                               Consumer<Pair<SortType, DisplayProductGroupThumbnail>> onDelete) {
        processItemUpdates(
                () -> extractSortItemsWithSortTypeTab(existingTabDetail, onDelete),
                () -> extractSortItemsWithSortTypeTab(newTabDetail, onDelete),
                onAdd,
                onUpdate,
                onDelete
        );
    }

    private Stream<Pair<SortType, DisplayProductGroupThumbnail>> extractSortItemsWithSortTypeSubComponent(SubComponent subComponent, Consumer<Pair<SortType, DisplayProductGroupThumbnail>> onRemove) {
        return extractSortItemsWithSortType(subComponent.getSortItems(), onRemove);
    }

    private Stream<Pair<SortType, DisplayProductGroupThumbnail>> extractSortItemsWithSortTypeTab(TabDetail tabDetail, Consumer<Pair<SortType, DisplayProductGroupThumbnail>> onRemove) {
        return extractSortItemsWithSortType(tabDetail.getSortItems(), onRemove);
    }


    private Stream<Pair<SortType, DisplayProductGroupThumbnail>> extractSortItemsWithSortType(List<SortItem> sortItems, Consumer<Pair<SortType, DisplayProductGroupThumbnail>> onRemove) {
        Set<Long> fixedProductGroupIds = new HashSet<>();


        sortItems.stream()
                .filter(sortItem -> sortItem.getSortType() == SortType.FIXED)
                .flatMap(sortItem -> sortItem.getProductGroups().stream())
                .map(DisplayProductGroupThumbnail::getProductGroupId)
                .forEach(fixedProductGroupIds::add);


        return sortItems.stream()
                .flatMap(sortItem -> sortItem.getProductGroups().stream()
                        .filter(pg -> {
                            boolean isAutoAndDuplicated = sortItem.getSortType() == SortType.AUTO && fixedProductGroupIds.contains(pg.getProductGroupId());
                            if (isAutoAndDuplicated) {
                                onRemove.accept(new Pair<>() {
                                    @Override
                                    public SortType getLeft() {
                                        return sortItem.getSortType();
                                    }
                                    @Override
                                    public DisplayProductGroupThumbnail getRight() {
                                        return pg;
                                    }
                                    @Override
                                    public DisplayProductGroupThumbnail setValue(DisplayProductGroupThumbnail value) {
                                        return pg;
                                    }
                                });
                            }
                            return !isAutoAndDuplicated;
                        })
                        .map(productGroupThumbnail -> Pair.of(sortItem.getSortType(), productGroupThumbnail)));
    }





    private void processItemUpdates(
            Supplier<Stream<Pair<SortType, DisplayProductGroupThumbnail>>> existingItemsSupplier,
            Supplier<Stream<Pair<SortType, DisplayProductGroupThumbnail>>> newItemsSupplier,
            Consumer<Pair<SortType, DisplayProductGroupThumbnail>> onAdd,
            Consumer<Pair<SortType, DisplayProductGroupThumbnail>> onUpdate,
            Consumer<Pair<SortType, DisplayProductGroupThumbnail>> onDelete) {

        BinaryOperator<Pair<SortType, DisplayProductGroupThumbnail>> mergeFunction = (oldValue, newValue) -> newValue;

        Map<Long, Pair<SortType, DisplayProductGroupThumbnail>> existingProductsMap = existingItemsSupplier.get()
                .collect(Collectors.toMap(
                        pair -> pair.getRight().getProductGroupId(),
                        Function.identity(),
                        mergeFunction));

        Map<Long, Pair<SortType, DisplayProductGroupThumbnail>> newProductsMap = newItemsSupplier.get()
                .collect(Collectors.toMap(
                        pair -> pair.getRight().getProductGroupId(),
                        Function.identity(),
                        mergeFunction));


        Set<Long> removedProductIds = new HashSet<>(existingProductsMap.keySet());
        removedProductIds.removeAll(newProductsMap.keySet());
        removedProductIds.forEach(aLong -> onDelete.accept(existingProductsMap.get(aLong)));

        newProductsMap.forEach((productId, newProductPair) -> {
            if (!existingProductsMap.containsKey(productId)) {
                onAdd.accept(newProductPair);
            } else {
                Pair<SortType, DisplayProductGroupThumbnail> existingProductPair = existingProductsMap.get(productId);
                DisplayProductGroupThumbnail newProduct = newProductPair.getRight();
                DisplayProductGroupThumbnail existingProduct = existingProductPair.getRight();
                SortType newSortType = newProductPair.getLeft();
                SortType existingSortType = existingProductPair.getLeft();

                if (!newProduct.equals(existingProduct) || !newSortType.equals(existingSortType)) {
                    onUpdate.accept(newProductPair);
                }
            }
        });

    }

}
