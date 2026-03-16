package com.djxlzk.dsasystem.dto;

import lombok.Data;

@Data
public class ScheduleDTO {
    private Long id;
    private Long coachId;
    private String scheduleDate;
    private String startTime;
    private String endTime;
    private Integer capacity;
    private Integer status;
}
