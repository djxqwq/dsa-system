package com.djxlzk.dsasystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.djxlzk.dsasystem.entity.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import java.util.List;

@Mapper
public interface MessageMapper extends BaseMapper<Message> {
    @Select("SELECT * FROM message WHERE student_id = #{studentId} ORDER BY created_at DESC LIMIT #{size} OFFSET #{page}")
    List<Message> selectByStudentId(@Param("studentId") Long studentId, @Param("page") int page, @Param("size") int size);

    @Select("SELECT COUNT(*) FROM message WHERE student_id = #{studentId}")
    int countByStudentId(@Param("studentId") Long studentId);

    @Select("SELECT * FROM message WHERE coach_id = #{coachId} ORDER BY created_at DESC LIMIT #{size} OFFSET #{page}")
    List<Message> selectByCoachId(@Param("coachId") Long coachId, @Param("page") int page, @Param("size") int size);

    @Select("SELECT COUNT(*) FROM message WHERE coach_id = #{coachId}")
    int countByCoachId(@Param("coachId") Long coachId);

    @Update("UPDATE message SET `read` = #{read} WHERE id IN (${ids})")
    int updateReadStatus(@Param("ids") String ids, @Param("read") Integer read);
}
