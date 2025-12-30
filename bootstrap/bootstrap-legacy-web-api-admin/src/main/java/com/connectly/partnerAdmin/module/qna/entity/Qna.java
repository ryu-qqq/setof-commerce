package com.connectly.partnerAdmin.module.qna.entity;

import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.qna.entity.embedded.QnaContents;
import com.connectly.partnerAdmin.module.qna.enums.QnaDetailType;
import com.connectly.partnerAdmin.module.qna.enums.QnaStatus;
import com.connectly.partnerAdmin.module.qna.enums.QnaType;
import com.connectly.partnerAdmin.module.user.enums.UserType;
import lombok.*;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Getter
@Table(name = "QNA")
@Entity
public class Qna extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "QNA_ID")
    private long id;

    private QnaContents qnaContents;

    @Enumerated(EnumType.STRING)
    private Yn privateYn;

    @Enumerated(EnumType.STRING)
    private QnaStatus qnaStatus;

    @Enumerated(EnumType.STRING)
    private QnaType qnaType;

    @Enumerated(EnumType.STRING)
    private QnaDetailType qnaDetailType;

    @Column(name = "USER_ID")
    private long userId;

    @Column(name = "SELLER_ID")
    private long sellerId;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    @OneToOne(mappedBy = "qna", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private QnaOrder qnaOrder;

    @OneToOne(mappedBy = "qna", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private QnaProduct qnaProduct;

    @OneToMany(mappedBy = "qna", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<QnaImage> qnaImages = new ArrayList<>();


    public Qna(QnaContents qnaContents, boolean privateYn, QnaType qnaType, QnaDetailType qnaDetailType, long userId, long sellerId) {
        this.qnaContents = qnaContents;
        this.privateYn = privateYn ? Yn.Y : Yn.N;
        this.qnaStatus = QnaStatus.OPEN;
        this.qnaType = qnaType;
        this.qnaDetailType = qnaDetailType;
        this.userType = UserType.MEMBERS;
        this.userId = userId;
        this.sellerId = sellerId;
    }

    public void addQnaImages(QnaImage image) {
        qnaImages.add(image);
        image.setQna(this);
    }

    public void deleteQnaImages(QnaImage image) {
        this.qnaImages.remove(image);
    }

    public void setQnaOrder(QnaOrder qnaOrder) {

        if (this.qnaOrder != null && this.qnaOrder.equals(qnaOrder)) {
            return;
        }

        this.qnaOrder = qnaOrder;

        if (qnaOrder != null && !qnaOrder.getQna().equals(this)) {
            qnaOrder.setQna(this);
        }

    }

    public void setQnaProduct(QnaProduct qnaProduct) {
        if (this.qnaProduct != null && this.qnaProduct.equals(qnaProduct)) {
            return;
        }

        this.qnaProduct = qnaProduct;

        if (qnaProduct != null && !qnaProduct.getQna().equals(this)) {
            qnaProduct.setQna(this);
        }
    }


    public void reply(){
        this.qnaStatus = QnaStatus.CLOSED;
    }


}
