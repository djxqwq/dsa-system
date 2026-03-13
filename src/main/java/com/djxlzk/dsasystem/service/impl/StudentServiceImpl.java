package com.djxlzk.dsasystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.djxlzk.dsasystem.dto.LoginDTO;
import com.djxlzk.dsasystem.dto.RegisterDTO;
import com.djxlzk.dsasystem.dto.ResultDTO;
import com.djxlzk.dsasystem.entity.Student;
import com.djxlzk.dsasystem.mapper.StudentMapper;
import com.djxlzk.dsasystem.service.StudentService;
import com.djxlzk.dsasystem.util.JwtUtil;
import com.djxlzk.dsasystem.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentMapper studentMapper;

    @Override
    public ResultDTO<?> register(RegisterDTO registerDTO) {
        // 验证手机号格式
        if (!registerDTO.getMobile().matches("^1[3-9]\\d{9}$")) {
            return ResultDTO.error(400, "手机号格式错误");
        }
        // 验证密码格式
        if (!registerDTO.getPassword().matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,20}$")) {
            return ResultDTO.error(400, "密码长度需8-20位，包含字母和数字");
        }
        // 检查手机号是否已注册
        if (studentMapper.selectByMobile(registerDTO.getMobile()) != null) {
            return ResultDTO.error(400, "手机号已注册");
        }
        // 创建学员
        Student student = new Student();
        student.setMobile(registerDTO.getMobile());
        student.setPassword(PasswordUtil.encrypt(registerDTO.getPassword()));
        student.setUserName(registerDTO.getUserName());
        student.setCreateTime(LocalDateTime.now());
        student.setStatus(1);
        student.setNoShowCount(0);
        studentMapper.insert(student);
        return ResultDTO.success("注册成功");
    }

    @Override
    public ResultDTO<?> login(LoginDTO loginDTO) {
        // 查找学员
        Student student = studentMapper.selectByMobile(loginDTO.getMobile());
        if (student == null) {
            return ResultDTO.error(400, "手机号未注册");
        }
        // 检查状态
        if (student.getStatus() == 0) {
            return ResultDTO.error(400, "账号已禁用");
        }
        // 验证密码
        if (!PasswordUtil.match(loginDTO.getPassword(), student.getPassword())) {
            return ResultDTO.error(400, "密码错误，剩余4次机会");
        }
        // 生成token
        String token = JwtUtil.generateToken(student.getId(), "student");
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("studentId", student.getId());
        data.put("userName", student.getUserName());
        return ResultDTO.success("登录成功", data);
    }

    @Override
    public ResultDTO<?> updatePassword(Long studentId, String oldPassword, String newPassword) {
        // 查找学员
        Student student = studentMapper.selectById(studentId);
        if (student == null) {
            return ResultDTO.error(400, "学员不存在");
        }
        // 验证旧密码
        if (!PasswordUtil.match(oldPassword, student.getPassword())) {
            return ResultDTO.error(400, "旧密码错误");
        }
        // 验证新密码格式
        if (!newPassword.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,20}$")) {
            return ResultDTO.error(400, "密码长度需8-20位，包含字母和数字");
        }
        // 更新密码
        student.setPassword(PasswordUtil.encrypt(newPassword));
        studentMapper.updateById(student);
        return ResultDTO.success("密码修改成功");
    }

    @Override
    public Student getById(Long id) {
        return studentMapper.selectById(id);
    }
}