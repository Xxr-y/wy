package com.fh.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.fh.common.Ignore;
import com.fh.common.LoginException;
import com.fh.member.model.Member;
import com.fh.util.JwtUtil;
import com.fh.util.RedisUtil;
import com.fh.util.SystemConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.net.URLDecoder;

public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // System.out.println("=================拦截器===================");
        // 处理客户端传过来的自定义头信息
        response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS,"x-auth");
        // 处理客户端传过来的put,delete等请求方法
        response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS,"PUT,DELETE,POST,GET");

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        // 判断该方法是否需要拦截  如果方法上加了@Ignore注解就放开
        if(method.isAnnotationPresent(Ignore.class)){
            return true;
        }

        //如果没有@Ignore注解,获取请求头信息里面的token
        String token = request.getHeader("x-auth");
        //如果token为空,则重新跳转到登录页面
        if(StringUtils.isEmpty(token)){
            throw new LoginException();
        }


        // 验证token是否失效
        boolean exist = RedisUtil.exist(SystemConstant.TOKEN_KEY+token);
        if(!exist){
            // 如果exist为空,token失效
            throw new LoginException();
        }


        // 验证token
        boolean verify = JwtUtil.verify(token);
        // 如果token不为空
        if(verify){
            String userString = JwtUtil.getUser(token);
            //解密
            String jsonUser = URLDecoder.decode(userString, "utf-8");
            Member member = JSONObject.parseObject(jsonUser, Member.class);
            request.getSession().setAttribute(SystemConstant.SESSION_KEY,member);
            request.getSession().setAttribute(SystemConstant.TOKEN_KEY,token);
        }else {
            throw new LoginException();
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
