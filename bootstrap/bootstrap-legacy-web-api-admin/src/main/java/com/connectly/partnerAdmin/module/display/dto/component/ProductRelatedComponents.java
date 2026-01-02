package com.connectly.partnerAdmin.module.display.dto.component;

import com.connectly.partnerAdmin.module.display.dto.query.ComponentItemQueryDto;
import com.connectly.partnerAdmin.module.display.dto.query.ComponentQuery;
import com.connectly.partnerAdmin.module.display.dto.query.brand.BrandComponentQueryDto;
import com.connectly.partnerAdmin.module.display.dto.query.category.CategoryComponentQueryDto;
import com.connectly.partnerAdmin.module.display.dto.query.product.ProductComponentQueryDto;
import com.connectly.partnerAdmin.module.display.dto.query.tab.TabComponentQueryDto;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductRelatedComponents {

    private long contentId;
    private Set<BrandComponentQueryDto> brandComponentQueries = new HashSet<>();
    private Set<CategoryComponentQueryDto> categoryComponentQueries= new HashSet<>();
    private Set<ProductComponentQueryDto> productComponentQueries= new HashSet<>();
    private Set<TabComponentQueryDto> tabComponentQueries= new HashSet<>();
    @Setter
    private Set<ComponentItemQueryDto> componentItemQueries= new HashSet<>();

    @QueryProjection
    public ProductRelatedComponents(long contentId, Set<BrandComponentQueryDto> brandComponentQueries, Set<CategoryComponentQueryDto> categoryComponentQueries, Set<ProductComponentQueryDto> productComponentQueries, Set<TabComponentQueryDto> tabComponentQueries) {
        this.contentId = contentId;
        this.brandComponentQueries = brandComponentQueries;
        this.categoryComponentQueries = categoryComponentQueries;
        this.productComponentQueries = productComponentQueries;
        this.tabComponentQueries = tabComponentQueries;
    }



    public List<ComponentQuery> getAllComponents() {
        List<ComponentQuery> allComponents = new ArrayList<>();
        allComponents.addAll(brandComponentQueries);
        allComponents.addAll(categoryComponentQueries);
        allComponents.addAll(productComponentQueries);
        allComponents.addAll(tabComponentQueries);
        return allComponents.stream()
                .filter(component -> component.getComponentId() != 0)
                .collect(Collectors.toList());
    }

}
