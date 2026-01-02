package com.connectly.partnerAdmin.module.common.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.connectly.partnerAdmin.module.common.config.EndPointsConstants.BASE_END_POINT_V1;

@Controller
@RequestMapping(BASE_END_POINT_V1)
public class RestDocsController {

    @GetMapping("/docs")
    public String getRestDocs(){
        return "/docs/index.html";
    }

    @GetMapping("/common/{id}")
    public String getRestDocs(@PathVariable String id){
        return "/docs/common/" + id;
    }

}
