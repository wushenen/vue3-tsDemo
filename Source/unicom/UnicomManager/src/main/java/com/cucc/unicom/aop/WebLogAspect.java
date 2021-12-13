package com.cucc.unicom.aop;

import com.cucc.unicom.component.util.NetworkUtil;
import com.cucc.unicom.pojo.DeviceUser;
import com.cucc.unicom.pojo.OperateLog;
import com.cucc.unicom.pojo.User;
import com.cucc.unicom.service.OperateLogService;
import com.cucc.unicom.pojo.AppUser;
import com.cucc.unicom.component.annotation.OperateLogAnno;
import com.cucc.unicom.shiro.ShiroUser;
import com.cucc.unicom.shiro.UserType;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Aspect
@Component
public class WebLogAspect {

    private final Logger logger = LoggerFactory.getLogger(WebLogAspect.class);
    @Autowired
    OperateLogService operateLogService;


    @Pointcut("execution(public * com.qtec.unicom.service.impl..*.*(..))")//切入点描述 这个是包的切入点
    public void controllerLog1(){}//签名，可以理解成这个切入点的一个名称
    @Pointcut("!execution(public * com.qtec.unicom.service.impl..*.getAllIps(..))")//切入点描述 这个是包的切入点
    public void controllerLog2(){}
    @Pointcut("!execution(public * com.qtec.unicom.service.impl..*.getQemsConfig(..))")//切入点描述 这个是包的切入点
    public void controllerLog3(){}
    @Pointcut("controllerLog1() && controllerLog2() && controllerLog3()")//切入点描述 这个是包的切入点
    public void controllerLog(){}//签名，可以理解成这个切入点的一个名称


    @Around("controllerLog()") //在切入点的方法run之前要干的controllerLog() || uiControllerLog()
    public Object logBeforeController(ProceedingJoinPoint joinPoint) throws Throwable{
//        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();//这个RequestContextHolder是Springmvc提供来获得请求的东西

        Object result = null;
        Map<String, String> methodDesc = getMethodDesc(joinPoint);
        if (methodDesc.get("IS_NEED_SAVE").equals("true")) {
            //TODO 持久化
//            HttpServletRequest request = ((ServletRequestAttributes)requestAttributes).getRequest();
            OperateLog operateLog = new OperateLog();
            /*operateLog.setOperateModel(methodDesc.get("operateModel"));
            operateLog.setDetail(methodDesc.get("operateDesc"));
            ShiroUser user = (ShiroUser)SecurityUtils.getSubject().getPrincipal();
            if (UserType.SYSTEM_USER.getUserType().equals(user.getUserType())) {
                User userInfo = (User) user.getUser();
                if(userInfo != null)
                    operateLog.setOperator(userInfo.getUserName());
            }else if (UserType.APP_USER.getUserType().equals(user.getUserType())){
                AppUser userInfo = (AppUser) user.getUser();
                if(userInfo != null)
                    operateLog.setOperator(userInfo.getUserName());
            }else if (UserType.DEVICE_USER.getUserType().equals(user.getUserType())){
                DeviceUser userInfo = (DeviceUser) user.getUser();
                if(userInfo != null)
                    operateLog.setOperator(userInfo.getDeviceName());
            }

            String ip = NetworkUtil.getIpAddress(request);
            operateLog.setOperateIp(ip);*/
            long start = System.currentTimeMillis();
            result = joinPoint.proceed();
            operateLog.setExecTime(System.currentTimeMillis() - start);
            operateLog.setOperateStatus(0);
            operateLogService.insertOperateLog(getOperateLog(operateLog,methodDesc));
            logger.info("保存日志" + operateLog);
            System.out.println(result);
        } else {
            //调用目标对象方法
            result = joinPoint.proceed();
        }
        return result;
    }

