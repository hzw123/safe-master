package cn.mauth.safe.gateway.aspect;


import cn.mauth.safe.gateway.annotation.Api;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(1)
public class Log {

    private final Logger logger= LoggerFactory.getLogger(this.getClass());

    @Before("@annotation(api)")
    public void before(JoinPoint joinpoint, Api api){
        String method=joinpoint.getSignature().getName();
        logger.info("method:"+method+",api-value:"+api.value());
    }
}
