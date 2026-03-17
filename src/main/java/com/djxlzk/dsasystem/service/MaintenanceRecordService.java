package com.djxlzk.dsasystem.service;

import com.djxlzk.dsasystem.dto.MaintenanceRecordDTO;
import com.djxlzk.dsasystem.dto.ResultDTO;
import com.djxlzk.dsasystem.entity.MaintenanceRecord;

import java.util.List;

public interface MaintenanceRecordService {

    ResultDTO<?> addRecord(MaintenanceRecordDTO dto);

    ResultDTO<?> updateRecord(MaintenanceRecordDTO dto);

    ResultDTO<?> deleteRecord(Long id);

    ResultDTO<?> getRecordById(Long id);

    ResultDTO<?> getRecordsByCoachId(Long coachId);

    ResultDTO<?> getRecordsByVehicleId(Long vehicleId);

    List<MaintenanceRecord> findByCoachId(Long coachId);

    List<MaintenanceRecord> findByVehicleId(Long vehicleId);
}
