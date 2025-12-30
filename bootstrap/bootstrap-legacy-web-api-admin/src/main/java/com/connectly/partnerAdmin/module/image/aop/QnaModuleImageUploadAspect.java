package com.connectly.partnerAdmin.module.image.aop;

import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.connectly.partnerAdmin.module.image.core.BaseImageContext;
import com.connectly.partnerAdmin.module.image.enums.ImagePath;
import com.connectly.partnerAdmin.module.image.service.ImageUploadService;
import com.connectly.partnerAdmin.module.qna.dto.query.CreateQnaAnswer;
import com.connectly.partnerAdmin.module.qna.dto.query.CreateQnaImage;
import com.connectly.partnerAdmin.module.qna.dto.query.UpdateQnaAnswer;

@Aspect
@Component
public class QnaModuleImageUploadAspect extends ImageUploadAspect {

    public QnaModuleImageUploadAspect(ImageUploadService imageUploadService) {
        super(imageUploadService);
    }

    @Pointcut(value = "execution(* com.connectly.partnerAdmin.module.qna.service.QnaAnswerRegistrationServiceImpl.doAnswer(..)) && args(createQnaAnswer)", argNames = "createQnaAnswer")
    private void qnaImageUploadWhenDoAnswer(CreateQnaAnswer createQnaAnswer) {}

    @Pointcut(value = "execution(* com.connectly.partnerAdmin.module.qna.service.QnaAnswerRegistrationServiceImpl.updateAnswer(..)) && args(updateQnaAnswer)", argNames = "updateQnaAnswer")
    private void qnaImageUploadWhenUpdateAnswer(UpdateQnaAnswer updateQnaAnswer) {}

    @Around(value = "qnaImageUploadWhenDoAnswer(createQnaAnswer)", argNames = "pjp, createQnaAnswer")
    public Object uploadQnaSingle(ProceedingJoinPoint pjp, CreateQnaAnswer createQnaAnswer) throws Throwable {
        uploadImageUrl(createQnaAnswer.getQnaImages());
        return pjp.proceed();
    }

    @Around(value = "qnaImageUploadWhenUpdateAnswer(updateQnaAnswer)", argNames = "pjp, updateQnaAnswer")
    public Object uploadQnaMulti(ProceedingJoinPoint pjp, UpdateQnaAnswer updateQnaAnswer) throws Throwable {
        uploadImageUrl(updateQnaAnswer.getQnaImages());
        return pjp.proceed();
    }

    private void uploadImageUrl(List<CreateQnaImage> qnaImages){
        for(CreateQnaImage qnaImage : qnaImages){
            BaseImageContext baseImageContext = new BaseImageContext(ImagePath.QNA, qnaImage.getImageUrl());
            String uploadedImageUrl = uploadImage(baseImageContext);
            qnaImage.setImageUrl(uploadedImageUrl);
        }
    }

}
