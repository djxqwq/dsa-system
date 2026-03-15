package com.djxlzk.dsasystem.service;

import com.djxlzk.dsasystem.dto.LoginDTO;
import com.djxlzk.dsasystem.dto.RegisterDTO;
import com.djxlzk.dsasystem.dto.ResultDTO;
import com.djxlzk.dsasystem.dto.StudentDTO;
import com.djxlzk.dsasystem.dto.StudentProfileDTO;
import com.djxlzk.dsasystem.entity.Student;

public interface StudentService {
    ResultDTO<?> register(RegisterDTO registerDTO);
    ResultDTO<?> login(LoginDTO loginDTO);
    ResultDTO<?> updatePassword(Long studentId, String oldPassword, String newPassword);
    Student getById(Long id);

    ResultDTO<?> addStudent(StudentDTO studentDTO);
    ResultDTO<?> deleteStudent(Long id);
    ResultDTO<?> updateStudent(StudentDTO studentDTO);
    ResultDTO<?> listStudents(String keyword, Integer status, int page, int size);
    ResultDTO<?> resetPassword(Long id);

    ResultDTO<?> getProfile(Long studentId);
    ResultDTO<?> updateProfile(Long studentId, StudentProfileDTO profileDTO);
}