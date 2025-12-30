package com.connectly.partnerAdmin.module.qna.entity;


import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import jakarta.persistence.*;



@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Getter
@Table(name = "QNA_ORDER")
@Entity
public class QnaOrder extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "QNA_ORDER_ID")
    private long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "QNA_ID", nullable = false)
    @JsonBackReference
    private Qna qna;

    @Column(name = "ORDER_ID")
    private long orderId;



    public QnaOrder(Qna qna, long orderId) {
        this.qna = qna;
        this.orderId = orderId;
    }

    public void setQna(Qna qna) {
        if (this.qna != null && this.qna.equals(qna)) {
            return;
        }
        this.qna = qna;
        if (qna != null && !qna.getQnaProduct().equals(this)) {
            qna.setQnaOrder(this);
        }
    }


}
