package com.djxlzk.dsasystem.service;

import com.djxlzk.dsasystem.dto.RepairRecordDTO;
import com.djxlzk.dsasystem.dto.ResultDTO;
import com.djxlzk.dsasystem.entity.RepairRecord;

import java.util.List;

public interface RepairRecordService {

    ResultDTO<?> addRecord(RepairRecordDTO dto);

    ResultDTO<?> updateRecord(RepairRecordDTO dto);

    ResultDTO<?> deleteRecord(Long id);

    ResultDTO<?> getRecordById(Long id);

    ResultDTO<?> getRecordsByCoachId(Long coachId);

    ResultDTO<?> getRecordsByVehicleId(Long vehicleId);

    List<RepairRecord> findByCoachId(Long coachId);

    List<RepairRecord> findByVehicleId(Long vehicleId);

    ResultDTO<?> getAllRecords();
}
