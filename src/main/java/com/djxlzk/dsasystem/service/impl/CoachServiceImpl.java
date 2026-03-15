package com.djxlzk.dsasystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.djxlzk.dsasystem.dto.CoachDTO;
import com.djxlzk.dsasystem.dto.LoginDTO;
import com.djxlzk.dsasystem.dto.ResultDTO;
import com.djxlzk.dsasystem.entity.Coach;
import com.djxlzk.dsasystem.mapper.CoachMapper;
import com.djxlzk.dsasystem.service.CoachService;
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

    @Override
    public ResultDTO<?> addCoach(CoachDTO coachDTO) {
        QueryWrapper<Coach> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile", coachDTO.getMobile());
        if (coachMapper.selectCount(wrapper) > 0) {
            return ResultDTO.error(400, "该手机号已存在");
        }
        wrapper = new QueryWrapper<>();
        wrapper.eq("coach_no", coachDTO.getCoachNo());
        if (coachMapper.selectCount(wrapper) > 0) {
            return ResultDTO.error(400, "教练工号已存在");
        }
        Coach coach = new Coach();
        BeanUtils.copyProperties(coachDTO, coach);
        coach.setCreateTime(LocalDateTime.now());
        if (coach.getStatus() == null) {
            coach.setStatus(1);
        }
        String raw = coachDTO.getPassword();
        if (StringUtils.hasText(raw)) {
            if (!raw.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,20}$")) {
                return ResultDTO.error(400, "密码长度需8-20位，包含字母和数字");
            }
            coach.setPassword(PasswordUtil.encrypt(raw));
        } else {
            coach.setPassword(PasswordUtil.encrypt("12345678"));
        }
        coachMapper.insert(coach);
        return ResultDTO.success("添加成功");
    }

    @Override
    public ResultDTO<?> deleteCoach(Long id) {
        if (coachMapper.selectById(id) == null) {
            return ResultDTO.error(400, "教练不存在");
        }
        coachMapper.deleteById(id);
        return ResultDTO.success("删除成功");
    }

    @Override
    public ResultDTO<?> updateCoach(CoachDTO coachDTO) {
        if (coachDTO.getId() == null) {
            return ResultDTO.error(400, "缺少教练ID");
        }
        Coach exist = coachMapper.selectById(coachDTO.getId());
        if (exist == null) {
            return ResultDTO.error(400, "教练不存在");
        }
        if (StringUtils.hasText(coachDTO.getMobile()) && !coachDTO.getMobile().equals(exist.getMobile())) {
            QueryWrapper<Coach> w = new QueryWrapper<>();
            w.eq("mobile", coachDTO.getMobile());
            if (coachMapper.selectCount(w) > 0) {
                return ResultDTO.error(400, "该手机号已被使用");
            }
        }
        if (StringUtils.hasText(coachDTO.getCoachNo()) && !coachDTO.getCoachNo().equals(exist.getCoachNo())) {
            QueryWrapper<Coach> w = new QueryWrapper<>();
            w.eq("coach_no", coachDTO.getCoachNo());
            if (coachMapper.selectCount(w) > 0) {
                return ResultDTO.error(400, "教练工号已被使用");
            }
        }
        exist.setCoachNo(coachDTO.getCoachNo() != null ? coachDTO.getCoachNo() : exist.getCoachNo());
        exist.setName(coachDTO.getName() != null ? coachDTO.getName() : exist.getName());
        exist.setMobile(coachDTO.getMobile() != null ? coachDTO.getMobile() : exist.getMobile());
        if (coachDTO.getStatus() != null) {
            exist.setStatus(coachDTO.getStatus());
        }
        if (StringUtils.hasText(coachDTO.getPassword())) {
            if (!coachDTO.getPassword().matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,20}$")) {
                return ResultDTO.error(400, "密码长度需8-20位，包含字母和数字");
            }
            exist.setPassword(PasswordUtil.encrypt(coachDTO.getPassword()));
        }
        coachMapper.updateById(exist);
        return ResultDTO.success("更新成功");
    }

    @Override
    public ResultDTO<?> listCoaches(String keyword, int page, int size) {
        Page<Coach> p = new Page<>(page, size);
        QueryWrapper<Coach> wrapper = new QueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.and(w -> w
                .like("name", keyword)
                .or().like("mobile", keyword)
                .or().like("coach_no", keyword));
        }
        wrapper.orderByDesc("create_time");
        Page<Coach> result = coachMapper.selectPage(p, wrapper);
        Map<String, Object> data = new HashMap<>();
        data.put("records", result.getRecords());
        data.put("total", result.getTotal());
        return ResultDTO.success(data);
    }

    @Override
    public ResultDTO<?> resetPassword(Long id) {
        Coach coach = coachMapper.selectById(id);
        if (coach == null) {
            return ResultDTO.error(400, "教练不存在");
        }
        coach.setPassword(PasswordUtil.encrypt("12345678"));
        coachMapper.updateById(coach);
        return ResultDTO.success("密码已重置为 12345678");
    }
}