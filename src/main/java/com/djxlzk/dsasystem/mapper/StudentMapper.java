package com.djxlzk.dsasystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.djxlzk.dsasystem.entity.Student;

/**
 * 学员Mapper接口
 */
public interface StudentMapper extends BaseMapper<Student> {
    /**
     * 根据手机号查询学员
     * 
     * @param mobile 手机号
     * @return 学员信息
     */
    Student selectByMobile(String mobile);
}