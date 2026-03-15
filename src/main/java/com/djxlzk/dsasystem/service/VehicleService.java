package com.djxlzk.dsasystem.service;

import com.djxlzk.dsasystem.dto.ResultDTO;
import com.djxlzk.dsasystem.dto.VehicleDTO;

public interface VehicleService {

    ResultDTO<?> addVehicle(VehicleDTO vehicleDTO);

    ResultDTO<?> deleteVehicle(Long id);

    ResultDTO<?> updateVehicle(VehicleDTO vehicleDTO);

    ResultDTO<?> listVehicles(String keyword, String vehicleType, Integer status, int page, int size);

    ResultDTO<?> updateStatus(Long id, Integer status);
}
