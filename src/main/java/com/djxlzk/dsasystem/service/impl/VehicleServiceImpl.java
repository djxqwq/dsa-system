package com.djxlzk.dsasystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.djxlzk.dsasystem.dto.ResultDTO;
import com.djxlzk.dsasystem.dto.VehicleDTO;
import com.djxlzk.dsasystem.entity.Coach;
import com.djxlzk.dsasystem.entity.Vehicle;
import com.djxlzk.dsasystem.mapper.CoachMapper;
import com.djxlzk.dsasystem.mapper.VehicleMapper;
import com.djxlzk.dsasystem.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class VehicleServiceImpl implements VehicleService {

    @Autowired
    private VehicleMapper vehicleMapper;

    @Autowired
    private CoachMapper coachMapper;

    @Override
    public ResultDTO<?> addVehicle(VehicleDTO vehicleDTO) {
        QueryWrapper<Vehicle> wrapper = new QueryWrapper<>();
        wrapper.eq("plate_number", vehicleDTO.getPlateNumber());
        if (vehicleMapper.selectCount(wrapper) > 0) {
            return ResultDTO.error(400, "该车牌号已存在");
        }
        Vehicle vehicle = new Vehicle();
        vehicle.setPlateNumber(vehicleDTO.getPlateNumber());
        vehicle.setVehicleType(vehicleDTO.getVehicleType());
        vehicle.setCoachId(vehicleDTO.getCoachId());
        vehicle.setRemark(vehicleDTO.getRemark());
        vehicle.setStatus(vehicleDTO.getStatus() != null ? vehicleDTO.getStatus() : 1);
        vehicle.setCreateTime(LocalDateTime.now());
        vehicle.setUpdateTime(LocalDateTime.now());
        vehicleMapper.insert(vehicle);
        return ResultDTO.success("添加成功");
    }

    @Override
    public ResultDTO<?> deleteVehicle(Long id) {
        if (vehicleMapper.selectById(id) == null) {
            return ResultDTO.error(400, "车辆不存在");
        }
        vehicleMapper.deleteById(id);
        return ResultDTO.success("删除成功");
    }

    @Override
    public ResultDTO<?> updateVehicle(VehicleDTO vehicleDTO) {
        if (vehicleDTO.getId() == null) {
            return ResultDTO.error(400, "缺少车辆ID");
        }
        Vehicle exist = vehicleMapper.selectById(vehicleDTO.getId());
        if (exist == null) {
            return ResultDTO.error(400, "车辆不存在");
        }
        if (StringUtils.hasText(vehicleDTO.getPlateNumber())
                && !vehicleDTO.getPlateNumber().equals(exist.getPlateNumber())) {
            QueryWrapper<Vehicle> w = new QueryWrapper<>();
            w.eq("plate_number", vehicleDTO.getPlateNumber());
            if (vehicleMapper.selectCount(w) > 0) {
                return ResultDTO.error(400, "该车牌号已被使用");
            }
        }
        exist.setPlateNumber(
                vehicleDTO.getPlateNumber() != null ? vehicleDTO.getPlateNumber() : exist.getPlateNumber());
        exist.setVehicleType(
                vehicleDTO.getVehicleType() != null ? vehicleDTO.getVehicleType() : exist.getVehicleType());
        exist.setCoachId(vehicleDTO.getCoachId());
        exist.setRemark(vehicleDTO.getRemark());
        if (vehicleDTO.getStatus() != null) {
            exist.setStatus(vehicleDTO.getStatus());
        }
        exist.setUpdateTime(LocalDateTime.now());
        vehicleMapper.updateById(exist);
        return ResultDTO.success("更新成功");
    }

    @Override
    public ResultDTO<?> listVehicles(String keyword, String vehicleType, Integer status, int page, int size) {
        Page<Vehicle> p = new Page<>(page, size);
        QueryWrapper<Vehicle> wrapper = new QueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.like("plate_number", keyword);
        }
        if (StringUtils.hasText(vehicleType) && !"all".equals(vehicleType)) {
            wrapper.eq("vehicle_type", vehicleType);
        }
        if (status != null) {
            wrapper.eq("status", status);
        }
        wrapper.orderByDesc("create_time");
        Page<Vehicle> result = vehicleMapper.selectPage(p, wrapper);

        List<Vehicle> records = result.getRecords();
        Set<Long> coachIds = records.stream()
                .map(Vehicle::getCoachId)
                .filter(id -> id != null)
                .collect(Collectors.toSet());
        if (!coachIds.isEmpty()) {
            List<Coach> coaches = coachMapper.selectBatchIds(coachIds);
            Map<Long, String> coachNameMap = coaches.stream()
                    .collect(Collectors.toMap(Coach::getId, Coach::getName));
            records.forEach(v -> {
                if (v.getCoachId() != null) {
                    v.setCoachName(coachNameMap.get(v.getCoachId()));
                }
            });
        }

        Map<String, Object> data = new HashMap<>();
        data.put("records", records);
        data.put("total", result.getTotal());
        return ResultDTO.success(data);
    }

    @Override
    public ResultDTO<?> updateStatus(Long id, Integer status) {
        Vehicle vehicle = vehicleMapper.selectById(id);
        if (vehicle == null) {
            return ResultDTO.error(400, "车辆不存在");
        }
        vehicle.setStatus(status);
        vehicle.setUpdateTime(LocalDateTime.now());
        vehicleMapper.updateById(vehicle);
        String msg = status == 1 ? "已启用" : (status == 2 ? "已设为维修中" : "已停用");
        return ResultDTO.success(msg);
    }

    @Override
    public List<Vehicle> findAllWithCoachName() {
        return vehicleMapper.findAllWithCoachName();
    }

    @Override
    public List<Vehicle> findByCarType(String carType) {
        return vehicleMapper.findByCarTypeWithCoachName(carType);
    }
}
