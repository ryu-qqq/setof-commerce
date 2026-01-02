package com.connectly.partnerAdmin.module.product.service.group;

import java.util.List;

import com.connectly.partnerAdmin.module.product.dto.query.CreateDeliveryNotice;
import com.connectly.partnerAdmin.module.product.dto.query.CreatePrice;
import com.connectly.partnerAdmin.module.product.dto.query.CreateProductImage;
import com.connectly.partnerAdmin.module.product.dto.query.CreateProductNotice;
import com.connectly.partnerAdmin.module.product.dto.query.CreateRefundNotice;
import com.connectly.partnerAdmin.module.product.dto.query.DeleteProductGroup;
import com.connectly.partnerAdmin.module.product.dto.query.UpdateCategory;
import com.connectly.partnerAdmin.module.product.dto.query.UpdateDisplayYn;
import com.connectly.partnerAdmin.module.product.dto.query.UpdateProductDescription;
import com.connectly.partnerAdmin.module.product.dto.query.UpdateProductGroup;
import com.connectly.partnerAdmin.module.product.request.UpdatePriceContext;

public interface ProductGroupUpdateService {

    long updateProductNotice(long productGroupId, CreateProductNotice createProductNotice);
    long updateProductDeliveryNotice(long productGroupId, CreateDeliveryNotice deliveryNotice);
    long updateProductRefundNotice(long productGroupId, CreateRefundNotice refundNotice);

    long updateProductImage(long productGroupId, List<CreateProductImage> createProductImages);
    long updateDetailDescription(long productGroupId, UpdateProductDescription updateProductDescription);
    long updateProductGroupCategory(long productGroupId, UpdateCategory updateCategory);
    long updatePrice(long productGroupId, CreatePrice createPrice);
    int updatePrice(UpdatePriceContext updatePriceContext);

    long updateProductGroup(long productGroupId, UpdateProductGroup updateProductGroup);
    long updateGroupDisplayYn(long productGroupId, UpdateDisplayYn updateDisplayYn);

    List<Long> deleteProductGroups(DeleteProductGroup deleteProductGroup);

}