    @AfterThrowing(pointcut = "controllerLog()", throwing = "ex")
    public void doAfterThrowing(JoinPoint joinPoint, Exception ex) throws Exception {
        String methodName = joinPoint.getSignature().getName();
        List<Object> args = Arrays.asList(joinPoint.getArgs());
        System.out.println("连接点方法为：" + methodName + ",参数为：" + args + ",异常为：" + ex);
        Map<String, String> methodDesc = getMethodDesc(joinPoint);
        OperateLog operateLog = new OperateLog();
        /*HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String ip = NetworkUtil.getIpAddress(request);
        operateLog.setOperateIp(ip);
        ShiroUser user = (ShiroUser)SecurityUtils.getSubject().getPrincipal();
        if (UserType.SYSTEM_USER.getUserType().equals(user.getUserType())) {
            User userInfo = (User) user.getUser();
            if(userInfo != null)
                operateLog.setOperator(userInfo.getUserName());
        }else if (UserType.APP_USER.getUserType().equals(user.getUserType())){
            AppUser userInfo = (AppUser) user.getUser();
            if(userInfo != null)
                operateLog.setOperator(userInfo.getUserName());
        }else if (UserType.DEVICE_USER.getUserType().equals(user.getUserType())){
            DeviceUser userInfo = (DeviceUser) user.getUser();
            if(userInfo != null)
                operateLog.setOperator(userInfo.getDeviceName());
        }*/
        operateLog.setOperateStatus(1);
/*        operateLog.setOperateModel(methodDesc.get("operateModel"));
        operateLog.setDetail(methodDesc.get("operateDesc") + "[操作失败]");*/
        operateLogService.insertOperateLog(getOperateLog(operateLog,methodDesc));
        logger.error("失败日志记录：" + operateLog);
    }
    private Map<String, String> getMethodDesc(JoinPoint joinPoint) throws NoSuchMethodException {
        Map<String, String> methodDetails = new HashMap<>();
        methodDetails.put("operateDesc", "默认操作");
        methodDetails.put("operateModel", "基础模块");
        methodDetails.put("IS_NEED_SAVE", "false");
        Signature signature = joinPoint.getSignature();
        if (!(signature instanceof MethodSignature)) {
            return methodDetails;
        }
        MethodSignature msig = (MethodSignature) signature;
        Object target = joinPoint.getTarget();
        Method method = target.getClass().getMethod(msig.getName(), msig.getParameterTypes());
        if (method.isAnnotationPresent(OperateLogAnno.class)) {
            methodDetails.put("operateDesc", method.getAnnotation(OperateLogAnno.class).operateDesc());
            methodDetails.put("operateModel", method.getAnnotation(OperateLogAnno.class).operateModel());
            methodDetails.put("IS_NEED_SAVE", "true");
        }
        return methodDetails;
    }

    private OperateLog getOperateLog(OperateLog operateLog,Map<String,String> map) throws IOException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String ip = NetworkUtil.getIpAddress(request);
        operateLog.setOperateIp(ip);
        ShiroUser user = (ShiroUser)SecurityUtils.getSubject().getPrincipal();
        if (UserType.SYSTEM_USER.getUserType().equals(user.getUserType())) {
            User userInfo = (User) user.getUser();
            if(userInfo != null)
                operateLog.setOperator(userInfo.getUserName());
        }else if (UserType.APP_USER.getUserType().equals(user.getUserType())){
            AppUser userInfo = (AppUser) user.getUser();
            if(userInfo != null)
                operateLog.setOperator(userInfo.getUserName());
        }else if (UserType.DEVICE_USER.getUserType().equals(user.getUserType())){
            DeviceUser userInfo = (DeviceUser) user.getUser();
            if(userInfo != null)
                operateLog.setOperator(userInfo.getDeviceName());
        }else
            operateLog.setOperator("unknown");
        operateLog.setOperateModel(map.get("operateModel"));
        if (operateLog.getOperateStatus().equals(0))
            operateLog.setDetail(map.get("operateDesc"));
        else
            operateLog.setDetail(map.get("operateDesc") + "[操作失败]");
        return operateLog;
    }

}




