package com.ryuqq.setof.application.productimage.factory.command;

import com.ryuqq.setof.application.productimage.dto.command.RegisterProductImageCommand;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import com.ryuqq.setof.domain.product.vo.ProductGroupId;
import com.ryuqq.setof.domain.productimage.aggregate.ProductImage;
import com.ryuqq.setof.domain.productimage.vo.ImageType;
import com.ryuqq.setof.domain.productimage.vo.ImageUrl;
import java.time.Instant;
import org.springframework.stereotype.Component;

/**
 * ProductImage Command Factory
 *
 * <p>Command DTO로부터 ProductImage 도메인 객체 생성
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ProductImageCommandFactory {

    private final ClockHolder clockHolder;

    public ProductImageCommandFactory(ClockHolder clockHolder) {
        this.clockHolder = clockHolder;
    }

    /**
     * 등록 Command로부터 ProductImage 생성
     *
     * @param command 등록 Command
     * @return ProductImage 도메인 객체
     */
    public ProductImage createFromRegisterCommand(RegisterProductImageCommand command) {
        Instant now = Instant.now(clockHolder.getClock());

        ProductGroupId productGroupId = ProductGroupId.of(command.productGroupId());
        ImageType imageType = ImageType.valueOf(command.imageType());
        ImageUrl originUrl = ImageUrl.of(command.originUrl());
        ImageUrl cdnUrl =
                command.cdnUrl() != null && !command.cdnUrl().isBlank()
                        ? ImageUrl.of(command.cdnUrl())
                        : originUrl;

        return ProductImage.create(
                productGroupId, imageType, originUrl, cdnUrl, command.displayOrder(), now);
    }
}
