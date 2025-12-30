package com.connectly.partnerAdmin.module.external.dto.order;

import com.connectly.partnerAdmin.module.external.core.ExMallOrder;
import com.connectly.partnerAdmin.module.external.core.ExMallOrderProduct;
import com.connectly.partnerAdmin.module.external.core.ExMallUser;
import com.connectly.partnerAdmin.module.external.dto.user.ExMallUserInfo;
import com.connectly.partnerAdmin.module.order.enums.OrderStatus;
import com.connectly.partnerAdmin.module.order.enums.SiteName;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@JsonTypeName("excelOrderSheet")
public class ExcelOrderSheet implements ExMallOrder, ExMallOrderProduct {

    private SiteName siteName;
    private String skuNumber;
    private int quantity;
    private String uniqueOrderKey;
    private String userName;
    private String phoneNumber;
    private String email;

    @Override
    public String getBuyerName() {
        return userName;
    }

    @Override
    public String getBuyerPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public OrderStatus getOrderStatus() {
        return OrderStatus.ORDER_COMPLETED;
    }

    @Override
    public String getBuyerEmail() {
        return email;
    }

    @Override
    public ExMallUser getExMallUser() {
        return new ExMallUserInfo(userName, phoneNumber, siteName);
    }

    @Override
    public long getExMallOrderId() {
        return 0;
    }

    @Override
    public String getExMallOrderCode() {
        return "";
    }

    @Override
    public List<Long> getProductGroupIds() {
        long productGroupId = getProductGroupId();
        return List.of(productGroupId);
    }

    @Override
    public long getSiteId() {
        return siteName.getSiteId();
    }

    public long getProductGroupId(){
        return Long.parseLong(skuNumber.split("_")[0]);
    }

    public String getOptionName(){
        String[] split = skuNumber.split("_");
        if(split.length == 2){
            return split[1];
        }else if(split.length ==1){
            return "";
        } else{
            return skuNumber.split("_")[1]+ skuNumber.split("_")[2];
        }
    }
}
