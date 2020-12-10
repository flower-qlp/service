package com.oauth.service.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * @author happy
 */
@Component
@Aspect
public class InterFaceAspect {

    @Around("within(com.oauth.service.controller..*)")
    public Object aroundMethod(ProceedingJoinPoint proceedingJoinPoint) {

        Object result = null;
        String method = proceedingJoinPoint.getSignature().getName();
        Object[] args = proceedingJoinPoint.getArgs();
        try {
            System.out.println("接口："+method +" 参数:"+ JSON.toJSONString(args, SerializerFeature.WriteMapNullValue));
            result = proceedingJoinPoint.proceed();
            System.out.println("结果：" + JSON.toJSONString(result));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return result;
    }
}
