package com.djxlzk.dsasystem.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class MaintenanceRecordDTO {

    private Long id;

    @NotNull(message = "车辆ID不能为空")
    private Long vehicleId;

    private Long coachId;

    @NotNull(message = "保养类型不能为空")
    private String maintenanceType;

    @NotNull(message = "保养日期不能为空")
    private LocalDate maintenanceDate;

    private BigDecimal cost;

    private Integer mileage;

    private String description;

    private LocalDate nextMaintenanceDate;

    private Integer nextMaintenanceMileage;
}
