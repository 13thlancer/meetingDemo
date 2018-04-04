package com.v4ward.core.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 *
 * @ClassName: ControllerAspect
 * @Description: 定义service层切面及相应通知
 * @author: Zhousheng
 * @date: 2017年4月24日 下午3:35:13
 */
@Aspect
@Component
public class ServiceLogAspect {

    
	private static Logger logger = LoggerFactory.getLogger(ServiceLogAspect.class);
	
	
	
	//Service层切点    
    @Pointcut("execution(* com.whxx..service..*.*(..))")
     public  void serviceAspect() {    
    }    
    
    @AfterReturning(value = "serviceAspect()" , returning = "ret")
    public void doAfterReturning(JoinPoint joinPoint , Object ret) {
   

        try {

            Signature sig = joinPoint.getSignature();

            logger.info("--------------service后置通知开始----------------------");
            logger.info("当前执行方法:" + (joinPoint.getTarget().getClass().getName() + "." + sig.getName() + "()"));
            logger.info("请求参数 : " + Arrays.toString(joinPoint.getArgs()));
            logger.info("当前方法返回值 : " + ret );
            logger.info("--------------service后置通知结束----------------------");

       }  catch (Exception ex) {
           //记录本地异常日志
            logger.error("--------------service后置通知异常开始----------------------");
            logger.error("异常类型:" + ex.getClass().getName());
            logger.error("异常信息:", ex.getMessage());
            logger.error("详细的异常堆栈:", ex);
            logger.error("--------------service后置通知异常结束----------------------");

       }
   }

    
    
    
    /**  
     * 异常抛出通知 用于拦截service层记录异常日志  
     *  
     * @param joinPoint  
     * @param e  
     */    
     @AfterThrowing(pointcut = "serviceAspect()", throwing = "e")
     public void doAfterThrowing(JoinPoint joinPoint, Throwable e) {
        
        //获取用户请求方法的参数并序列化为JSON格式字符串    
           
         try {    
             
        	
        	logger.error("--------------service异常抛出通知开始----------------------");    
            
            logger.error("异常方法:" + (joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()"));
            logger.error("请求参数 : " + Arrays.toString(joinPoint.getArgs()));
            logger.error("异常类型:" + e.getClass().getName());
            logger.error("异常信息:" + e.getMessage());
            logger.error("详细的异常堆栈:", e);
            logger.error("--------------service异常抛出通知结束----------------------");  
       
 
           
               
            
         
        }  catch (Exception ex) {    
            
        	logger.error("--------------service异常抛出通知异常开始----------------------");
            logger.error("异常类型:" + ex.getClass().getName());
            logger.error("异常信息:", ex.getMessage());
            logger.error("详细的异常堆栈:", ex);
            logger.error("--------------service异常抛出通知异常结束----------------------");
        }


    }



     /**
      *
      * @Title: getServiceMthodDescription
      * @Description: 若service层有自定义注解,一种获取自定义注解的方法
      * @param joinPoint
      * @throws Exception
      * @return: String
      */
     private static String getServiceMthodDescription(JoinPoint joinPoint)
             throws Exception {
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        String description = "";
         for (Method method : methods) {
             if (method.getName().equals(methodName)) {
                Class[] clazzs = method.getParameterTypes();
                 if (clazzs.length == arguments.length) {
                   /* description = method.getAnnotation(SystemServiceLog. class).description(); */
                     break;
                }
            }
        }
         return description;
    }



}
