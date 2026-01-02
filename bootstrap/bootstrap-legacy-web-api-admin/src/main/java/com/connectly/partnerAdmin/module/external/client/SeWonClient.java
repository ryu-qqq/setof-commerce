package com.connectly.partnerAdmin.module.external.client;


import com.connectly.partnerAdmin.module.external.dto.qna.SewonQnaRequest;
import com.connectly.partnerAdmin.module.external.dto.category.SeWonCategoryRequest;
import com.connectly.partnerAdmin.module.external.dto.category.SeWonCategoryResponse;
import com.connectly.partnerAdmin.module.external.dto.order.sellic.SellicClaimOrderRequest;
import com.connectly.partnerAdmin.module.external.dto.order.sellic.SellicClaimOrderResponse;
import com.connectly.partnerAdmin.module.external.dto.order.sellic.SellicShipmentRequestDto;
import com.connectly.partnerAdmin.module.external.dto.order.sellic.SellicShipmentResponseDto;
import com.connectly.partnerAdmin.module.external.payload.SeWonResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "seWonClient", url="${sewon.host-url}")
public interface SeWonClient {

    @PostMapping("/get_category")
    ResponseEntity<SeWonResponse<List<SeWonCategoryResponse>>> fetchSewonCategories(
            @RequestBody SeWonCategoryRequest seWonCategoryRequest
    );

    @PostMapping("/set_ship")
    ResponseEntity<SeWonResponse<List<SellicShipmentResponseDto>>> createShipment(
            @RequestBody SellicShipmentRequestDto seWonShipmentRequestDto
    );


    @PostMapping("/get_claim")
    ResponseEntity<SeWonResponse<List<SellicClaimOrderResponse>>> fetchClaimOrders(
            @RequestBody SellicClaimOrderRequest sellicClaimOrderRequest
    );

    @PostMapping("/set_answer")
    ResponseEntity<SeWonResponse<Object>> createQna(
            @RequestBody SewonQnaRequest sewonQnaRequest
    );


}
