package com.ryuqq.setof.adapter.out.client.smartdelivery.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * SmartDeliveryTrackingResponse - 스마트택배 추적 API 응답
 *
 * <p>스마트택배 API /api/v1/trackingInfo 응답을 매핑합니다.
 *
 * <p><strong>응답 예시:</strong>
 *
 * <pre>{@code
 * {
 *   "result": "Y",
 *   "senderName": "홍*동",
 *   "receiverName": "김*철",
 *   "itemName": "의류",
 *   "level": 6,
 *   "complete": true,
 *   "trackingDetails": [...]
 * }
 * }</pre>
 *
 * @author development-team
 * @since 1.0.0
 */
public class SmartDeliveryTrackingResponse {

    /** 결과 코드 (Y: 성공, N: 실패) */
    private String result;

    /** 오류 메시지 (실패 시) */
    private String msg;

    /** 발송인 이름 (마스킹) */
    private String senderName;

    /** 수신인 이름 (마스킹) */
    private String receiverName;

    /** 상품명 */
    private String itemName;

    /** 인보이스 번호 */
    @JsonProperty("invoiceNo")
    private String invoiceNumber;

    /** 배송 단계 (1~6) */
    private Integer level;

    /** 배송 완료 여부 */
    private Boolean complete;

    /** 택배사명 */
    @JsonProperty("companyName")
    private String companyName;

    /** 추적 상세 내역 */
    private List<TrackingDetail> trackingDetails;

    // 기본 생성자
    public SmartDeliveryTrackingResponse() {}

    // ===== Getters =====

    public String getResult() {
        return result;
    }

    public String getMsg() {
        return msg;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public String getItemName() {
        return itemName;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public Integer getLevel() {
        return level;
    }

    public Boolean getComplete() {
        return complete;
    }

    public String getCompanyName() {
        return companyName;
    }

    public List<TrackingDetail> getTrackingDetails() {
        return trackingDetails;
    }

    // ===== Setters (Jackson deserialization) =====

    public void setResult(String result) {
        this.result = result;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public void setComplete(Boolean complete) {
        this.complete = complete;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setTrackingDetails(List<TrackingDetail> trackingDetails) {
        this.trackingDetails = trackingDetails;
    }

    // ===== 편의 메서드 =====

    /**
     * 성공 여부 확인
     *
     * @return 성공이면 true
     */
    public boolean isSuccess() {
        return "Y".equalsIgnoreCase(result);
    }

    /**
     * 배송 완료 여부 확인
     *
     * @return 배송 완료면 true
     */
    public boolean isDelivered() {
        return Boolean.TRUE.equals(complete);
    }

    /**
     * 추적 데이터 존재 여부 확인
     *
     * @return 추적 데이터가 있으면 true
     */
    public boolean hasTrackingDetails() {
        return trackingDetails != null && !trackingDetails.isEmpty();
    }

    /**
     * 마지막 추적 상세 반환
     *
     * @return 마지막 TrackingDetail, 없으면 null
     */
    public TrackingDetail getLastDetail() {
        if (!hasTrackingDetails()) {
            return null;
        }
        return trackingDetails.get(trackingDetails.size() - 1);
    }

    /** TrackingDetail - 추적 상세 내역 */
    public static class TrackingDetail {

        /** 처리 시각 (yyyyMMddHHmmss 또는 yyyy.MM.dd HH:mm:ss) */
        private String timeString;

        /** 위치 */
        private String where;

        /** 현재 상태 */
        private String kind;

        /** 담당자/기사 */
        private String telno;

        /** 담당자 전화번호 */
        private String telno2;

        /** 상세 설명 */
        private String code;

        public TrackingDetail() {}

        public String getTimeString() {
            return timeString;
        }

        public void setTimeString(String timeString) {
            this.timeString = timeString;
        }

        public String getWhere() {
            return where;
        }

        public void setWhere(String where) {
            this.where = where;
        }

        public String getKind() {
            return kind;
        }

        public void setKind(String kind) {
            this.kind = kind;
        }

        public String getTelno() {
            return telno;
        }

        public void setTelno(String telno) {
            this.telno = telno;
        }

        public String getTelno2() {
            return telno2;
        }

        public void setTelno2(String telno2) {
            this.telno2 = telno2;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }
}
