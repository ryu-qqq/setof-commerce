package com.connectly.partnerAdmin.module.qna.dto.fetch;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class DetailQnaResponse {
    private FetchQnaResponse qna;
    private Set<AnswerQnaResponse> answerQnas;

    @JsonIgnore
    private List<QnaImageDto> qnaImages;

    @JsonIgnore
    private List<QnaImageDto> qnaAnswerImages;


    @QueryProjection
    public DetailQnaResponse(FetchQnaResponse qna, Set<AnswerQnaResponse> answerQnas) {
        this.qna = qna;
        setAnswerQnas(answerQnas);
    }

    @QueryProjection
    public DetailQnaResponse(FetchQnaResponse qna, Set<AnswerQnaResponse> answerQnas, List<QnaImageDto> qnaImages, List<QnaImageDto> qnaAnswerImages) {
        this.qna = qna;
        setAnswerQnas(answerQnas);
        if (qnaImages != null || qnaAnswerImages != null) {
            setQnaImages(qnaImages, qnaAnswerImages);
        }
    }

    private void setAnswerQnas(Set<AnswerQnaResponse> answerQnas){
        if(answerQnas.isEmpty()){
            this.answerQnas = new HashSet<>();
        }

        else if(answerQnas.size() == 1 && answerQnas.stream().findAny().get().getQnaAnswerId() ==0){
            this.answerQnas = new HashSet<>();

        }
        else{
            this.answerQnas = answerQnas;
        }

    }

    private void setQnaImages(List<QnaImageDto> qnaImages, List<QnaImageDto> qnaAnswerImages) {
        this.qnaImages = qnaImages;
        this.qnaAnswerImages = qnaAnswerImages;

        if (qna.getQnaType().isOrderQna()) {
            processQnaImagesQna(qnaImages);
            processQnaImagesAnswer(qnaAnswerImages);
        }
    }

    private void processQnaImagesQna(List<QnaImageDto> images) {
        Map<Long, List<QnaImageDto>> groupedImages = images.stream()
                .filter(qnaImageDto -> qnaImageDto.getQnaId() != null)
                .collect(Collectors.groupingBy(QnaImageDto::getQnaId));

        setQuestionImage(groupedImages);

    }

    private void processQnaImagesAnswer(List<QnaImageDto> images) {
        Map<Long, List<QnaImageDto>> groupedImages = images.stream()
                .filter(qnaImageDto -> qnaImageDto.getQnaAnswerId() != null)
                .collect(Collectors.groupingBy(QnaImageDto::getQnaAnswerId));

        setAnswerImage(groupedImages);

    }

    private void setQuestionImage(Map<Long, List<QnaImageDto>> qnaIdMap) {
        long qnaId = this.qna.getQnaId();
        qna.setQnaImages(qnaIdMap.getOrDefault(qnaId, new ArrayList<>()));
    }

    private void setAnswerImage(Map<Long, List<QnaImageDto>> qnaAnswerIdMap) {
        answerQnas.forEach(answer -> answer.setQnaImages(qnaAnswerIdMap.getOrDefault(answer.getQnaAnswerId(), new ArrayList<>())));
    }


}
