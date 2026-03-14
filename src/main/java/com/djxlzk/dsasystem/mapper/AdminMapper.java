package com.djxlzk.dsasystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.djxlzk.dsasystem.entity.Admin;
import org.apache.ibatis.annotations.Select;

public interface AdminMapper extends BaseMapper<Admin> {
     @Select("SELECT * FROM admin WHERE mobile = #{mobile} LIMIT 1")
    Admin selectByMobile(String mobile);
}
