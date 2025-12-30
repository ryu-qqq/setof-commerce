package com.connectly.partnerAdmin.module.display.service.component.query.item;

import com.connectly.partnerAdmin.module.display.dto.component.DisplayProductGroupThumbnail;
import com.connectly.partnerAdmin.module.display.dto.component.SubComponent;
import com.connectly.partnerAdmin.module.display.dto.component.tab.TabDetail;
import com.connectly.partnerAdmin.module.display.enums.SortType;
import org.apache.commons.lang3.tuple.Pair;

import java.util.function.Consumer;

public interface SortItemUpdateProcessor {

    void processUpdates(SubComponent existingSubcomponent, SubComponent newComponent,
                        Consumer<Pair<SortType, DisplayProductGroupThumbnail>> onAdd,
                        Consumer<Pair<SortType, DisplayProductGroupThumbnail>> onUpdate,
                        Consumer<Pair<SortType, DisplayProductGroupThumbnail>> deletedProducts);

    void processUpdates(TabDetail existingSubcomponent, TabDetail newComponent,
                        Consumer<Pair<SortType, DisplayProductGroupThumbnail>> onAdd,
                        Consumer<Pair<SortType, DisplayProductGroupThumbnail>> onUpdate,
                        Consumer<Pair<SortType, DisplayProductGroupThumbnail>> deletedProducts);
}
