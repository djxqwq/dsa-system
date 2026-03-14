package com.djxlzk.dsasystem.service.impl;

import com.djxlzk.dsasystem.dto.AdminLoginDTO;
import com.djxlzk.dsasystem.dto.ResultDTO;
import com.djxlzk.dsasystem.entity.Admin;
import com.djxlzk.dsasystem.mapper.AdminMapper;
import com.djxlzk.dsasystem.service.AdminService;
import com.djxlzk.dsasystem.util.CaptchaStore;
import com.djxlzk.dsasystem.util.JwtUtil;
import com.djxlzk.dsasystem.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Override
    public ResultDTO<?> login(AdminLoginDTO loginDTO) {
        if (!CaptchaStore.verifyAndRemove(loginDTO.getCaptchaId(), loginDTO.getCaptchaCode())) {
            return ResultDTO.error(400, "验证码错误或已过期");
        }

        Admin admin = adminMapper.selectByMobile(loginDTO.getMobile());
        if (admin == null) {
            return ResultDTO.error(400, "账号不存在");
        }
        if (admin.getStatus() != null && admin.getStatus() == 0) {
            return ResultDTO.error(400, "账号已禁用");
        }
        if (!PasswordUtil.match(loginDTO.getPassword(), admin.getPassword())) {
            return ResultDTO.error(400, "密码错误");
        }

        String token = JwtUtil.generateToken(admin.getId(), "admin");
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("adminId", admin.getId());
        data.put("name", admin.getName());
        return ResultDTO.success("登录成功", data);
    }

    @Override
    public Admin getById(Long id) {
        return adminMapper.selectById(id);
    }
}
