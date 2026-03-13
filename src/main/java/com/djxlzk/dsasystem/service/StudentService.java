package com.djxlzk.dsasystem.service;

import com.djxlzk.dsasystem.dto.LoginDTO;
import com.djxlzk.dsasystem.dto.RegisterDTO;
import com.djxlzk.dsasystem.dto.ResultDTO;
import com.djxlzk.dsasystem.entity.Student;

public interface StudentService {
    ResultDTO<?> register(RegisterDTO registerDTO);
    ResultDTO<?> login(LoginDTO loginDTO);
    ResultDTO<?> updatePassword(Long studentId, String oldPassword, String newPassword);
    Student getById(Long id);
}