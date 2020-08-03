package com.fh.member.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fh.common.ServerResponse;
import com.fh.member.mapper.MemberMapper;
import com.fh.member.model.Member;
import com.fh.member.service.MemberService;
import com.fh.util.JwtUtil;
import com.fh.util.RedisUtil;
import com.fh.util.SystemConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberMapper memberMapper;


    //根据用户名查询数据库中是否存在该用户
    @Override
    public ServerResponse checkMemberName(String name) {
        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name",name);
        Member member = memberMapper.selectOne(queryWrapper);
        if(member != null){
            return ServerResponse.error("用户名已存在");
        }
        return ServerResponse.success();
    }

    //根据手机号查询数据库中是否存在该手机号
    @Override
    public ServerResponse checkMemberPhone(String phone) {
        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone",phone);
        Member member = memberMapper.selectOne(queryWrapper);
        if(member != null){
            return ServerResponse.error("手机号已存在");
        }
        return ServerResponse.success();
    }


    //注册
    @Override
    public ServerResponse register(Member member) {
        String redisCode = RedisUtil.get(member.getPhone());
        if(redisCode == null){
            ServerResponse.error("验证码已失效");
        }else if(redisCode.equals(member.getCode())){
            ServerResponse.error("验证码错误");
        }
        memberMapper.insert(member);
        return ServerResponse.success();
    }


    //登录
    @Override
    public ServerResponse login(Member member) {
        // 用户名和手机号都可以登录
        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name",member.getName());
        queryWrapper.or();
        queryWrapper.eq("phone",member.getName());
        Member memberDB = memberMapper.selectOne(queryWrapper);

        if(null == memberDB){
            ServerResponse.error("用户名或手机号不存在");
        }
        if(!member.getPassword().equals(memberDB.getPassword())){
            return ServerResponse.error("密码错误");
        }
        //账号密码都正确，就生成一个token返回前台
        String token = "";
        try {
            String jsonString = JSONObject.toJSONString(memberDB);
            String encodeJson = URLEncoder.encode(jsonString, "utf-8");
            token = JwtUtil.sign(encodeJson);
            RedisUtil.setEx(SystemConstant.TOKEN_KEY+token,token, SystemConstant.TOKEN_EXPIRE_TIME);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return ServerResponse.success(token);
    }
}
