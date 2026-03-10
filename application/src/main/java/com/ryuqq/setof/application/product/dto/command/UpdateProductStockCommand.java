package com.ryuqq.setof.application.product.dto.command;

public record UpdateProductStockCommand(long productId, int stockQuantity) {}
