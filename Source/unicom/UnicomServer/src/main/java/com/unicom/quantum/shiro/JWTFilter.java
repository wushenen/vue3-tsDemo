package com.unicom.quantum.shiro;

import com.unicom.quantum.component.util.JWTUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public class JWTFilter extends BasicHttpAuthenticationFilter {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 如果带有 token，则对 token 进行检查，否则直接通过
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws UnauthorizedException {
        if (isLoginAttempt(request, response)) {
            try {
                boolean flag = executeLogin(request, response);
                if(!flag){
                    HttpServletResponse re = (HttpServletResponse)response;
                    logger.info("executeLogin is false......");
                    re.getWriter().write("{\"code\":401}");
                    return false;
                }
                ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
                if(shiroUser == null){
                    HttpServletResponse re = (HttpServletResponse)response;
                    logger.info("user is null......");
                    re.getWriter().write("{\"code\":401}");
                    re.getOutputStream();
                    flag =false;
                }
                return flag;
            } catch (Exception e) {
                responseError(response);
            }
        }
        try {
            logger.info("no Token......");
            response.getWriter().write("{\"code\":401}");
        }catch(IOException e){
           e.printStackTrace();
        }
        return false;
    }

    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        HttpServletRequest req = (HttpServletRequest) request;
        String token = req.getHeader("Token");
        return token != null;
    }

    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String token = httpServletRequest.getHeader("Token");
        String username = JWTUtil.getUsername(token);
        boolean flag = JWTUtil.verifyToken(token, username);
        Date expiresAt = JWTUtil.getExpiresAt(token);
        if ((expiresAt.getTime() - System.currentTimeMillis() )< (1000*30*60)) {
            token = JWTUtil.generateToken(username);
            HttpServletResponse res = (HttpServletResponse) response;
            res.setHeader("Token",token);
        }
        return flag;
    }


    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        boolean loggedIn = false;
        if (isLoginAttempt(request, response)) {
            loggedIn = executeLogin(request, response);
        }
        if (!loggedIn) {
            HttpServletResponse httpResponse = WebUtils.toHttp(response);
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        return loggedIn;
    }

    private void responseError(ServletResponse response) {
        try {
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            httpServletResponse.sendRedirect("/login/deviceLogin");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
