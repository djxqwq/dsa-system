package com.djxlzk.dsasystem.controller;

import com.djxlzk.dsasystem.dto.AdminLoginDTO;
import com.djxlzk.dsasystem.dto.CoachProfileDTO;
import com.djxlzk.dsasystem.dto.LoginDTO;
import com.djxlzk.dsasystem.dto.PasswordDTO;
import com.djxlzk.dsasystem.dto.RegisterDTO;
import com.djxlzk.dsasystem.dto.ResultDTO;
import com.djxlzk.dsasystem.dto.StudentProfileDTO;
import com.djxlzk.dsasystem.service.AdminService;
import com.djxlzk.dsasystem.service.CoachService;
import com.djxlzk.dsasystem.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;

import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/api/user")
@Validated
public class UserController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private CoachService coachService;

    @Autowired
    private AdminService adminService;

    /**
     * 学员注册
     * 
     * @param registerDTO 注册信息
     * @return 响应结果
     */
    @PostMapping("/student/register")
    public ResultDTO<?> register(@RequestBody @Validated RegisterDTO registerDTO) {
        return studentService.register(registerDTO);
    }

    /**
     * 学员登录
     * 
     * @param loginDTO 登录信息
     * @return 响应结果
     */
    @PostMapping("/student/login")
    public ResultDTO<?> studentLogin(@RequestBody @Validated LoginDTO loginDTO) {
        return studentService.login(loginDTO);
    }

    /**
     * 学员修改密码
     * 
     * @param passwordDTO    密码信息
     * @param authentication 认证信息
     * @return 响应结果
     */
    @PutMapping("/student/password")
    public ResultDTO<?> updateStudentPassword(@RequestBody PasswordDTO passwordDTO, Authentication authentication) {
        Long studentId = (Long) authentication.getPrincipal();
        return studentService.updatePassword(studentId, passwordDTO.getOldPassword(), passwordDTO.getNewPassword());
    }

    /**
     * 教练登录
     * 
     * @param loginDTO 登录信息
     * @return 响应结果
     */
    @PostMapping("/coach/login")
    public ResultDTO<?> coachLogin(@RequestBody @Validated LoginDTO loginDTO) {
        return coachService.login(loginDTO);
    }

    /**
     * 管理员登录
     *
     * @param loginDTO 登录信息
     * @return 响应结果
     */
    @PostMapping("/admin/login")
    public ResultDTO<?> adminLogin(@RequestBody @Validated AdminLoginDTO loginDTO) {
        return adminService.login(loginDTO);
    }

    /**
     * 教练修改密码
     * 
     * @param passwordDTO    密码信息
     * @param authentication 认证信息
     * @return 响应结果
     */
    @PutMapping("/coach/password")
    public ResultDTO<?> updateCoachPassword(@RequestBody PasswordDTO passwordDTO, Authentication authentication) {
        Long coachId = (Long) authentication.getPrincipal();
        return coachService.updatePassword(coachId, passwordDTO.getOldPassword(), passwordDTO.getNewPassword());
    }

    /**
     * 学员获取个人信息
     * 
     * @param authentication 认证信息
     * @return 响应结果
     */
    @GetMapping("/student/profile")
    public ResultDTO<?> getStudentProfile(Authentication authentication) {
        Long studentId = (Long) authentication.getPrincipal();
        return studentService.getProfile(studentId);
    }

    /**
     * 学员更新个人信息
     * 
     * @param profileDTO     个人信息
     * @param authentication 认证信息
     * @return 响应结果
     */
    @PutMapping("/student/profile")
    public ResultDTO<?> updateStudentProfile(@RequestBody @Validated StudentProfileDTO profileDTO, Authentication authentication) {
        Long studentId = (Long) authentication.getPrincipal();
        return studentService.updateProfile(studentId, profileDTO);
    }

    /**
     * 教练获取个人信息
     * 
     * @param authentication 认证信息
     * @return 响应结果
     */
    @GetMapping("/coach/profile")
    public ResultDTO<?> getCoachProfile(Authentication authentication) {
        Long coachId = (Long) authentication.getPrincipal();
        return coachService.getProfile(coachId);
    }

    /**
     * 教练更新个人信息
     * 
     * @param profileDTO     个人信息
     * @param authentication 认证信息
     * @return 响应结果
     */
    @PutMapping("/coach/profile")
    public ResultDTO<?> updateCoachProfile(@RequestBody @Validated CoachProfileDTO profileDTO, Authentication authentication) {
        Long coachId = (Long) authentication.getPrincipal();
        return coachService.updateProfile(coachId, profileDTO);
    }

    /**
     * 教练更新状态
     * 
     * @param status         状态值 (0-离职, 1-在职)
     * @param authentication 认证信息
     * @return 响应结果
     */
    @PutMapping("/coach/status")
    public ResultDTO<?> updateCoachStatus(@RequestBody java.util.Map<String, Integer> body, Authentication authentication) {
        Long coachId = (Long) authentication.getPrincipal();
        Integer status = body.get("status");
        return coachService.updateStatus(coachId, status);
    }
}