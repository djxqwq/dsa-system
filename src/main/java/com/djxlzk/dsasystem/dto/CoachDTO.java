package com.djxlzk.dsasystem.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class CoachDTO {

    private Long id;

    @NotBlank(message = "教练编号不能为空")
    @Size(max = 32, message = "教练工号不能超过32个字符")
    private String coachNo;

    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String mobile;

    private String password;

    @NotBlank(message = "姓名不能为空")
    private String name;

    private Integer status;
}
