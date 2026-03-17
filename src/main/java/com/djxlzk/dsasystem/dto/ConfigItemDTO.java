package com.djxlzk.dsasystem.dto;

import lombok.Data;

@Data
public class ConfigItemDTO {
    private Long id;
    private String configKey;
    private String configValue;
    private String configType;
    private String description;
}
