package com.ryuqq.setof.application.brand.service;

import com.ryuqq.setof.application.brand.assembler.BrandAssembler;
import com.ryuqq.setof.application.brand.dto.response.BrandResult;
import com.ryuqq.setof.application.brand.manager.BrandReadManager;
import com.ryuqq.setof.application.brand.port.in.GetBrandByIdUseCase;
import com.ryuqq.setof.domain.brand.aggregate.Brand;
import com.ryuqq.setof.domain.brand.id.BrandId;
import org.springframework.stereotype.Service;

/**
 * 브랜드 단건 조회 Service.
 *
 * <p>브랜드 ID로 단건 조회합니다.
 *
 * <p>ReadManager를 통해 Brand 조회
 *
 * <p>Assembler를 통해 BrandResult 생성
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Service
public class GetBrandByIdService implements GetBrandByIdUseCase {

    private final BrandReadManager readManager;
    private final BrandAssembler assembler;

    public GetBrandByIdService(BrandReadManager readManager, BrandAssembler assembler) {
        this.readManager = readManager;
        this.assembler = assembler;
    }

    @Override
    public BrandResult execute(Long brandId) {
        Brand brand = readManager.getById(BrandId.of(brandId));

        return assembler.toResult(brand);
    }
}
