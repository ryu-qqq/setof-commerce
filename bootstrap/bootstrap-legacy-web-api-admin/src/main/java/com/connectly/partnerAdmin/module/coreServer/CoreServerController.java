package com.connectly.partnerAdmin.module.coreServer;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.connectly.partnerAdmin.module.common.config.EndPointsConstants.BASE_END_POINT_V1;

@RestController
@RequestMapping(BASE_END_POINT_V1)
@RequiredArgsConstructor
public class CoreServerController {

    private final CoreServerProductService coreServerProductService;


    @GetMapping("/core/{productGroupId}")
    public void fetchProductGroup(@PathVariable long productGroupId){
        coreServerProductService.register(productGroupId);
    }

    @GetMapping("/core/update/{productGroupId}")
    public void updateProductGroup(@PathVariable long productGroupId){
        coreServerProductService.update(productGroupId);
    }

    @GetMapping("/core/register/{productGroupId}")
    public void registerProductGroup(@PathVariable long productGroupId){
        coreServerProductService.registerCore(productGroupId);
    }


}

