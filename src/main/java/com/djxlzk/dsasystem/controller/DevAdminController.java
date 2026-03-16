package com.djxlzk.dsasystem.controller;

import com.djxlzk.dsasystem.dto.ResultDTO;
import com.djxlzk.dsasystem.entity.Admin;
import com.djxlzk.dsasystem.entity.Coach;
import com.djxlzk.dsasystem.entity.Student;
import com.djxlzk.dsasystem.mapper.AdminMapper;
import com.djxlzk.dsasystem.mapper.CoachMapper;
import com.djxlzk.dsasystem.mapper.StudentMapper;
import com.djxlzk.dsasystem.util.JwtUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 免验证码一键登录（需携带正确 secret），便于本地调试。
 */
@RestController
@RequestMapping("/api/user")
public class DevAdminController {

    private static final String DEFAULT_SECRET = "dev-admin-2025";

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private CoachMapper coachMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Value("${dev.admin.secret:dev-admin-2025}")
    private String devAdminSecret;

    private boolean validateSecret(String secret) {
        return secret != null && secret.equals(devAdminSecret);
    }

    @PostMapping("/admin/dev-login")
    public ResultDTO<?> devAdminLogin(@RequestBody(required = false) Map<String, String> body) {
        try {
            String secret = (body != null && body.get("secret") != null) ? body.get("secret").trim() : null;
            if (secret == null || !secret.equals(devAdminSecret)) {
                return ResultDTO.error(403, "密钥错误");
            }
            QueryWrapper<Admin> wrapper = new QueryWrapper<>();
            wrapper.orderByAsc("id").last("LIMIT 1");
            List<Admin> list = adminMapper.selectList(wrapper);
            if (list == null || list.isEmpty()) {
                return ResultDTO.error(400, "数据库中暂无管理员账号，请先添加");
            }
            Admin admin = list.get(0);
            Long id = admin.getId();
            if (id == null) {
                return ResultDTO.error(500, "管理员数据异常：id 为空");
            }
            String token = JwtUtil.generateToken(id, "admin");
            Map<String, Object> data = new HashMap<>();
            data.put("token", token);
            data.put("adminId", id);
            data.put("name", admin.getName() != null ? admin.getName() : "管理员");
            return ResultDTO.success("登录成功", data);
        } catch (Exception e) {
            e.printStackTrace();
            String msg = e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName();
            return ResultDTO.error(500, "一键登录异常：" + msg);
        }
    }

    @PostMapping("/student/dev-login")
    public ResultDTO<?> devStudentLogin(@RequestBody(required = false) Map<String, String> body) {
        try {
            String secret = (body != null && body.get("secret") != null) ? body.get("secret").trim() : null;
            if (!validateSecret(secret)) {
                return ResultDTO.error(403, "密钥错误");
            }
            QueryWrapper<Student> wrapper = new QueryWrapper<>();
            wrapper.orderByAsc("id").last("LIMIT 1");
            List<Student> list = studentMapper.selectList(wrapper);
            if (list == null || list.isEmpty()) {
                return ResultDTO.error(400, "数据库中暂无学员账号，请先添加");
            }
            Student student = list.get(0);
            Long id = student.getId();
            if (id == null) {
                return ResultDTO.error(500, "学员数据异常：id 为空");
            }
            String token = JwtUtil.generateToken(id, "student");
            Map<String, Object> data = new HashMap<>();
            data.put("token", token);
            data.put("studentId", id);
            data.put("userName", student.getUserName() != null ? student.getUserName() : "学员");
            data.put("mobile", student.getMobile());
            return ResultDTO.success("登录成功", data);
        } catch (Exception e) {
            e.printStackTrace();
            String msg = e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName();
            return ResultDTO.error(500, "一键登录异常：" + msg);
        }
    }

    @PostMapping("/coach/dev-login")
    public ResultDTO<?> devCoachLogin(@RequestBody(required = false) Map<String, String> body) {
        try {
            String secret = (body != null && body.get("secret") != null) ? body.get("secret").trim() : null;
            if (!validateSecret(secret)) {
                return ResultDTO.error(403, "密钥错误");
            }
            QueryWrapper<Coach> wrapper = new QueryWrapper<>();
            wrapper.orderByAsc("id").last("LIMIT 1");
            List<Coach> list = coachMapper.selectList(wrapper);
            if (list == null || list.isEmpty()) {
                return ResultDTO.error(400, "数据库中暂无教练账号，请先添加");
            }
            Coach coach = list.get(0);
            Long id = coach.getId();
            if (id == null) {
                return ResultDTO.error(500, "教练数据异常：id 为空");
            }
            String token = JwtUtil.generateToken(id, "coach");
            Map<String, Object> data = new HashMap<>();
            data.put("token", token);
            data.put("coachId", id);
            data.put("name", coach.getName() != null ? coach.getName() : "教练");
            data.put("mobile", coach.getMobile());
            return ResultDTO.success("登录成功", data);
        } catch (Exception e) {
            e.printStackTrace();
            String msg = e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName();
            return ResultDTO.error(500, "一键登录异常：" + msg);
        }
    }
}
