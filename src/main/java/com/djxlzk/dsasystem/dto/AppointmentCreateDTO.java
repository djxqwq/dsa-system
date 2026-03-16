package com.djxlzk.dsasystem.dto;

import lombok.Data;

@Data
public class AppointmentCreateDTO {
    private Long scheduleId;
    private Long coachId;
    private Long vehicleId;
    private String remark;
}
