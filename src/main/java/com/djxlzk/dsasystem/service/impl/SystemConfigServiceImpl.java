package com.djxlzk.dsasystem.service.impl;

import com.djxlzk.dsasystem.dto.ConfigItemDTO;
import com.djxlzk.dsasystem.dto.ResultDTO;
import com.djxlzk.dsasystem.dto.SystemConfigDTO;
import com.djxlzk.dsasystem.entity.SystemConfig;
import com.djxlzk.dsasystem.mapper.SystemConfigMapper;
import com.djxlzk.dsasystem.service.SystemConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SystemConfigServiceImpl implements SystemConfigService {

    @Autowired
    private SystemConfigMapper systemConfigMapper;

    @Override
    public ResultDTO<SystemConfigDTO> getAllConfigs() {
        List<SystemConfig> configs = systemConfigMapper.findAll();
        SystemConfigDTO dto = new SystemConfigDTO();
        List<ConfigItemDTO> allConfigs = new ArrayList<>();

        for (SystemConfig config : configs) {
            ConfigItemDTO item = new ConfigItemDTO();
            item.setId(config.getId());
            item.setConfigKey(config.getConfigKey());
            item.setConfigValue(config.getConfigValue());
            item.setConfigType(config.getConfigType());
            item.setDescription(config.getDescription());
            allConfigs.add(item);

            String key = config.getConfigKey();
            String value = config.getConfigValue();
            String type = config.getConfigType();

            switch (key) {
                case "open_days":
                    dto.setOpenDays(parseInteger(value, 7));
                    break;
                case "daily_limit":
                    dto.setDailyLimit(parseInteger(value, 1));
                    break;
                case "cancel_deadline":
                    dto.setCancelDeadline(parseInteger(value, 2));
                    break;
                case "confirm_timeout":
                    dto.setConfirmTimeout(parseInteger(value, 30));
                    break;
                case "system_notice":
                    dto.setSystemNotice(value != null ? value : "");
                    break;
                case "min_appointment_hours":
                    dto.setMinAppointmentHours(parseInteger(value, 1));
                    break;
                case "max_appointment_hours":
                    dto.setMaxAppointmentHours(parseInteger(value, 4));
                    break;
                case "slot_duration":
                    dto.setSlotDuration(parseInteger(value, 30));
                    break;
                case "enable_sms_notify":
                    dto.setEnableSmsNotify(parseBoolean(value, false));
                    break;
                case "enable_email_notify":
                    dto.setEnableEmailNotify(parseBoolean(value, false));
                    break;
                case "working_start_time":
                    dto.setWorkingStartTime(value != null ? value : "08:00");
                    break;
                case "working_end_time":
                    dto.setWorkingEndTime(value != null ? value : "18:00");
                    break;
                case "default_required_hours":
                    dto.setDefaultRequiredHours(parseInteger(value, 12));
                    break;
            }
        }

        dto.setAllConfigs(allConfigs);
        return ResultDTO.success(dto);
    }

    @Override
    @Transactional
    public ResultDTO<?> updateConfig(String key, String value) {
        SystemConfig config = systemConfigMapper.findByKey(key);
        if (config == null) {
            return ResultDTO.error(404, "配置项不存在");
        }

        config.setConfigValue(value);
        config.setUpdateTime(LocalDateTime.now());
        systemConfigMapper.updateById(config);

        return ResultDTO.success("配置已更新", null);
    }

    @Override
    @Transactional
    public ResultDTO<?> updateConfigs(SystemConfigDTO dto) {
        updateConfigIfNotNull("open_days", dto.getOpenDays());
        updateConfigIfNotNull("daily_limit", dto.getDailyLimit());
        updateConfigIfNotNull("cancel_deadline", dto.getCancelDeadline());
        updateConfigIfNotNull("confirm_timeout", dto.getConfirmTimeout());
        updateConfigIfNotNull("system_notice", dto.getSystemNotice());
        updateConfigIfNotNull("min_appointment_hours", dto.getMinAppointmentHours());
        updateConfigIfNotNull("max_appointment_hours", dto.getMaxAppointmentHours());
        updateConfigIfNotNull("slot_duration", dto.getSlotDuration());
        updateConfigIfNotNull("enable_sms_notify", dto.getEnableSmsNotify());
        updateConfigIfNotNull("enable_email_notify", dto.getEnableEmailNotify());
        updateConfigIfNotNull("working_start_time", dto.getWorkingStartTime());
        updateConfigIfNotNull("working_end_time", dto.getWorkingEndTime());
        updateConfigIfNotNull("default_required_hours", dto.getDefaultRequiredHours());

        return ResultDTO.success("配置已保存", null);
    }

    @Override
    public String getConfigValue(String key) {
        return systemConfigMapper.getValueByKey(key);
    }

    @Override
    public Integer getConfigValueAsInt(String key, Integer defaultValue) {
        String value = getConfigValue(key);
        return parseInteger(value, defaultValue);
    }

    @Override
    public Boolean getConfigValueAsBool(String key, Boolean defaultValue) {
        String value = getConfigValue(key);
        return parseBoolean(value, defaultValue);
    }

    private void updateConfigIfNotNull(String key, Object value) {
        if (value == null)
            return;

        SystemConfig config = systemConfigMapper.findByKey(key);
        if (config != null) {
            config.setConfigValue(String.valueOf(value));
            config.setUpdateTime(LocalDateTime.now());
            systemConfigMapper.updateById(config);
        }
    }

    private Integer parseInteger(String value, Integer defaultValue) {
        if (value == null || value.isEmpty())
            return defaultValue;
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private Boolean parseBoolean(String value, Boolean defaultValue) {
        if (value == null || value.isEmpty())
            return defaultValue;
        return "true".equalsIgnoreCase(value) || "1".equals(value);
    }
}
