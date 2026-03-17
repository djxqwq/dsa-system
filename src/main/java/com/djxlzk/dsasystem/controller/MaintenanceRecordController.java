package com.djxlzk.dsasystem.controller;

import com.djxlzk.dsasystem.dto.MaintenanceRecordDTO;
import com.djxlzk.dsasystem.dto.ResultDTO;
import com.djxlzk.dsasystem.service.MaintenanceRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/maintenance")
@Validated
public class MaintenanceRecordController {

    @Autowired
    private MaintenanceRecordService maintenanceRecordService;

    @PostMapping
    public ResultDTO<?> addRecord(@RequestBody @Validated MaintenanceRecordDTO dto, Authentication auth) {
        Long coachId = (Long) auth.getPrincipal();
        dto.setCoachId(coachId);
        return maintenanceRecordService.addRecord(dto);
    }

    @PutMapping
    public ResultDTO<?> updateRecord(@RequestBody @Validated MaintenanceRecordDTO dto) {
        return maintenanceRecordService.updateRecord(dto);
    }

    @DeleteMapping("/{id}")
    public ResultDTO<?> deleteRecord(@PathVariable Long id) {
        return maintenanceRecordService.deleteRecord(id);
    }

    @GetMapping("/{id}")
    public ResultDTO<?> getRecordById(@PathVariable Long id) {
        return maintenanceRecordService.getRecordById(id);
    }

    @GetMapping("/coach")
    public ResultDTO<?> getRecordsByCoach(Authentication auth) {
        Long coachId = (Long) auth.getPrincipal();
        return maintenanceRecordService.getRecordsByCoachId(coachId);
    }

    @GetMapping("/vehicle/{vehicleId}")
    public ResultDTO<?> getRecordsByVehicle(@PathVariable Long vehicleId) {
        return maintenanceRecordService.getRecordsByVehicleId(vehicleId);
    }

    @GetMapping("/all")
    public ResultDTO<?> getAllRecords() {
        return maintenanceRecordService.getAllRecords();
    }
}
