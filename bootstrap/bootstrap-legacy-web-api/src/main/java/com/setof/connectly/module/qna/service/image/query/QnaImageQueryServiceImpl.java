package com.setof.connectly.module.qna.service.image.query;


import com.setof.connectly.module.exception.qna.QnaImagesExceedException;
import com.setof.connectly.module.qna.dto.image.CreateQnaImage;
import com.setof.connectly.module.qna.entity.QnaImage;
import com.setof.connectly.module.qna.enums.QnaIssueType;
import com.setof.connectly.module.qna.mapper.QnaMapper;
import com.setof.connectly.module.qna.repository.image.query.QnaImageJdbcRepository;
import com.setof.connectly.module.qna.service.image.fetch.QnaImageFindService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
public class QnaImageQueryServiceImpl implements QnaImageQueryService{

    private final QnaImageFindService qnaImageFindService;
    private final QnaImageJdbcRepository qnaImageJdbcRepository;
    private final QnaMapper qnaMapper;

    @Override
    public void updateQnaImages(long qnaId, Long qnaAnswerId, List<CreateQnaImage> images, QnaIssueType qnaIssueType) {
        List<QnaImage> qnaImages = qnaImageFindService.fetchQnaImageEntitiesByQnaId(qnaId);
        processQnaImages(qnaId, qnaAnswerId, images, qnaImages, qnaIssueType);
    }



    @Override
    public void saveQnaImages(long qnaId, Long qnaAnswerId, List<CreateQnaImage> images, QnaIssueType qnaIssueType){
        CompletableFuture<List<QnaImage>> listCompletableFuture = qnaMapper.toQnaImageEntities(qnaId, qnaAnswerId, images, qnaIssueType);
        List<QnaImage> qnaImages = listCompletableFuture.join();
        qnaImageJdbcRepository.saveAll(qnaImages);
    }



    private void processQnaImages(long qnaId, Long qnaAnswerId, List<CreateQnaImage> images, List<QnaImage> existingQnaImages, QnaIssueType qnaIssueType){
        Map<Long, QnaImage> qnaImageMap = existingQnaImages.stream()
                .collect(Collectors.toMap(QnaImage::getId, Function.identity()));

        List<QnaImage> imagesToUpdate = new ArrayList<>();
        List<QnaImage> imagesToAdd = new ArrayList<>();

        Set<Long> existingImageIds = new HashSet<>(qnaImageMap.keySet());

        for (CreateQnaImage image : images) {
            Long qnaImageId = image.getQnaImageId();
            existingImageIds.remove(qnaImageId);

            if (qnaImageId == null) {
                imagesToAdd.add(createQnaImageEntity(qnaId, qnaAnswerId, image, qnaIssueType));
            } else {
                QnaImage existingImage = qnaImageMap.get(qnaImageId);
                if (existingImage != null && (imageNeedsUpdate(existingImage, image))) {
                    imagesToUpdate.add(createQnaImageEntity(qnaId, qnaAnswerId, image, qnaIssueType));
                }
            }
        }

        if(imagesToUpdate.size() + imagesToAdd.size() > 3) throw new QnaImagesExceedException();

        List<Long> imagesToDelete = new ArrayList<>(existingImageIds);

        if (!imagesToAdd.isEmpty()) {
            qnaImageJdbcRepository.saveAll(imagesToAdd);
        }

        if (!imagesToUpdate.isEmpty()) {
            qnaImageJdbcRepository.updateAll(imagesToUpdate);
        }

        if (!imagesToDelete.isEmpty()) {
            qnaImageJdbcRepository.deleteAll(imagesToDelete);
        }

    }

    private QnaImage createQnaImageEntity(long qnaId, long qnaAnswerId, CreateQnaImage createQnaImage, QnaIssueType qnaIssueType) {
        return qnaMapper.toQnaImageEntity(qnaId, qnaAnswerId, createQnaImage, qnaIssueType);
    }

    private boolean imageNeedsUpdate(QnaImage existingImage, CreateQnaImage newImage) {
        return !existingImage.getImageUrl().equals(newImage.getImageUrl())
                || existingImage.getDisplayOrder() != newImage.getDisplayOrder();
    }

}
