package com.ryuqq.setof.application.product.dto.command;

public record UpdateProductPriceCommand(long productId, int regularPrice, int currentPrice) {}
