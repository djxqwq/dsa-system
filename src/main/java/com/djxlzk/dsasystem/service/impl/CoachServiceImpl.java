package com.djxlzk.dsasystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.djxlzk.dsasystem.dto.LoginDTO;
import com.djxlzk.dsasystem.dto.ResultDTO;
import com.djxlzk.dsasystem.entity.Coach;
import com.djxlzk.dsasystem.mapper.CoachMapper;
import com.djxlzk.dsasystem.service.CoachService;
import com.djxlzk.dsasystem.util.CaptchaStore;
import com.djxlzk.dsasystem.util.JwtUtil;
import com.djxlzk.dsasystem.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CoachServiceImpl implements CoachService {

    @Autowired
    private CoachMapper coachMapper;

    @Override
    public ResultDTO<?> login(LoginDTO loginDTO) {
        if (!CaptchaStore.verifyAndRemove(loginDTO.getCaptchaId(), loginDTO.getCaptchaCode())) {
            return ResultDTO.error(400, "验证码错误或已过期");
        }
        // 查找教练
        QueryWrapper<Coach> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile", loginDTO.getMobile());
        Coach coach = coachMapper.selectOne(wrapper);

        if (coach == null) {
            return ResultDTO.error(400, "账号不存在");
        }
        // 检查状态
        if (coach.getStatus() == 0) {
            return ResultDTO.error(400, "账号已禁用");
        }
        // 验证密码
        if (!PasswordUtil.match(loginDTO.getPassword(), coach.getPassword())) {
            return ResultDTO.error(400, "密码错误");
        }
        // 生成token
        String token = JwtUtil.generateToken(coach.getId(), "coach");
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("coachId", coach.getId());
        data.put("name", coach.getName());
        return ResultDTO.success("登录成功", data);
    }

    @Override
    public ResultDTO<?> updatePassword(Long coachId, String oldPassword, String newPassword) {
        // 查找教练
        Coach coach = coachMapper.selectById(coachId);
        if (coach == null) {
            return ResultDTO.error(400, "教练不存在");
        }
        // 验证旧密码
        if (!PasswordUtil.match(oldPassword, coach.getPassword())) {
            return ResultDTO.error(400, "旧密码错误");
        }
        // 验证新密码格式
        if (!newPassword.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,20}$")) {
            return ResultDTO.error(400, "密码长度需8-20位，包含字母和数字");
        }
        // 更新密码
        coach.setPassword(PasswordUtil.encrypt(newPassword));
        coachMapper.updateById(coach);
        return ResultDTO.success("密码修改成功");
    }

    @Override
    public Coach getById(Long id) {
        return coachMapper.selectById(id);
    }
}