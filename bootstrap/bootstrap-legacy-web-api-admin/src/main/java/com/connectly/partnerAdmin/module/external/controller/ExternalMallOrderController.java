package com.connectly.partnerAdmin.module.external.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.connectly.partnerAdmin.module.external.core.ExMallClaimOrder;
import com.connectly.partnerAdmin.module.external.core.ExMallOrder;
import com.connectly.partnerAdmin.module.external.dto.order.ExcelOrderSheet;
import com.connectly.partnerAdmin.module.external.dto.order.buyma.BuymaOrder;
import com.connectly.partnerAdmin.module.external.entity.ExternalOrder;
import com.connectly.partnerAdmin.module.external.service.order.ExternalOrderExcelIssueService;
import com.connectly.partnerAdmin.module.external.service.order.ExternalOrderIssueService;
import com.connectly.partnerAdmin.module.external.service.order.lf.LfOrderIssueService;
import com.connectly.partnerAdmin.module.external.service.order.lf.LfOrderRequestResponseDto;
import com.connectly.partnerAdmin.module.notification.service.slack.SlackOrderIssueService;
import com.connectly.partnerAdmin.module.order.dto.UpdateOrderResponse;
import com.connectly.partnerAdmin.module.payload.ApiResponse;

import static com.connectly.partnerAdmin.module.common.config.EndPointsConstants.BASE_END_POINT_V1;

import lombok.RequiredArgsConstructor;


//@PreAuthorize(HAS_AUTHORITY_MASTER)
@RestController
@RequestMapping(BASE_END_POINT_V1)
@RequiredArgsConstructor
public class ExternalMallOrderController<T extends ExMallOrder, R extends ExMallClaimOrder>  {

    private final ExternalOrderIssueService externalOrderIssueService;
    private final ExternalOrderExcelIssueService externalOrderExcelIssueService;
    private final LfOrderIssueService lfOrderIssueService;
    private final SlackOrderIssueService slackOrderIssueService;

    @PostMapping("/external/order")
    public ResponseEntity<ApiResponse<List<ExternalOrder>>> syncOrders(@RequestBody T t){
        return ResponseEntity.ok(ApiResponse.success(externalOrderIssueService.syncOrders(t)));
    }

    @PostMapping("/external/buyma/order")
    public ResponseEntity<ApiResponse<List<ExternalOrder>>> syncBuymaOrders(@RequestBody BuymaOrder buymaOrder){
        return ResponseEntity.ok(ApiResponse.success(externalOrderIssueService.syncOrders(buymaOrder)));
    }

    @PutMapping("/external/order")
    public ResponseEntity<ApiResponse<UpdateOrderResponse>> interLockingClaimOrders(@RequestBody R r){
        return ResponseEntity.ok(ApiResponse.success(externalOrderIssueService.syncClaimOrder(r)));
    }

    @PostMapping("/external/order/excel")
    public ResponseEntity<ApiResponse<List<ExternalOrder>>> syncOrders(@RequestBody List<ExcelOrderSheet> excelOrderSheets){
        return ResponseEntity.ok(ApiResponse.success(externalOrderExcelIssueService.syncOrders(excelOrderSheets)));
    }


    @PostMapping("/external/order/lf")
    public ResponseEntity<ApiResponse<Long>> syncOrdersLf(@RequestBody LfOrderRequestResponseDto responseDto){
        return ResponseEntity.ok(ApiResponse.success(lfOrderIssueService.syncOrder(responseDto)));
    }

    @GetMapping("/slack/{paymentId}")
    public void trigger(@PathVariable("paymentId") long paymentId){
        slackOrderIssueService.sendSlackMessage(paymentId);
    }


}
