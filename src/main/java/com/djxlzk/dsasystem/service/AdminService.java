package com.djxlzk.dsasystem.service;

import com.djxlzk.dsasystem.dto.AdminLoginDTO;
import com.djxlzk.dsasystem.dto.ResultDTO;
import com.djxlzk.dsasystem.entity.Admin;

public interface AdminService {

    ResultDTO<?> login(AdminLoginDTO loginDTO);

    Admin getById(Long id);
}
