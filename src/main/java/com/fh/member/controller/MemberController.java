package com.fh.member.controller;

import com.fh.common.Ignore;
import com.fh.common.ServerResponse;
import com.fh.member.model.Member;
import com.fh.member.service.MemberService;
import com.fh.util.RedisUtil;
import com.fh.util.SystemConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("member")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @RequestMapping("checkMemberName")
    @Ignore
    public ServerResponse checkMemberName(String name){
        return memberService.checkMemberName(name);
    }


    @RequestMapping("checkMemberPhone")
    @Ignore
    public ServerResponse checkMemberPhone(String phone){
        return memberService.checkMemberPhone(phone);
    }



    @RequestMapping("register")
    @Ignore
    public ServerResponse register(Member member){
        return memberService.register(member);
    }


    @RequestMapping("login")
    @Ignore
    public ServerResponse login(Member member){
        return memberService.login(member);
    }


    // 验证用户是否登录
    @RequestMapping("whetherLogin")
    @Ignore
    public ServerResponse whetherLogin(HttpServletRequest request){
        Member member = (Member) request.getSession().getAttribute(SystemConstant.SESSION_KEY);
        if(member == null){
            return ServerResponse.error();
        }
        return ServerResponse.success();
    }


    @RequestMapping("out")
    @Ignore
    public ServerResponse out(HttpServletRequest request){
        // 手动设置token失效
        String token = (String) request.getSession().getAttribute(SystemConstant.TOKEN_KEY);
        RedisUtil.del(SystemConstant.TOKEN_KEY+token);
        // 清除session中用户的信息
        request.getSession().removeAttribute(SystemConstant.SESSION_KEY);
        // 清除session中的token信息
        request.getSession().removeAttribute(SystemConstant.TOKEN_KEY);
        return ServerResponse.success();
    }


}