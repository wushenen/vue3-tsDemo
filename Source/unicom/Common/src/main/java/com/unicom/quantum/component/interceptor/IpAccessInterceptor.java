package com.unicom.quantum.component.interceptor;

import com.unicom.quantum.component.init.Init;
import com.unicom.quantum.component.util.IpWhiteCheckUtil;
import com.unicom.quantum.component.util.NetworkUtil;
import com.unicom.quantum.component.util.QrngUtil;
import com.unicom.quantum.mapper.IpMapper;
import com.unicom.quantum.pojo.IpInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.util.List;


/**
 * Created by wuzh on 2020/1/15.
 * Describe: jwt形式的Token拦截器
 */

public class IpAccessInterceptor implements HandlerInterceptor {

    @Autowired
    private IpMapper ipMapper;
    private final Logger logger = LoggerFactory.getLogger(QrngUtil.class);

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean ipAccessAble = ipAccessAble(request);
        if (!ipAccessAble) {
            logger.error("客户端ip：" + NetworkUtil.getIpAddress(request) + "不在ip白名单中，不允许访问");
        }
        return ipAccessAble;
    }
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
    }
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
    }

    private boolean ipAccessAble(@NotNull HttpServletRequest request){
        if (Init.IP_WHITE_SET.contains("0.0.0.0") || "localhost".equals(NetworkUtil.getIpAddress(request)))
            return true;
        if (IpWhiteCheckUtil.isPermitted(NetworkUtil.getIpAddress(request), Init.IP_WHITE_SET)) {
            return true;
        }
        List<IpInfo> allIps = ipMapper.getAllIps();
        for (IpInfo allIp : allIps) {
            Init.IP_WHITE_SET.add(allIp.getIpInfo());
        }
        if (IpWhiteCheckUtil.isPermitted(NetworkUtil.getIpAddress(request), Init.IP_WHITE_SET)) {
            return true;
        }
        return false;
    }

}

