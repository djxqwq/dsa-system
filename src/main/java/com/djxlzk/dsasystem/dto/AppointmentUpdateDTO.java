package com.djxlzk.dsasystem.dto;

import lombok.Data;

@Data
public class AppointmentUpdateDTO {
    private Long id;
    private Long vehicleId;
    private String appointmentDate;
    private String startTime;
    private String endTime;
    private Integer status;
    private String remark;
}
