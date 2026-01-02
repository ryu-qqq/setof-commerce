package com.connectly.partnerAdmin.module.external.service.order;

import com.connectly.partnerAdmin.module.external.dto.order.ExcelOrderSheet;
import com.connectly.partnerAdmin.module.external.entity.ExternalOrder;

import java.util.List;

public interface ExternalOrderExcelIssueService {

    List<ExternalOrder> syncOrders(List<ExcelOrderSheet> excelOrderSheets);

}
