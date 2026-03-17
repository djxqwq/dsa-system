package com.djxlzk.dsasystem.controller;

import com.djxlzk.dsasystem.dto.ResultDTO;
import com.djxlzk.dsasystem.dto.SystemConfigDTO;
import com.djxlzk.dsasystem.service.SystemConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/config")
public class SystemConfigController {

    @Autowired
    private SystemConfigService systemConfigService;

    @GetMapping
    public ResultDTO<SystemConfigDTO> getAllConfigs() {
        return systemConfigService.getAllConfigs();
    }

    @PutMapping
    public ResultDTO<?> updateConfigs(@RequestBody SystemConfigDTO dto) {
        return systemConfigService.updateConfigs(dto);
    }

    @PutMapping("/{key}")
    public ResultDTO<?> updateConfig(@PathVariable String key, @RequestParam String value) {
        return systemConfigService.updateConfig(key, value);
    }
}
