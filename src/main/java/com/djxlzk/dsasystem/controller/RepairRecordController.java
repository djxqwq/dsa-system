package com.djxlzk.dsasystem.controller;

import com.djxlzk.dsasystem.dto.RepairRecordDTO;
import com.djxlzk.dsasystem.dto.ResultDTO;
import com.djxlzk.dsasystem.service.RepairRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/repair")
@Validated
public class RepairRecordController {

    @Autowired
    private RepairRecordService repairRecordService;

    @PostMapping
    public ResultDTO<?> addRecord(@RequestBody @Validated RepairRecordDTO dto, Authentication auth) {
        Long coachId = (Long) auth.getPrincipal();
        dto.setCoachId(coachId);
        return repairRecordService.addRecord(dto);
    }

    @PutMapping
    public ResultDTO<?> updateRecord(@RequestBody @Validated RepairRecordDTO dto) {
        return repairRecordService.updateRecord(dto);
    }

    @DeleteMapping("/{id}")
    public ResultDTO<?> deleteRecord(@PathVariable Long id) {
        return repairRecordService.deleteRecord(id);
    }

    @GetMapping("/{id}")
    public ResultDTO<?> getRecordById(@PathVariable Long id) {
        return repairRecordService.getRecordById(id);
    }

    @GetMapping("/coach")
    public ResultDTO<?> getRecordsByCoach(Authentication auth) {
        Long coachId = (Long) auth.getPrincipal();
        return repairRecordService.getRecordsByCoachId(coachId);
    }

    @GetMapping("/vehicle/{vehicleId}")
    public ResultDTO<?> getRecordsByVehicle(@PathVariable Long vehicleId) {
        return repairRecordService.getRecordsByVehicleId(vehicleId);
    }

    @GetMapping("/all")
    public ResultDTO<?> getAllRecords() {
        return repairRecordService.getAllRecords();
    }
}
