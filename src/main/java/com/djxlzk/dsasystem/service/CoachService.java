package com.djxlzk.dsasystem.service;

import com.djxlzk.dsasystem.dto.LoginDTO;
import com.djxlzk.dsasystem.dto.ResultDTO;
import com.djxlzk.dsasystem.entity.Coach;

public interface CoachService {
    ResultDTO<?> login(LoginDTO loginDTO);
    ResultDTO<?> updatePassword(Long coachId, String oldPassword, String newPassword);
    Coach getById(Long id);
}