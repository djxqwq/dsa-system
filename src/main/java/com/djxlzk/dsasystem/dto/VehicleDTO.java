package com.djxlzk.dsasystem.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class VehicleDTO {

    private Long id;

    @NotBlank(message = "车牌号不能为空")
    @Size(max = 20, message = "车牌号不能超过20个字符")
    private String plateNumber;

    @NotBlank(message = "车型不能为空")
    private String vehicleType;

    private Long coachId;

    private Integer status;

    private String remark;
}
