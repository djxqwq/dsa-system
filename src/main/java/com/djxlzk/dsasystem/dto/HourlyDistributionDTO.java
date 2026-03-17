package com.djxlzk.dsasystem.dto;

import lombok.Data;

public class HourlyDistributionDTO {
    private Integer hour;
    private Long count;

    public HourlyDistributionDTO() {}

    public HourlyDistributionDTO(Integer hour, Long count) {
        this.hour = hour;
        this.count = count;
    }

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
