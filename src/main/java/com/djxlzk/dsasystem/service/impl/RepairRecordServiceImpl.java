package com.djxlzk.dsasystem.service.impl;

import com.djxlzk.dsasystem.dto.RepairRecordDTO;
import com.djxlzk.dsasystem.dto.ResultDTO;
import com.djxlzk.dsasystem.entity.RepairRecord;
import com.djxlzk.dsasystem.entity.Vehicle;
import com.djxlzk.dsasystem.mapper.RepairRecordMapper;
import com.djxlzk.dsasystem.mapper.VehicleMapper;
import com.djxlzk.dsasystem.service.RepairRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RepairRecordServiceImpl implements RepairRecordService {

    @Autowired
    private RepairRecordMapper repairRecordMapper;

    @Autowired
    private VehicleMapper vehicleMapper;

    @Override
    public ResultDTO<?> addRecord(RepairRecordDTO dto) {
        Vehicle vehicle = vehicleMapper.selectById(dto.getVehicleId());
        if (vehicle == null) {
            return ResultDTO.error(400, "车辆不存在");
        }

        RepairRecord record = new RepairRecord();
        record.setVehicleId(dto.getVehicleId());
        record.setCoachId(dto.getCoachId() != null ? dto.getCoachId() : vehicle.getCoachId());
        record.setRepairType(dto.getRepairType());
        record.setRepairDate(dto.getRepairDate());
        record.setCost(dto.getCost());
        record.setMileage(dto.getMileage());
        record.setDescription(dto.getDescription());
        record.setParts(dto.getParts());
        record.setRepairShop(dto.getRepairShop());
        record.setStatus(dto.getStatus() != null ? dto.getStatus() : 1);
        record.setCreateTime(LocalDateTime.now());
        record.setUpdateTime(LocalDateTime.now());

        repairRecordMapper.insert(record);
        return ResultDTO.success("添加成功", record);
    }

    @Override
    public ResultDTO<?> updateRecord(RepairRecordDTO dto) {
        if (dto.getId() == null) {
            return ResultDTO.error(400, "记录ID不能为空");
        }

        RepairRecord exist = repairRecordMapper.selectById(dto.getId());
        if (exist == null) {
            return ResultDTO.error(400, "记录不存在");
        }

        if (dto.getRepairType() != null) {
            exist.setRepairType(dto.getRepairType());
        }
        if (dto.getRepairDate() != null) {
            exist.setRepairDate(dto.getRepairDate());
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
        if (dto.getParts() != null) {
            exist.setParts(dto.getParts());
        }
        if (dto.getRepairShop() != null) {
            exist.setRepairShop(dto.getRepairShop());
        }
        if (dto.getStatus() != null) {
            exist.setStatus(dto.getStatus());
        }
        exist.setUpdateTime(LocalDateTime.now());

        repairRecordMapper.updateById(exist);
        return ResultDTO.success("更新成功", exist);
    }

    @Override
    public ResultDTO<?> deleteRecord(Long id) {
        RepairRecord record = repairRecordMapper.selectById(id);
        if (record == null) {
            return ResultDTO.error(400, "记录不存在");
        }
        repairRecordMapper.deleteById(id);
        return ResultDTO.success("删除成功");
    }

    @Override
    public ResultDTO<?> getRecordById(Long id) {
        RepairRecord record = repairRecordMapper.selectById(id);
        if (record == null) {
            return ResultDTO.error(400, "记录不存在");
        }
        return ResultDTO.success(record);
    }

    @Override
    public ResultDTO<?> getRecordsByCoachId(Long coachId) {
        List<RepairRecord> records = repairRecordMapper.findByCoachId(coachId);
        return ResultDTO.success(records);
    }

    @Override
    public ResultDTO<?> getRecordsByVehicleId(Long vehicleId) {
        List<RepairRecord> records = repairRecordMapper.findByVehicleId(vehicleId);
        return ResultDTO.success(records);
    }

    @Override
    public List<RepairRecord> findByCoachId(Long coachId) {
        return repairRecordMapper.findByCoachId(coachId);
    }

    @Override
    public List<RepairRecord> findByVehicleId(Long vehicleId) {
        return repairRecordMapper.findByVehicleId(vehicleId);
    }

    @Override
    public ResultDTO<?> getAllRecords() {
        List<RepairRecord> records = repairRecordMapper.findAllWithDetails();
        return ResultDTO.success(records);
    }
}
