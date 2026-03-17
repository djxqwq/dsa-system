package com.djxlzk.dsasystem.service;

import com.djxlzk.dsasystem.dto.ResultDTO;
import com.djxlzk.dsasystem.dto.SystemConfigDTO;

public interface SystemConfigService {
    ResultDTO<SystemConfigDTO> getAllConfigs();

    ResultDTO<?> updateConfig(String key, String value);

    ResultDTO<?> updateConfigs(SystemConfigDTO dto);

    String getConfigValue(String key);

    Integer getConfigValueAsInt(String key, Integer defaultValue);

    Boolean getConfigValueAsBool(String key, Boolean defaultValue);
}
