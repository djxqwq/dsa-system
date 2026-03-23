package com.djxlzk.dsasystem.service;

import com.djxlzk.dsasystem.dto.CoachDTO;
import com.djxlzk.dsasystem.dto.CoachProfileDTO;
import com.djxlzk.dsasystem.dto.LoginDTO;
import com.djxlzk.dsasystem.dto.ResultDTO;
import com.djxlzk.dsasystem.entity.Coach;

public interface CoachService {
    ResultDTO<?> login(LoginDTO loginDTO);

    ResultDTO<?> updatePassword(Long coachId, String oldPassword, String newPassword);

    Coach getById(Long id);

    ResultDTO<?> addCoach(CoachDTO coachDTO);

    ResultDTO<?> deleteCoach(Long id);

    ResultDTO<?> updateCoach(CoachDTO coachDTO);

    ResultDTO<?> listCoaches(String keyword, int page, int size);

    ResultDTO<?> resetPassword(Long id);

    ResultDTO<?> listAllCoaches();

    ResultDTO<?> getProfile(Long coachId);

    ResultDTO<?> updateProfile(Long coachId, CoachProfileDTO profileDTO);

    ResultDTO<?> updateStatus(Long coachId, Integer status);

    ResultDTO<?> updateWorkStatus(Long coachId, Integer workStatus);
}