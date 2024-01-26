package cn.mrcsh.zfmcserverpanelapi.aop;

import cn.mrcsh.zfmcserverpanelapi.annotation.APISupervisory;
import cn.mrcsh.zfmcserverpanelapi.config.Cache;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class APISupervisoryProxy {

    @Pointcut("@annotation(cn.mrcsh.zfmcserverpanelapi.annotation.APISupervisory)")
    public void point() {
    }

    @Around("point()")
    public Object before(ProceedingJoinPoint pjp) throws Throwable {
        Object proceed = pjp.proceed();
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Method method = methodSignature.getMethod();
        String type = method.getAnnotation(APISupervisory.class).value();
        Cache.cacheCount.put(type, Cache.cacheCount.get(type) == null ? 1 : Cache.cacheCount.get(type) + 1);
        return proceed;

    }
}
