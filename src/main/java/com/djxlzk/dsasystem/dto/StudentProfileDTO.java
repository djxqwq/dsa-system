package com.djxlzk.dsasystem.dto;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class StudentProfileDTO {

    @Size(max = 50, message = "用户名不能超过50个字符")
    private String userName;

    @Pattern(regexp = "^$|^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String mobile;

    private String carType;

    private Integer gender;
}
