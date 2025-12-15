package com.ryuqq.setof.application.productdescription.service.command;

import com.ryuqq.setof.application.productdescription.dto.command.UpdateProductDescriptionCommand;
import com.ryuqq.setof.application.productdescription.factory.command.ProductDescriptionCommandFactory;
import com.ryuqq.setof.application.productdescription.manager.command.ProductDescriptionPersistenceManager;
import com.ryuqq.setof.application.productdescription.manager.query.ProductDescriptionReadManager;
import com.ryuqq.setof.application.productdescription.port.in.command.UpdateProductDescriptionUseCase;
import com.ryuqq.setof.domain.productdescription.aggregate.ProductDescription;
import com.ryuqq.setof.domain.productdescription.vo.DescriptionImage;
import com.ryuqq.setof.domain.productdescription.vo.HtmlContent;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * 상품설명 수정 서비스
 *
 * <p>상품설명을 수정합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class UpdateProductDescriptionService implements UpdateProductDescriptionUseCase {

    private final ProductDescriptionReadManager productDescriptionReadManager;
    private final ProductDescriptionPersistenceManager productDescriptionPersistenceManager;
    private final ProductDescriptionCommandFactory productDescriptionCommandFactory;

    public UpdateProductDescriptionService(
            ProductDescriptionReadManager productDescriptionReadManager,
            ProductDescriptionPersistenceManager productDescriptionPersistenceManager,
            ProductDescriptionCommandFactory productDescriptionCommandFactory) {
        this.productDescriptionReadManager = productDescriptionReadManager;
        this.productDescriptionPersistenceManager = productDescriptionPersistenceManager;
        this.productDescriptionCommandFactory = productDescriptionCommandFactory;
    }

    @Override
    public void execute(UpdateProductDescriptionCommand command) {
        ProductDescription productDescription =
                productDescriptionReadManager.findById(command.productDescriptionId());

        Instant now = productDescriptionCommandFactory.now();
        HtmlContent htmlContent = HtmlContent.of(command.htmlContent());
        List<DescriptionImage> images =
                productDescriptionCommandFactory.toImages(command.images(), now);

        ProductDescription updated = productDescription.update(htmlContent, images, now);

        productDescriptionPersistenceManager.update(updated);
    }
}
