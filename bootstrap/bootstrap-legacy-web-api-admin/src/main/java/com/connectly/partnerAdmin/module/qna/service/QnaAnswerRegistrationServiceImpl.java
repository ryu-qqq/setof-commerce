package com.connectly.partnerAdmin.module.qna.service;

import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.connectly.partnerAdmin.module.qna.dto.CreateQnaAnswerResponse;
import com.connectly.partnerAdmin.module.qna.dto.fetch.QnaImageDto;
import com.connectly.partnerAdmin.module.qna.dto.query.CreateQnaAnswer;
import com.connectly.partnerAdmin.module.qna.dto.query.CreateQnaImage;
import com.connectly.partnerAdmin.module.qna.dto.query.UpdateQnaAnswer;
import com.connectly.partnerAdmin.module.qna.entity.Qna;
import com.connectly.partnerAdmin.module.qna.entity.QnaAnswer;
import com.connectly.partnerAdmin.module.qna.entity.QnaImage;
import com.connectly.partnerAdmin.module.qna.exception.InvalidQnaException;
import com.connectly.partnerAdmin.module.qna.exception.QnaErrorConstant;
import com.connectly.partnerAdmin.module.qna.mapper.QnaMapper;
import com.connectly.partnerAdmin.module.qna.repository.QnaAnswerRepository;

@Transactional
@RequiredArgsConstructor
@Service
public class QnaAnswerRegistrationServiceImpl implements QnaAnswerRegistrationService{

    private final QnaMapper qnaMapper;
    private final QnaFetchService qnaFetchService;
    private final QnaAnswerFetchService qnaAnswerFetchService;
    private final QnaAnswerRepository qnaAnswerRepository;


    @Override
    public CreateQnaAnswerResponse doAnswer(CreateQnaAnswer createQnaAnswer) {
        Qna qna = qnaFetchService.fetchQnaEntity(createQnaAnswer.getQnaId());

        Optional<QnaAnswer> findQnaAnswerOpt = qnaAnswerFetchService.fetchLastQnaAnswerByCustomer(qna.getId());

        Optional<Long> lastQnaAnswerId = Optional.empty();

        if(findQnaAnswerOpt.isPresent()){
            QnaAnswer qnaAnswer = findQnaAnswerOpt.get();
            qnaAnswer.reply();
            lastQnaAnswerId = Optional.of(qnaAnswer.getId());
        }

        qna.reply();

        QnaAnswer qnaAnswer = qnaMapper.toQnaAnswerEntity(qna, createQnaAnswer, lastQnaAnswerId);

        QnaAnswer savedQnaAnswer = qnaAnswerRepository.save(qnaAnswer);
        return toCreateQnaAnswerResponse(savedQnaAnswer);
    }


    @Override
    public CreateQnaAnswerResponse updateAnswer(UpdateQnaAnswer updateQnaAnswer) {
        QnaAnswer qnaAnswer = qnaAnswerFetchService.fetchQnaAnswerEntity(updateQnaAnswer.getQnaAnswerId());

        if(qnaAnswer.getQnaStatus().isClosed()) throw new InvalidQnaException(QnaErrorConstant.ALREADY_CLOSING_QNA_ERROR_MSG);

        qnaAnswer.doAnswer(updateQnaAnswer.getQnaContents());

        if (!updateQnaAnswer.getQnaImages().isEmpty()) {
            updateQnaImages(qnaAnswer, updateQnaAnswer.getQnaImages());
        }

        return toCreateQnaAnswerResponse(qnaAnswer);
    }



    private void updateQnaImages(QnaAnswer qnaAnswer, List<CreateQnaImage> newImages) {
        List<QnaImage> existingImages = qnaAnswer.getQnaImages();

        Map<Long, QnaImage> existingImageMap = existingImages.stream()
                .collect(Collectors.toMap(QnaImage::getId, Function.identity()));

        Set<Long> newImageIds = new HashSet<>();

        for (CreateQnaImage newImage : newImages) {
            if (newImage.getQnaImageId() != null) {
                QnaImage existingImage = existingImageMap.get(newImage.getQnaImageId());
                if (existingImage != null) {
                    existingImage.setImageUrl(newImage.getImageUrl());
                    existingImage.setDisplayOrder(newImage.getDisplayOrder());
                    newImageIds.add(existingImage.getId());
                }
            } else {
                QnaImage qnaImage = QnaImage.builder()
                        .imageUrl(newImage.getImageUrl())
                        .displayOrder(newImage.getDisplayOrder())
                        .build();

                qnaAnswer.addQnaImages(qnaImage);
            }
        }

        List<QnaImage> imagesToDelete = existingImages.stream()
                .filter(image -> !newImageIds.contains(image.getId()))
                .toList();

        for (QnaImage imageToDelete : imagesToDelete) {
            qnaAnswer.deleteQnaImages(imageToDelete);
        }
    }



    private CreateQnaAnswerResponse toCreateQnaAnswerResponse(QnaAnswer qnaAnswer) {
        List<QnaImageDto> qnaImages = qnaAnswer.getQnaImages().stream()
                .map(qnaImage -> QnaImageDto.builder()
                        .qnaIssueType(qnaImage.getQnaIssueType())
                        .qnaImageId(qnaImage.getId())
                        .qnaId(qnaAnswer.getQna().getId())
                        .qnaAnswerId(qnaAnswer.getId())
                        .imageUrl(qnaImage.getImageUrl())
                        .displayOrder(qnaImage.getDisplayOrder())
                        .build())
                .toList();


        return CreateQnaAnswerResponse.builder()
                .qnaId(qnaAnswer.getQna().getId())
                .qnaAnswerId(qnaAnswer.getId())
                .qnaType(qnaAnswer.getQna().getQnaType())
                .qnaStatus(qnaAnswer.getQnaStatus())
                .qnaImages(qnaImages)
                .build();
    }

}
