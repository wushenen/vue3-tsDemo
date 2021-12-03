package com.qtec.unicom.shiro;

import com.qtec.unicom.component.util.JWTUtil;
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

/**
 * Created with IntelliJ IDEA
 *
 * @Author yuanhaoyue swithaoy@gmail.com
 * @Description preHandle->isAccessAllowed->isLoginAttempt->executeLogin
 * @Date 2018-04-08
 * @Time 12:36
 */
public class JWTFilter extends BasicHttpAuthenticationFilter {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 如果带有 token，则对 token 进行检查，否则直接通过
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws UnauthorizedException {
        //判断请求的请求头是否带上 "Token"
        if (isLoginAttempt(request, response)) {
            //如果存在，则进入 executeLogin 方法执行登入，检查 token 是否正确
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
                //token 错误
                responseError(response);
            }
        }
        //判断是否登陆
        ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        if(shiroUser != null){
            HttpServletRequest req = (HttpServletRequest) request;
            String s = req.getServletPath();
//            System.out.println(s);
            if(s.startsWith("/excel/export/operateLog"))
                return true;
        }

        try {
            logger.info("no Token......");
            response.getWriter().write("{\"code\":401}");
        }catch(IOException e){
           e.printStackTrace();
        }
        return false;
    }

    /**
     * 判断用户是否想要登入。
     * 检测 header 里面是否包含 Token 字段
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        HttpServletRequest req = (HttpServletRequest) request;
        String token = req.getHeader("Token");//Authorization
        return token != null;
    }

    /**
     * 执行验证操作
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String token = httpServletRequest.getHeader("Token");
        // 提交给realm进行登入，如果错误他会抛出异常并被捕获
        String username = JWTUtil.getUsername(token);
        boolean flag = JWTUtil.verifyToken(token, username);
        // 如果没有抛出异常则代表登入成功，返回true
        Date expiresAt = JWTUtil.getExpiresAt(token);
        if ((expiresAt.getTime() - System.currentTimeMillis() )< (1000*30*60)) {
            token = JWTUtil.generateToken(username);
            HttpServletResponse res = (HttpServletResponse) response;
            res.setHeader("Token",token);
        }
        return flag;
    }

    /**
     * 对跨域提供支持
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }

    /**
     * 去除WWW-Authenticate
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        boolean loggedIn = false; //false by default or we wouldn't be in this method
        if (isLoginAttempt(request, response)) {
            loggedIn = executeLogin(request, response);
        }
        if (!loggedIn) {
            HttpServletResponse httpResponse = WebUtils.toHttp(response);
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        return loggedIn;
    }
    /**
     * 将非法请求跳转到 /unauthorized/**
     */
    private void responseError(ServletResponse response) {
        try {
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            httpServletResponse.sendRedirect("/login");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public static void main(String[] args) {
        String s = "eyJhbGciOiJTTTMiLCJ0eXAiOiJKV1QifQ.eyJhdWQiOiJ0ZXN0Iiwic3ViIjoiY2lwaGVyX3NlcnZlciIsImlzcyI6InF0ZWNfUiZEIiwiaWF0IjoxNjE4NTM4MzEzfQ.g2ValHi-vpwyQt7SFS1iBFf12lm2LdMaHyR5drnHBIM";
        String username = null;
        try {
            username = JWTUtil.getUsername(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(username);
        boolean f = JWTUtil.verifyToken(s, username);
        System.out.println(f);
    }
}
