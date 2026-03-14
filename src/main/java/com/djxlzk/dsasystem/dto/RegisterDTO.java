package com.djxlzk.dsasystem.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;

/**
 * 注册请求DTO
 */
@Data
public class RegisterDTO {

    /**
     * 手机号（11位）
     */
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String mobile;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    private String userName;

    /**
     * 验证码ID
     */
    @NotBlank(message = "验证码ID不能为空")
    private String captchaId;

    /**
     * 验证码
     */
    @NotBlank(message = "验证码不能为空")
    private String captchaCode;
}