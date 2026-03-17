package com.djxlzk.dsasystem.service;

import com.djxlzk.dsasystem.dto.ResultDTO;
import com.djxlzk.dsasystem.dto.VehicleDTO;
import com.djxlzk.dsasystem.entity.Vehicle;

import java.util.List;

public interface VehicleService {

    ResultDTO<?> addVehicle(VehicleDTO vehicleDTO);

    ResultDTO<?> deleteVehicle(Long id);

    ResultDTO<?> updateVehicle(VehicleDTO vehicleDTO);

    ResultDTO<?> listVehicles(String keyword, String vehicleType, Integer status, Long coachId, int page, int size);

    ResultDTO<?> updateStatus(Long id, Integer status);

    List<Vehicle> findAllWithCoachName();

    List<Vehicle> findByCarType(String carType);

    ResultDTO<?> getVehiclesByCoachId(Long coachId);
}
