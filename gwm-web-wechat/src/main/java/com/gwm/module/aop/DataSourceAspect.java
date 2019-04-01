package com.gwm.module.aop;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import com.gwm.db.dao.configuration.DataSourceSwitcher;

@Aspect
@Component
public class DataSourceAspect {
	    @Pointcut("execution(* com.gwm.module..*Service.*(..))")
	    public void changeDatasource(){}
	    /**
	     * service方法执行之前被调用
	     * @param joinPoint
	     * @throws Throwable
	     */
	    @Before("changeDatasource()")
	    public void doBefore(JoinPoint joinPoint) throws Throwable {
	    	Signature signature = joinPoint.getSignature();  
	    	MethodSignature methodSignature = (MethodSignature) signature;  
	    	Method m = methodSignature.getMethod();
	        DataSourceSwitcher.switchSource(m);
	    }
	    @AfterReturning(returning = "ret", pointcut = "changeDatasource()")
	    public void doAfterReturning(Object ret) throws Throwable {
	    }
	    @AfterThrowing( "changeDatasource()")
	    public void doAfterThrowing() throws Throwable {
	    	DataSourceSwitcher.setSlave();
	    }
}
