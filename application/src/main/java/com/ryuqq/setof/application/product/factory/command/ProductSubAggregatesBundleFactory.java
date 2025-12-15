package com.ryuqq.setof.application.product.factory.command;

import com.ryuqq.setof.application.product.dto.bundle.ProductSubAggregatesPersistBundle;
import com.ryuqq.setof.application.product.dto.command.ProductDescriptionCommandDto;
import com.ryuqq.setof.application.product.dto.command.ProductImageCommandDto;
import com.ryuqq.setof.application.product.dto.command.ProductNoticeCommandDto;
import com.ryuqq.setof.application.product.dto.command.ProductSkuCommandDto;
import com.ryuqq.setof.application.product.dto.command.RegisterFullProductCommand;
import com.ryuqq.setof.application.product.dto.command.RegisterProductGroupCommand.RegisterProductCommand;
import com.ryuqq.setof.application.productdescription.dto.command.RegisterProductDescriptionCommand;
import com.ryuqq.setof.application.productdescription.factory.command.ProductDescriptionCommandFactory;
import com.ryuqq.setof.application.productimage.dto.command.RegisterProductImageCommand;
import com.ryuqq.setof.application.productimage.factory.command.ProductImageCommandFactory;
import com.ryuqq.setof.application.productnotice.dto.command.RegisterProductNoticeCommand;
import com.ryuqq.setof.application.productnotice.factory.command.ProductNoticeCommandFactory;
import com.ryuqq.setof.domain.product.aggregate.Product;
import com.ryuqq.setof.domain.product.vo.OptionType;
import com.ryuqq.setof.domain.productdescription.aggregate.ProductDescription;
import com.ryuqq.setof.domain.productimage.aggregate.ProductImage;
import com.ryuqq.setof.domain.productnotice.aggregate.ProductNotice;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Component;

/**
 * Product 하위 Aggregate Bundle Factory
 *
 * <p>RegisterFullProductCommand와 productGroupId로 하위 Aggregate Bundle 생성
 *
 * <p>기존 개별 Factory 재사용
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ProductSubAggregatesBundleFactory {

    private final ProductGroupCommandFactory productFactory;
    private final ProductImageCommandFactory imageFactory;
    private final ProductDescriptionCommandFactory descriptionFactory;
    private final ProductNoticeCommandFactory noticeFactory;

    public ProductSubAggregatesBundleFactory(
            ProductGroupCommandFactory productFactory,
            ProductImageCommandFactory imageFactory,
            ProductDescriptionCommandFactory descriptionFactory,
            ProductNoticeCommandFactory noticeFactory) {
        this.productFactory = productFactory;
        this.imageFactory = imageFactory;
        this.descriptionFactory = descriptionFactory;
        this.noticeFactory = noticeFactory;
    }

    /**
     * Bundle 생성
     *
     * @param productGroupId 저장된 ProductGroup ID
     * @param command 전체 등록 Command
     * @return 하위 Aggregate Bundle
     */
    public ProductSubAggregatesPersistBundle create(
            Long productGroupId, RegisterFullProductCommand command) {
        Objects.requireNonNull(productGroupId, "productGroupId must not be null");
        Objects.requireNonNull(command, "command must not be null");
        Objects.requireNonNull(command.products(), "products must not be null");
        Objects.requireNonNull(command.description(), "description must not be null");
        Objects.requireNonNull(command.notice(), "notice must not be null");

        OptionType optionType = OptionType.valueOf(command.optionType());
        List<Product> products = createProducts(productGroupId, optionType, command.products());
        List<ProductImage> images = createImages(productGroupId, command.images());
        ProductDescription description = createDescription(productGroupId, command.description());
        ProductNotice notice = createNotice(productGroupId, command.notice());

        return new ProductSubAggregatesPersistBundle(products, images, description, notice);
    }

    private List<Product> createProducts(
            Long productGroupId, OptionType optionType, List<ProductSkuCommandDto> skuDtos) {
        List<RegisterProductCommand> productCommands =
                skuDtos.stream().map(this::toRegisterProductCommand).toList();
        return productFactory.createProducts(productGroupId, optionType, productCommands);
    }

    private RegisterProductCommand toRegisterProductCommand(ProductSkuCommandDto sku) {
        return new RegisterProductCommand(
                sku.option1Name(),
                sku.option1Value(),
                sku.option2Name(),
                sku.option2Value(),
                sku.additionalPrice(),
                sku.initialStock());
    }

    private List<ProductImage> createImages(
            Long productGroupId, List<ProductImageCommandDto> imageDtos) {
        if (imageDtos == null || imageDtos.isEmpty()) {
            return List.of();
        }

        return imageDtos.stream().map(dto -> createImage(productGroupId, dto)).toList();
    }

    private ProductImage createImage(Long productGroupId, ProductImageCommandDto dto) {
        RegisterProductImageCommand command =
                new RegisterProductImageCommand(
                        productGroupId,
                        dto.imageType(),
                        dto.originUrl(),
                        dto.cdnUrl(),
                        dto.displayOrder());
        return imageFactory.createFromRegisterCommand(command);
    }

    private ProductDescription createDescription(
            Long productGroupId, ProductDescriptionCommandDto dto) {
        RegisterProductDescriptionCommand command =
                new RegisterProductDescriptionCommand(
                        productGroupId, dto.htmlContent(), dto.images());
        return descriptionFactory.createProductDescription(command);
    }

    private ProductNotice createNotice(Long productGroupId, ProductNoticeCommandDto dto) {
        RegisterProductNoticeCommand command =
                new RegisterProductNoticeCommand(productGroupId, dto.templateId(), dto.items());
        return noticeFactory.createProductNotice(command);
    }
}
