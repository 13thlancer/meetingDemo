package com.v4ward.core.aop;

import com.v4ward.core.model.Manager;
import io.swagger.annotations.ApiOperation;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.util.Arrays;

import static com.v4ward.core.kit.HttpKit.getSession;

/**
 * @ClassName: ControllerAspect
 * @Description: 定义controller层切面及相应通知
 * @author: Zhousheng
 * @date: 2017年4月24日 下午3:35:13
 */
@Aspect
@Component
public class ControllerLogAspect {

    
	private static Logger logger = LoggerFactory.getLogger(ControllerLogAspect.class);

    //Controller层切点    
    @Pointcut("execution(* com.whxx..controller..*.*(..))")
     public  void controllerAspect() {    
    	
    }

     /**
      *    
      * @Title: doAfterReturning 
      * @Description: 后置通知 用于拦截Controller层，并记录用户的操作  
      * @param joinPoint
      * @param ret
      * @return: void
      */
      @AfterReturning(value = "controllerAspect()" , returning = "ret")
      public  void doAfterReturning(JoinPoint joinPoint , Object ret) {

          try {
              HttpSession session = getSession();
              Manager manager  = (Manager) session.getAttribute("manager");
             HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
              Signature sig = joinPoint.getSignature();
              String methodName = sig.getName();
              MethodSignature msig = (MethodSignature) sig;
              Class[] parameterTypes = msig.getMethod().getParameterTypes();
              Method method = joinPoint.getTarget().getClass().getMethod(methodName, parameterTypes);
              boolean anoSign =  method.isAnnotationPresent(ApiOperation.class);


              logger.info("--------------controller后置通知开始----------------------");
              logger.info("URL : " + request.getRequestURL().toString());
              logger.info("HTTP请求方法 : " + request.getMethod());
              logger.info("IP : " + request.getRemoteAddr());

              if(manager != null){
                  String currentUserName = manager.getName();
                  logger.info("当前操作人员 : " + currentUserName);
              }


              if(anoSign){
                 ApiOperation apiOperation = method.getAnnotation(ApiOperation.class);
                 String apiValue = apiOperation.value();
                 String apiNotes = apiOperation.notes();

               logger.info("Swagger注解：ApiOperation-value:" + apiValue);
               logger.info("Swagger注解：ApiOperation-notes:" + apiNotes);

               }
              logger.info("当前执行方法:" + (joinPoint.getTarget().getClass().getName() + "." + sig.getName() + "()"));
              logger.info("请求参数 : " + Arrays.toString(joinPoint.getArgs()));
              logger.info("当前方法返回值 : " + ret );
              logger.info("--------------controller后置通知结束----------------------");

         }  catch (Exception ex) {
             //记录本地异常日志
              logger.error("--------------controller后置通知异常开始----------------------");
              logger.error("异常类型:" + ex.getClass().getName());
              logger.error("异常信息:", ex.getMessage());
              logger.error("详细的异常堆栈:", ex);
              logger.error("--------------controller后置通知异常结束----------------------");

         }
     }

       /**
        *     
        * @Title: doAfterThrowing 
        * @Description: 异常抛出通知 用于拦截controller层记录异常日志 
        * @param joinPoint
        * @param e
        * @return: void
        */
       @AfterThrowing(pointcut = "controllerAspect()", throwing = "e")
       public void doAfterThrowing(JoinPoint joinPoint, Throwable e) {

          //获取用户请求方法的参数并序列化为JSON格式字符串

           try {
               HttpSession session = getSession();
               Manager manager  = (Manager) session.getAttribute("manager");
               HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
               Signature sig = joinPoint.getSignature();
               String methodName = sig.getName();
               MethodSignature msig = (MethodSignature) sig;
               Class[] parameterTypes = msig.getMethod().getParameterTypes();
               Method method = joinPoint.getTarget().getClass().getMethod(methodName, parameterTypes);
               boolean anoSign =  method.isAnnotationPresent(ApiOperation.class);
               String currentUserName = manager.getName();

              logger.error("--------------controller异常抛出通知开始----------------------");
              logger.error("URL : " + request.getRequestURL().toString());
              logger.error("HTTP请求方法 : " + request.getMethod());
              logger.error("IP : " + request.getRemoteAddr());
              logger.error("当前操作人员 : " + currentUserName);
              if(anoSign){
                  ApiOperation apiOperation = method.getAnnotation(ApiOperation.class);
                  String apiValue = apiOperation.value();
                  String apiNotes = apiOperation.notes();

                  logger.error("Swagger注解：ApiOperation-value:" + apiValue);
                  logger.error("Swagger注解：ApiOperation-notes:" + apiNotes);

                  }
              logger.error("异常方法:" + (joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()"));
              logger.error("异常请求参数 : " + Arrays.toString(joinPoint.getArgs()));
              logger.error("异常类型:" + e.getClass().getName());
              logger.error("异常信息:" + e.getMessage());//获取异常的消息字符串，无堆栈信息
              logger.error("详细的异常堆栈:", e);
              logger.error("--------------controller异常抛出通知结束----------------------");  
         
    
           
          }  catch (Exception ex) {    
              //记录本地异常日志    

              logger.error("--------------controller异常抛出通知异常开始----------------------");
              logger.error("异常类型:" + ex.getClass().getName());
              logger.error("异常信息:", ex.getMessage());
              logger.error("详细的异常堆栈:", ex);
              logger.error("--------------controller异常抛出通知异常结束----------------------");

          }    
             
      
      } 
       

     /**
      *
      * @Title: getControllerMethodDescription
      * @Description: 一种获取Controller上的注解描述的示例方法
      * @param joinPoint
      * @throws Exception
      * @return: String
      */
     public  static String getControllerMethodDescription(JoinPoint joinPoint)  throws Exception {

        //拦截的实体类，即当前正在执行的Conroller
        Class target = joinPoint.getTarget().getClass();
        System.out.println("1----"+target);

        //Conroller的名字
        String targetName = target.getName();
        System.out.println("2----"+targetName);

        //拦截的方法，当前正在执行的方法
        Signature sig =joinPoint.getSignature();
        System.out.println("3----"+sig);

        //当前正在执行的方法的名称
        String methodName = sig.getName();
        System.out.println("4----"+methodName);

        //拦截的方法参数值
        Object[] arguments = joinPoint.getArgs();
        System.out.println("5----"+Arrays.toString(arguments));

        //当前类中的所有方法
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        System.out.println("6----"+Arrays.toString(methods));

        String description = "";
         for (Method method : methods) {
             if (method.getName().equals(methodName)) {
                //当前方法参数的类型
                Class[] params = method.getParameterTypes();
                 if (params.length == arguments.length && method.isAnnotationPresent(ApiOperation.class)) {
                    description = method.getAnnotation(ApiOperation.class).value();
                     break;
                }
            }
        }
         return description;
    }

}
