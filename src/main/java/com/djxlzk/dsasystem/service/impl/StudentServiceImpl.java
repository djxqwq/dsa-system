package com.djxlzk.dsasystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.djxlzk.dsasystem.dto.LoginDTO;
import com.djxlzk.dsasystem.dto.RegisterDTO;
import com.djxlzk.dsasystem.dto.ResultDTO;
import com.djxlzk.dsasystem.dto.StudentDTO;
import com.djxlzk.dsasystem.entity.Student;
import com.djxlzk.dsasystem.mapper.StudentMapper;
import com.djxlzk.dsasystem.service.StudentService;
import com.djxlzk.dsasystem.util.CaptchaStore;
import com.djxlzk.dsasystem.util.JwtUtil;
import com.djxlzk.dsasystem.util.PasswordUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentMapper studentMapper;

    @Override
    public ResultDTO<?> register(RegisterDTO registerDTO) {
        if (!CaptchaStore.verifyAndRemove(registerDTO.getCaptchaId(), registerDTO.getCaptchaCode())) {
            return ResultDTO.error(400, "验证码错误或已过期");
        }
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
        if (!CaptchaStore.verifyAndRemove(loginDTO.getCaptchaId(), loginDTO.getCaptchaCode())) {
            return ResultDTO.error(400, "验证码错误或已过期");
        }
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

    @Override
    public ResultDTO<?> addStudent(StudentDTO studentDTO) {
        QueryWrapper<Student> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile", studentDTO.getMobile());
        if (studentMapper.selectCount(wrapper) > 0) {
            return ResultDTO.error(400, "该手机号已存在");
        }
        Student student = new Student();
        BeanUtils.copyProperties(studentDTO, student);
        student.setCreateTime(LocalDateTime.now());
        if (student.getStatus() == null) {
            student.setStatus(1);
        }
        student.setNoShowCount(0);
        String raw = studentDTO.getPassword();
        if (StringUtils.hasText(raw)) {
            if (!raw.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,20}$")) {
                return ResultDTO.error(400, "密码长度需8-20位，包含字母和数字");
            }
            student.setPassword(PasswordUtil.encrypt(raw));
        } else {
            student.setPassword(PasswordUtil.encrypt("12345678"));
        }
        studentMapper.insert(student);
        return ResultDTO.success("添加成功");
    }

    @Override
    public ResultDTO<?> deleteStudent(Long id) {
        if (studentMapper.selectById(id) == null) {
            return ResultDTO.error(400, "学员不存在");
        }
        studentMapper.deleteById(id);
        return ResultDTO.success("删除成功");
    }

    @Override
    public ResultDTO<?> updateStudent(StudentDTO studentDTO) {
        if (studentDTO.getId() == null) {
            return ResultDTO.error(400, "缺少学员ID");
        }
        Student exist = studentMapper.selectById(studentDTO.getId());
        if (exist == null) {
            return ResultDTO.error(400, "学员不存在");
        }
        if (StringUtils.hasText(studentDTO.getMobile()) && !studentDTO.getMobile().equals(exist.getMobile())) {
            QueryWrapper<Student> w = new QueryWrapper<>();
            w.eq("mobile", studentDTO.getMobile());
            if (studentMapper.selectCount(w) > 0) {
                return ResultDTO.error(400, "该手机号已被使用");
            }
        }
        exist.setUserName(studentDTO.getUserName() != null ? studentDTO.getUserName() : exist.getUserName());
        exist.setMobile(studentDTO.getMobile() != null ? studentDTO.getMobile() : exist.getMobile());
        if (studentDTO.getStatus() != null) {
            exist.setStatus(studentDTO.getStatus());
        }
        if (StringUtils.hasText(studentDTO.getPassword())) {
            if (!studentDTO.getPassword().matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,20}$")) {
                return ResultDTO.error(400, "密码长度需8-20位，包含字母和数字");
            }
            exist.setPassword(PasswordUtil.encrypt(studentDTO.getPassword()));
        }
        studentMapper.updateById(exist);
        return ResultDTO.success("更新成功");
    }

    @Override
    public ResultDTO<?> listStudents(String keyword, Integer status, int page, int size) {
        Page<Student> p = new Page<>(page, size);
        QueryWrapper<Student> wrapper = new QueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.and(w -> w
                .like("user_name", keyword)
                .or().like("mobile", keyword));
        }
        if (status != null) {
            wrapper.eq("status", status);
        }
        wrapper.orderByDesc("create_time");
        Page<Student> result = studentMapper.selectPage(p, wrapper);
        Map<String, Object> data = new HashMap<>();
        data.put("records", result.getRecords());
        data.put("total", result.getTotal());
        return ResultDTO.success(data);
    }

    @Override
    public ResultDTO<?> resetPassword(Long id) {
        Student student = studentMapper.selectById(id);
        if (student == null) {
            return ResultDTO.error(400, "学员不存在");
        }
        student.setPassword(PasswordUtil.encrypt("12345678"));
        studentMapper.updateById(student);
        return ResultDTO.success("密码已重置为 12345678");
    }
}