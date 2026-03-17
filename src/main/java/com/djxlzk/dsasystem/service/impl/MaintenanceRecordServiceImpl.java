package com.djxlzk.dsasystem.service.impl;

import com.djxlzk.dsasystem.dto.MaintenanceRecordDTO;
import com.djxlzk.dsasystem.dto.ResultDTO;
import com.djxlzk.dsasystem.entity.MaintenanceRecord;
import com.djxlzk.dsasystem.entity.Vehicle;
import com.djxlzk.dsasystem.mapper.MaintenanceRecordMapper;
import com.djxlzk.dsasystem.mapper.VehicleMapper;
import com.djxlzk.dsasystem.service.MaintenanceRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MaintenanceRecordServiceImpl implements MaintenanceRecordService {

    @Autowired
    private MaintenanceRecordMapper maintenanceRecordMapper;

    @Autowired
    private VehicleMapper vehicleMapper;

    @Override
    public ResultDTO<?> addRecord(MaintenanceRecordDTO dto) {
        Vehicle vehicle = vehicleMapper.selectById(dto.getVehicleId());
        if (vehicle == null) {
            return ResultDTO.error(400, "车辆不存在");
        }

        MaintenanceRecord record = new MaintenanceRecord();
        record.setVehicleId(dto.getVehicleId());
        record.setCoachId(dto.getCoachId() != null ? dto.getCoachId() : vehicle.getCoachId());
        record.setMaintenanceType(dto.getMaintenanceType());
        record.setMaintenanceDate(dto.getMaintenanceDate());
        record.setCost(dto.getCost());
        record.setMileage(dto.getMileage());
        record.setDescription(dto.getDescription());
        record.setNextMaintenanceDate(dto.getNextMaintenanceDate());
        record.setNextMaintenanceMileage(dto.getNextMaintenanceMileage());
        record.setCreateTime(LocalDateTime.now());
        record.setUpdateTime(LocalDateTime.now());

        maintenanceRecordMapper.insert(record);
        return ResultDTO.success("添加成功", record);
    }

    @Override
    public ResultDTO<?> updateRecord(MaintenanceRecordDTO dto) {
        if (dto.getId() == null) {
            return ResultDTO.error(400, "记录ID不能为空");
        }

        MaintenanceRecord exist = maintenanceRecordMapper.selectById(dto.getId());
        if (exist == null) {
            return ResultDTO.error(400, "记录不存在");
        }

        if (dto.getMaintenanceType() != null) {
            exist.setMaintenanceType(dto.getMaintenanceType());
        }
        if (dto.getMaintenanceDate() != null) {
            exist.setMaintenanceDate(dto.getMaintenanceDate());
        }
        if (dto.getCost() != null) {
            exist.setCost(dto.getCost());
        }
        if (dto.getMileage() != null) {
            exist.setMileage(dto.getMileage());
        }
        if (dto.getDescription() != null) {
            exist.setDescription(dto.getDescription());
        }
        if (dto.getNextMaintenanceDate() != null) {
            exist.setNextMaintenanceDate(dto.getNextMaintenanceDate());
        }
        if (dto.getNextMaintenanceMileage() != null) {
            exist.setNextMaintenanceMileage(dto.getNextMaintenanceMileage());
        }
        exist.setUpdateTime(LocalDateTime.now());

        maintenanceRecordMapper.updateById(exist);
        return ResultDTO.success("更新成功", exist);
    }

    @Override
    public ResultDTO<?> deleteRecord(Long id) {
        MaintenanceRecord record = maintenanceRecordMapper.selectById(id);
        if (record == null) {
            return ResultDTO.error(400, "记录不存在");
        }
        maintenanceRecordMapper.deleteById(id);
        return ResultDTO.success("删除成功");
    }

    @Override
    public ResultDTO<?> getRecordById(Long id) {
        MaintenanceRecord record = maintenanceRecordMapper.selectById(id);
        if (record == null) {
            return ResultDTO.error(400, "记录不存在");
        }
        return ResultDTO.success(record);
    }

    @Override
    public ResultDTO<?> getRecordsByCoachId(Long coachId) {
        List<MaintenanceRecord> records = maintenanceRecordMapper.findByCoachId(coachId);
        return ResultDTO.success(records);
    }

    @Override
    public ResultDTO<?> getRecordsByVehicleId(Long vehicleId) {
        List<MaintenanceRecord> records = maintenanceRecordMapper.findByVehicleId(vehicleId);
        return ResultDTO.success(records);
    }

    @Override
    public List<MaintenanceRecord> findByCoachId(Long coachId) {
        return maintenanceRecordMapper.findByCoachId(coachId);
    }

    @Override
    public List<MaintenanceRecord> findByVehicleId(Long vehicleId) {
        return maintenanceRecordMapper.findByVehicleId(vehicleId);
    }

    @Override
    public ResultDTO<?> getAllRecords() {
        List<MaintenanceRecord> records = maintenanceRecordMapper.findAllWithDetails();
        return ResultDTO.success(records);
    }
}
