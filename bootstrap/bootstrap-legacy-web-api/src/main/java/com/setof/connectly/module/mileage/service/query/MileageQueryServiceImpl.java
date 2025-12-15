package com.setof.connectly.module.mileage.service.query;

import com.setof.connectly.module.mileage.dto.*;
import com.setof.connectly.module.mileage.dto.query.PendingMileageQueryDto;
import com.setof.connectly.module.mileage.entity.Mileage;
import com.setof.connectly.module.mileage.entity.MileageHistory;
import com.setof.connectly.module.mileage.entity.MileageTransaction;
import com.setof.connectly.module.mileage.enums.MileageIssueType;
import com.setof.connectly.module.mileage.enums.MileageStatus;
import com.setof.connectly.module.mileage.enums.Reason;
import com.setof.connectly.module.mileage.mapper.MileageMapper;
import com.setof.connectly.module.mileage.repository.MileageRepository;
import com.setof.connectly.module.mileage.repository.MileageTransactionRepository;
import com.setof.connectly.module.mileage.repository.history.MileageHistoryJdbcRepository;
import com.setof.connectly.module.mileage.repository.query.MileageQueryRepository;
import com.setof.connectly.module.mileage.service.fetch.MileageFindService;
import com.setof.connectly.module.order.dto.query.OrderAmountDto;
import com.setof.connectly.module.order.dto.query.RefundOrderInfo;
import com.setof.connectly.module.order.entity.snapshot.mileage.OrderSnapShotMileage;
import com.setof.connectly.module.order.entity.snapshot.mileage.OrderSnapShotMileageDetail;
import com.setof.connectly.module.order.repository.snapshot.query.mileage.OrderSnapShotMileageDetailJdbcRepository;
import com.setof.connectly.module.order.repository.snapshot.query.mileage.OrderSnapShotMileageRepository;
import com.setof.connectly.module.order.service.fetch.OrderFindService;
import com.setof.connectly.module.payment.entity.snapshot.PaymentSnapShotMileage;
import com.setof.connectly.module.payment.repository.mileage.PaymentSnapShotMileageRepository;
import com.setof.connectly.module.user.entity.Users;
import com.setof.connectly.module.user.repository.mileage.query.UserMileageJdbcRepository;
import com.setof.connectly.module.utils.NumberUtils;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class MileageQueryServiceImpl implements MileageQueryService {
    private final MileageMapper mileageMapper;
    private final MileageFindService mileageFindService;
    private final MileageQueryRepository mileageQueryRepository;
    private final UserMileageJdbcRepository userMileageJdbcRepository;
    private final PaymentSnapShotMileageRepository paymentSnapShotMileageRepository;
    private final MileageHistoryJdbcRepository mileageHistoryJdbcRepository;
    private final MileageSaveQueryService mileageSaveQueryService;
    private final OrderFindService orderFindService;
    private final OrderSnapShotMileageRepository orderSnapShotMileageRepository;
    private final OrderSnapShotMileageDetailJdbcRepository orderSnapShotMileageDetailJdbcRepository;
    private final MileageManageService mileageManageService;
    private final UserMileageRedisQueryService userMileageRedisQueryService;
    private final MileageRepository mileageRepository;
    private final MileageTransactionRepository mileageTransactionRepository;

    @Override
    public void saveMileageForJoining(Users users) {
        Mileage mileage = mileageMapper.joinMileageForOpenEvent(users);
        mileageRepository.save(mileage);
        MileageHistory mileageHistory = mileageMapper.toMileageHistory(mileage);
        mileageHistoryJdbcRepository.saveAll(Collections.singletonList(mileageHistory));
        MileageTransaction mileageTransaction =
                mileageMapper.toMileageTransaction(
                        users.getId(),
                        MileageIssueType.JOIN,
                        mileageHistory.getTargetId(),
                        mileage.getMileageAmount(),
                        MileageStatus.APPROVED);
        mileageTransactionRepository.save(mileageTransaction);
    }

    @Override
    public void rollBackMileage(long paymentId, long userId, RefundOrderInfo refundOrderInfo) {
        MileageRefundQueryDto mileageRefundQueryDto =
                mileageFindService.fetchUsedMileage(
                        paymentId, refundOrderInfo.getOrderId(), userId);
        processMileageRefund(userId, refundOrderInfo, mileageRefundQueryDto);
        Optional<MileageTransaction> mileageTransaction =
                mileageFindService.fetchMileageTransactionEntity(refundOrderInfo.getOrderId());
        mileageTransaction.ifPresent(MileageTransaction::delete);
    }

    @Override
    public void deductMileage(
            long paymentId, long userId, long paymentAmount, double usedMileageAmount) {
        Optional<PendingMileageQueryDto> pendingMileageQueryOpt =
                orderFindService.fetchPendingMileage(paymentId);

        if (usedMileageAmount > 0) {
            Optional<MileageQueryDto> mileageQueryDtoOpt =
                    mileageFindService.fetchUsableMileage(userId);
            if (mileageQueryDtoOpt.isPresent()) {
                MileageDeductionResult deductionResult =
                        mileageManageService.calculateMileageDeduction(
                                mileageQueryDtoOpt.get(), usedMileageAmount);
                if (!deductionResult.getUsedMileages().isEmpty()) {
                    updateUserMileage(userId, deductionResult.getCurrentUsingMileageAmount());
                    createPaymentSnapshotHistories(
                            userId,
                            paymentId,
                            usedMileageAmount,
                            deductionResult.getCurrentUsingMileageAmount());
                    createOrderSnapShotMileages(
                            userId,
                            paymentId,
                            usedMileageAmount,
                            deductionResult.getUsedMileages(),
                            pendingMileageQueryOpt);
                    updateMileage(deductionResult.getUsedMileages());
                }
            }
        }

        saveMileageTransaction(userId, paymentAmount, pendingMileageQueryOpt);
    }

    private void processMileageRefund(
            long userId,
            RefundOrderInfo refundOrderInfo,
            MileageRefundQueryDto mileageRefundQueryDto) {
        LocalDateTime now = LocalDateTime.now();
        List<MileageHistory> mileageHistories = new ArrayList<>();
        List<MileageRefundDto> usedMileage = new ArrayList<>();
        double totalUsedAmount = 0;

        for (MileageRefundDto mileageRefundDto : mileageRefundQueryDto.getMileages()) {
            if (mileageRefundDto.getExpirationDate().isAfter(now)) {
                mileageHistories.add(
                        mileageMapper.toMileageHistory(
                                userId,
                                refundOrderInfo.getOrderId(),
                                Reason.REFUND,
                                MileageIssueType.ORDER,
                                mileageRefundDto));
                totalUsedAmount += mileageRefundDto.getUsedAmount();
            } else {
                mileageRefundDto.deActiveYn();
                mileageHistories.add(
                        mileageMapper.toMileageHistory(
                                userId,
                                refundOrderInfo.getOrderId(),
                                Reason.EXPIRED,
                                MileageIssueType.ORDER,
                                mileageRefundDto));
            }
            usedMileage.add(mileageRefundDto);
        }

        if (totalUsedAmount > 0) {
            updateUserMileageRefund(userId, totalUsedAmount);
        }

        if (!usedMileage.isEmpty()) {
            updateRefundMileages(usedMileage);
        }

        createMileageHistories(mileageHistories);
    }

    private void updateMileage(List<MileageDto> usedMileage) {
        mileageQueryRepository.updateMileages(usedMileage);
    }

    private void updateRefundMileages(List<MileageRefundDto> usedMileage) {
        mileageQueryRepository.updateRefundMileages(usedMileage);
    }

    private void updateUserMileage(long userId, double currentUsingMileageAmount) {
        userMileageJdbcRepository.updateUsingMileage(currentUsingMileageAmount, userId);
        userMileageRedisQueryService.updateUserMileageInCache(
                userId, currentUsingMileageAmount * -1);
    }

    private void updateUserMileageRefund(long userId, double totalRefundMileage) {
        userMileageJdbcRepository.updateRefundMileage(totalRefundMileage, userId);
        userMileageRedisQueryService.updateUserMileageInCache(userId, totalRefundMileage);
    }

    private void createMileageHistories(List<MileageHistory> mileageHistories) {
        mileageHistoryJdbcRepository.saveAll(mileageHistories);
    }

    private void createPaymentSnapshotHistories(
            long userId,
            long paymentId,
            double usedMileageAmount,
            double currentUsingMileageAmount) {
        PaymentSnapShotMileage paymentSnapShotMileage =
                mileageMapper.toSnapShot(
                        userId, paymentId, usedMileageAmount, currentUsingMileageAmount);
        paymentSnapShotMileageRepository.save(paymentSnapShotMileage);
    }

    private void createOrderSnapShotMileages(
            long userId,
            long paymentId,
            double usedMileageAmount,
            List<MileageDto> usedMileages,
            Optional<PendingMileageQueryDto> pendingMileageQueryOpt) {
        if (pendingMileageQueryOpt.isPresent()) {

            Map<Long, MileageDto> mileageMap =
                    usedMileages.stream()
                            .collect(
                                    Collectors.toMap(
                                            MileageDto::getMileageId, Function.identity()));

            List<MileageHistory> mileageHistories = new ArrayList<>();

            PendingMileageQueryDto pendingMileageQueryDto = pendingMileageQueryOpt.get();
            double totalOrderAmount = pendingMileageQueryDto.getTotalOrderAmount();

            List<OrderSnapShotMileageDetail> toSaveOrderSnapShotMileageDetails = new ArrayList<>();

            for (OrderAmountDto order : pendingMileageQueryDto.getOrderAmounts()) {
                long orderId = order.getOrderId();
                long orderAmount = order.getOrderAmount();

                BigDecimal usedMileageForOrder =
                        NumberUtils.getProPortion(totalOrderAmount, orderAmount, usedMileageAmount);

                OrderSnapShotMileage orderSnapShotMileage =
                        OrderSnapShotMileage.builder()
                                .orderId(orderId)
                                .paymentId(paymentId)
                                .build();

                OrderSnapShotMileage savedOrderSnapShotMileage =
                        orderSnapShotMileageRepository.save(orderSnapShotMileage);
                List<OrderSnapShotMileageDetail> orderSnapShotMileageDetails =
                        mileageManageService.distributeUsedMileage(
                                savedOrderSnapShotMileage.getId(),
                                usedMileages,
                                usedMileageForOrder.doubleValue());

                double sum =
                        orderSnapShotMileageDetails.stream()
                                .mapToDouble(OrderSnapShotMileageDetail::getUsedAmount)
                                .sum();
                order.setRealOrderAmount(sum);
                toSaveOrderSnapShotMileageDetails.addAll(orderSnapShotMileageDetails);

                for (OrderSnapShotMileageDetail detail : orderSnapShotMileageDetails) {
                    MileageDto mileage = mileageMap.get(detail.getMileageId());
                    if (mileage != null) {
                        mileageHistories.add(
                                mileageMapper.toMileageHistory(
                                        userId,
                                        orderId,
                                        Reason.USE,
                                        MileageIssueType.ORDER,
                                        mileage));
                    }
                }
            }

            orderSnapShotMileageDetailJdbcRepository.saveAll(toSaveOrderSnapShotMileageDetails);
            createMileageHistories(mileageHistories);
        }
    }

    private void saveMileageTransaction(
            long userId,
            long paymentAmount,
            Optional<PendingMileageQueryDto> pendingMileageQueryOpt) {
        if (paymentAmount > 0 && pendingMileageQueryOpt.isPresent()) {
            mileageSaveQueryService.saveOrderExpectedMileage(
                    userId, paymentAmount, pendingMileageQueryOpt.get());
        }
    }
}
