package com.connectly.partnerAdmin.module.external.aop;


import com.connectly.partnerAdmin.module.common.exception.UnExpectedException;
import com.connectly.partnerAdmin.module.external.annotation.RequiresAccessToken;
import com.connectly.partnerAdmin.module.external.exception.ExMallForbiddenException;
import com.connectly.partnerAdmin.module.external.service.auth.ExternalSiteTokenProvider;
import com.connectly.partnerAdmin.module.external.service.auth.ExternalSiteTokenService;
import com.connectly.partnerAdmin.module.order.enums.SiteName;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Aspect
@Component
@RequiredArgsConstructor
public class GetAccessTokenAop {

    private static final String TOKEN_ERROR_MSG = "Failed to token after retryCount attempts";
    private static final int MAX_RETRY_COUNT = 4;

    private final ExternalSiteTokenProvider externalSiteTokenProvider;
    public static ConcurrentHashMap<SiteName, String> accessTokenMap = new ConcurrentHashMap<>();


    @Pointcut(value = "@annotation(com.connectly.partnerAdmin.module.external.annotation.RequiresAccessToken)")
    public void RequiresAccessToken(){}



    @Around("RequiresAccessToken() && @annotation(requiresAccessToken)")
    public Object checkAccessToken(ProceedingJoinPoint joinPoint, RequiresAccessToken requiresAccessToken) throws Throwable {
        Object proceed = null;
        SiteName siteName = requiresAccessToken.siteName();

        int retryCount = 0;
        while(true) {
            try {
                proceed = joinPoint.proceed();
                break;
            } catch (ExMallForbiddenException | FeignException e) {
                if(retryCount < MAX_RETRY_COUNT) {
                    ExternalSiteTokenService externalSiteTokenService = externalSiteTokenProvider.get(siteName);
                    String token = externalSiteTokenService.getToken();
                    accessTokenMap.put(siteName, token);
                    retryCount++;
                }
                else {
                    throw new UnExpectedException(TOKEN_ERROR_MSG + retryCount);
                }
            } catch (Exception e){
                break;
            }
        }
        return proceed;
    }

}
