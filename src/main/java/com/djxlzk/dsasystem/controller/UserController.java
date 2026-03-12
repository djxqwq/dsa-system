package com.djxlzk.dsasystem.controller;

import com.djxlzk.dsasystem.dto.LoginDTO;
import com.djxlzk.dsasystem.dto.PasswordDTO;
import com.djxlzk.dsasystem.dto.RegisterDTO;
import com.djxlzk.dsasystem.dto.ResultDTO;
import com.djxlzk.dsasystem.service.CoachService;
import com.djxlzk.dsasystem.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private CoachService coachService;

    // 学员注册
    @PostMapping("/student/register")
    public ResultDTO<?> register(@RequestBody RegisterDTO registerDTO) {
        return studentService.register(registerDTO);
    }

    // 学员登录
    @PostMapping("/student/login")
    public ResultDTO<?> studentLogin(@RequestBody LoginDTO loginDTO) {
        return studentService.login(loginDTO);
    }

    // 学员修改密码
    @PutMapping("/student/password")
    public ResultDTO<?> updateStudentPassword(@RequestBody PasswordDTO passwordDTO, Authentication authentication) {
        Long studentId = (Long) authentication.getPrincipal();
        return studentService.updatePassword(studentId, passwordDTO.getOldPassword(), passwordDTO.getNewPassword());
    }

    // 教练登录
    @PostMapping("/coach/login")
    public ResultDTO<?> coachLogin(@RequestBody LoginDTO loginDTO) {
        return coachService.login(loginDTO);
    }

    // 教练修改密码
    @PutMapping("/coach/password")
    public ResultDTO<?> updateCoachPassword(@RequestBody PasswordDTO passwordDTO, Authentication authentication) {
        Long coachId = (Long) authentication.getPrincipal();
        return coachService.updatePassword(coachId, passwordDTO.getOldPassword(), passwordDTO.getNewPassword());
    }
}