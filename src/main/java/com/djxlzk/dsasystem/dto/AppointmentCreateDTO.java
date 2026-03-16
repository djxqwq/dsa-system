package com.djxlzk.dsasystem.dto;

import lombok.Data;

@Data
public class AppointmentCreateDTO {
    private Long coachId;
    private Long vehicleId;
    private String appointmentDate;
    private String startTime;
    private String endTime;
    private String remark;
}
