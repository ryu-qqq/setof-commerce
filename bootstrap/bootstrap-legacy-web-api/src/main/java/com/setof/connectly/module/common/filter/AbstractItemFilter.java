package com.setof.connectly.module.common.filter;

import com.setof.connectly.module.display.enums.component.OrderType;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.util.StringUtils;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@AllArgsConstructor
public abstract class AbstractItemFilter implements ItemFilter {

    private Long lastDomainId;
    private String cursorValue;
    private Long lowestPrice;
    private Long highestPrice;
    private Long categoryId;
    private Long brandId;
    private Long sellerId;
    private List<Long> categoryIds;
    private List<Long> brandIds;
    public OrderType orderType;

    @Override
    public void setBrandIds(List<Long> brandIds) {
        this.brandIds = brandIds;
    }

    @Override
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public void setCategoryIds(List<Long> categoryIds) {
        this.categoryIds = categoryIds;
    }

    @Override
    public void setLastDomainId(Long lastDomainId) {
        this.lastDomainId = lastDomainId;
    }

    @Override
    public void setCursorValue(String cursorValue) {
        this.cursorValue = cursorValue;
    }

    @Override
    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    @Override
    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    protected boolean hasExistLastIdAndCursorValue() {
        return StringUtils.hasText(cursorValue) && lastDomainId != null;
    }
}
