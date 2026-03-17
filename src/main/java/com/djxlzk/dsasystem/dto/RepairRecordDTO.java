package com.djxlzk.dsasystem.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class RepairRecordDTO {

    private Long id;

    @NotNull(message = "车辆ID不能为空")
    private Long vehicleId;

    private Long coachId;

    @NotNull(message = "维修类型不能为空")
    private String repairType;

    @NotNull(message = "维修日期不能为空")
    private LocalDate repairDate;

    private BigDecimal cost;

    private Integer mileage;

    private String description;

    private String parts;

    private String repairShop;

    private Integer status;
}
