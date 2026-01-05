package com.setof.connectly.module.search.dto;

import com.setof.connectly.module.common.filter.AbstractItemFilter;
import com.setof.connectly.module.display.enums.component.OrderType;
import java.util.List;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchFilter extends AbstractItemFilter {
    private String searchWord;
    private Long productGroupId;

    @Override
    public Long getLastDomainId() {
        return super.getLastDomainId();
    }

    @Override
    public String getCursorValue() {
        return super.getCursorValue();
    }

    @Override
    public Long getLowestPrice() {
        return super.getLowestPrice();
    }

    @Override
    public Long getHighestPrice() {
        return super.getHighestPrice();
    }

    @Override
    public Long getCategoryId() {
        return super.getCategoryId();
    }

    @Override
    public Long getBrandId() {
        return super.getBrandId();
    }

    @Override
    public Long getSellerId() {
        return super.getSellerId();
    }

    @Override
    public List<Long> getCategoryIds() {
        return super.getCategoryIds();
    }

    @Override
    public List<Long> getBrandIds() {
        return super.getBrandIds();
    }

    @Override
    public OrderType getOrderType() {
        return super.getOrderType();
    }

    @Override
    public void setOrderType(OrderType orderType) {
        super.setOrderType(orderType);
    }

    @Override
    public void setBrandIds(List<Long> brandIds) {
        super.setBrandIds(brandIds);
    }

    @Override
    public void setCategoryId(Long categoryId) {
        super.setCategoryId(categoryId);
    }

    @Override
    public void setCategoryIds(List<Long> categoryIds) {
        super.setCategoryIds(categoryIds);
    }

    @Override
    public void setLastDomainId(Long lastDomainId) {
        super.setLastDomainId(lastDomainId);
    }

    @Override
    public void setCursorValue(String cursorValue) {
        super.setCursorValue(cursorValue);
    }

    @Override
    public void setSellerId(Long sellerId) {
        super.setSellerId(sellerId);
    }

    @Override
    public boolean hasExistLastIdAndCursorValue() {
        return super.hasExistLastIdAndCursorValue();
    }

    public void setProductGroupId(Long productGroupId) {
        this.productGroupId = productGroupId;
    }
}
