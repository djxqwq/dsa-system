package com.djxlzk.dsasystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.djxlzk.dsasystem.entity.SystemConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SystemConfigMapper extends BaseMapper<SystemConfig> {

    @Select("SELECT * FROM system_config ORDER BY id")
    List<SystemConfig> findAll();

    @Select("SELECT * FROM system_config WHERE config_key = #{key}")
    SystemConfig findByKey(@Param("key") String key);

    @Select("SELECT config_value FROM system_config WHERE config_key = #{key}")
    String getValueByKey(@Param("key") String key);
}
