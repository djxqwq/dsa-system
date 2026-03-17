package com.djxlzk.dsasystem.dto;

import lombok.Data;

import java.util.List;

@Data
public class SystemConfigDTO {
    private Integer openDays;
    private Integer dailyLimit;
    private Integer cancelDeadline;
    private Integer confirmTimeout;
    private String systemNotice;
    private Integer minAppointmentHours;
    private Integer maxAppointmentHours;
    private Integer slotDuration;
    private Boolean enableSmsNotify;
    private Boolean enableEmailNotify;
    private String workingStartTime;
    private String workingEndTime;
    private Integer defaultRequiredHours;
    private List<ConfigItemDTO> allConfigs;
}
