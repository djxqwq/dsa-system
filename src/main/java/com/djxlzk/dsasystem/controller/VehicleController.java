package com.djxlzk.dsasystem.controller;

import com.djxlzk.dsasystem.dto.ResultDTO;
import com.djxlzk.dsasystem.dto.VehicleDTO;
import com.djxlzk.dsasystem.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vehicle")
@Validated
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @PostMapping
    public ResultDTO<?> addVehicle(@RequestBody @Validated VehicleDTO vehicleDTO) {
        return vehicleService.addVehicle(vehicleDTO);
    }

    @DeleteMapping("/{id}")
    public ResultDTO<?> deleteVehicle(@PathVariable Long id) {
        return vehicleService.deleteVehicle(id);
    }

    @PutMapping
    public ResultDTO<?> updateVehicle(@RequestBody @Validated VehicleDTO vehicleDTO) {
        return vehicleService.updateVehicle(vehicleDTO);
    }

    @GetMapping("/list")
    public ResultDTO<?> listVehicles(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String vehicleType,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return vehicleService.listVehicles(keyword, vehicleType, status, page, size);
    }

    @PutMapping("/status/{id}")
    public ResultDTO<?> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        return vehicleService.updateStatus(id, status);
    }

    @GetMapping("/all")
    public ResultDTO<?> getAllVehicles() {
        return ResultDTO.success(vehicleService.findAllWithCoachName());
    }
}
