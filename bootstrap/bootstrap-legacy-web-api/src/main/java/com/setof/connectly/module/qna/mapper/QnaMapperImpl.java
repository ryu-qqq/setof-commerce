package com.setof.connectly.module.qna.mapper;


import com.setof.connectly.module.common.enums.UserType;
import com.setof.connectly.module.common.enums.Yn;
import com.setof.connectly.module.image.enums.ImagePath;
import com.setof.connectly.module.image.service.ImageUploadService;
import com.setof.connectly.module.qna.dto.QnaResponse;
import com.setof.connectly.module.qna.dto.image.CreateQnaImage;
import com.setof.connectly.module.qna.dto.image.QnaImageDto;
import com.setof.connectly.module.qna.dto.query.CreateQna;
import com.setof.connectly.module.qna.entity.Qna;
import com.setof.connectly.module.qna.entity.QnaAnswer;
import com.setof.connectly.module.qna.entity.QnaImage;
import com.setof.connectly.module.qna.enums.QnaIssueType;
import com.setof.connectly.module.qna.enums.QnaStatus;
import com.setof.connectly.module.qna.enums.QnaWriterType;
import com.setof.connectly.module.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class QnaMapperImpl implements QnaMapper{

    private final ImageUploadService imageUploadService;

    @Override
    public List<QnaResponse> toQnaList(List<QnaResponse> qnaResponses) {
        qnaResponses.forEach(
                qna -> {
                    if(isRestrictContent(qna.getQna().getUserId())){
                        String maskedName = maskName(qna.getQna().getUserName());
                        qna.getQna().setUserName(maskedName);
                        if(qna.getQna().isPrivateQna()){
                            qna.getQna().restrictContent();
                        }
                    }
                }
        );

        return qnaResponses;
    }

    @Override
    public List<QnaResponse> toQnaResponse(List<QnaResponse> qnaResponses, List<QnaImage> qnaImages) {
        qnaResponses.forEach(qnaResponse -> {
            isOwnQna(qnaResponse);
            if(qnaResponse.getQna().getQnaType().isOrderQna()){
                Map<QnaIssueType, List<QnaImage>> questionTypeMap = qnaImages.stream().collect(Collectors.groupingBy(QnaImage::getQnaIssueType));
                questionTypeMap.forEach(
                        (qnaIssueType, images) -> {

                            Map<Long, List<QnaImage>> qnaIdMap = images.stream()
                                    .filter(qnaImage -> qnaImage.getQnaId() >0)
                                    .collect(Collectors.groupingBy(QnaImage::getQnaId));

                            Map<Long, List<QnaImage>> qnaAnswerIdMap = images.stream()
                                    .filter(qnaImage -> qnaImage.getQnaAnswerId() != null)
                                    .collect(Collectors.groupingBy(QnaImage::getQnaAnswerId));

                            if(qnaIssueType.isQuestion()) setQuestionImage(qnaResponse, qnaIdMap);
                            else setAnswerImage(qnaResponse, qnaAnswerIdMap);
                        });
            }


        });
        return qnaResponses;
    }

    @Override
    public Qna toEntity(CreateQna createQna) {
        return Qna.builder()
                .qnaContents(createQna.getQnaContents())
                .qnaStatus(QnaStatus.OPEN)
                .privateYn(createQna.isPrivate() ? Yn.Y : Yn.N)
                .qnaType(createQna.getQnaType())
                .qnaDetailType(createQna.getQnaDetailType())
                .userId(SecurityUtils.currentUserId())
                .userType(UserType.MEMBERS)
                .sellerId(createQna.getSellerId())
                .build();

    }

    @Override
    public QnaAnswer toQnaAnswerEntity(long qnaId, CreateQna createQna, Optional<Long> lastQnaAnswerId) {
        return QnaAnswer.builder()
                .qnaId(qnaId)
                .qnaWriterType(QnaWriterType.CUSTOMER)
                .qnaContents(createQna.getQnaContents())
                .qnaStatus(QnaStatus.OPEN)
                .qnaParentId(lastQnaAnswerId.orElse(null))
                .build();
    }

    @Override
    public QnaImage toQnaImageEntity(long qnaId, long qnaAnswerId, CreateQnaImage createQnaImage, QnaIssueType qnaIssueType) {
        String uploadedImageUrl = imageUploadService.uploadImage(ImagePath.QNA, createQnaImage.getImageUrl()).join();
        return QnaImage.builder()
                .qnaId(qnaId)
                .qnaIssueType(qnaIssueType)
                .qnaAnswerId(qnaAnswerId)
                .imageUrl(uploadedImageUrl)
                .displayOrder(createQnaImage.getDisplayOrder())
                .build();
    }

    @Override
    public CompletableFuture<List<QnaImage>> toQnaImageEntities(long qnaId, Long qnaAnswerId, List<CreateQnaImage> images, QnaIssueType qnaIssueType) {
        List<CompletableFuture<QnaImage>> futures = images.stream().map(image -> {
            return imageUploadService.uploadImage(ImagePath.QNA, image.getImageUrl()).thenApply(url ->
                    new QnaImage(qnaIssueType, qnaId, qnaAnswerId, url, image.getDisplayOrder()));

        }).collect(Collectors.toList());

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> futures.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList()));
    }

    private void setQuestionImage(QnaResponse qnaResponse, Map<Long, List<QnaImage>> qnaIdMap){
        long qnaId = qnaResponse.getQna().getQnaId();
        List<QnaImage> qnaImages = qnaIdMap.getOrDefault(qnaId, new ArrayList<>());

        if(!qnaImages.isEmpty()) {
            List<QnaImageDto> qnaImageDtos = toQnaImages(qnaImages);
            qnaResponse.getQna().setQnaImages(qnaImageDtos);
        }


    }

    private void setAnswerImage(QnaResponse detailQnaResponse, Map<Long, List<QnaImage>> qnaAnswerIdMap){
        detailQnaResponse.getAnswerQnas().forEach(answerQnaResponse -> {
            long qnaAnswerId = answerQnaResponse.getQnaAnswerId();
            List<QnaImage> qnaAnswerImages = qnaAnswerIdMap.getOrDefault(qnaAnswerId, new ArrayList<>());
            if(!qnaAnswerImages.isEmpty()){
                List<QnaImageDto> qnaImageDtos = toQnaImages(qnaAnswerImages);
                answerQnaResponse.setQnaImages(qnaImageDtos);
            }
        });




    }


    private void isOwnQna(QnaResponse qnaResponse){
        if(isRestrictContent(qnaResponse.getQna().getUserId())){
            String maskedName = maskName(qnaResponse.getQna().getUserName());
            qnaResponse.getQna().setUserName(maskedName);
            if(qnaResponse.getQna().isPrivateQna()){
                qnaResponse.notPermissionReadQna();
            }
        }
    }


    private List<QnaImageDto> toQnaImages(List<QnaImage> qnaImages) {
        return qnaImages.stream()
                .map(qnaImage -> {
                    return QnaImageDto.builder()
                            .qnaIssueType(qnaImage.getQnaIssueType())
                            .qnaId(qnaImage.getQnaId())
                            .qnaAnswerId(qnaImage.getQnaAnswerId())
                            .qnaImageId(qnaImage.getId())
                            .imageUrl(qnaImage.getImageUrl())
                            .displayOrder(qnaImage.getDisplayOrder())
                            .build();
                })
                .sorted(Comparator.comparingInt(QnaImageDto::getDisplayOrder))
                .collect(Collectors.toList());
    }

    private boolean isRestrictContent(long userId){
        return userId != SecurityUtils.currentUserId();
    }

    private String maskName(String name) {
        if (name == null || name.length() < 2) {
            return name;
        }
        return name.substring(0, name.length() - 2) + "**";
    }



}
