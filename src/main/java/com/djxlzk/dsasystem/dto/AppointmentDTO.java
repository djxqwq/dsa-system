package com.djxlzk.dsasystem.dto;

import lombok.Data;

@Data
public class AppointmentDTO {
    private Long coachId;
    private Long siteId;
    private String appointTime;
}