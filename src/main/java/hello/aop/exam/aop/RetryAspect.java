package hello.aop.exam.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import hello.aop.exam.annotation.Retry;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
public class RetryAspect {

	@Around("@annotation(retry)")
	public Object doRetry(ProceedingJoinPoint joinPoint, Retry retry) throws Throwable {
		log.info("[trace] {} retry={}", joinPoint.getSignature(), retry);

		int maxTry = retry.value();
		Exception exceptionHolder = null;

		for (int retryCount = 1; retryCount <= maxTry; retryCount++) {
			try {
				log.info("[retry] try count ={}/{}", retryCount, maxTry);
				return joinPoint.proceed();
			} catch (Exception e) {
				exceptionHolder = e;
			}
		}

		throw exceptionHolder;
	}

}
