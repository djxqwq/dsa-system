package com.djxlzk.dsasystem.service;

import com.djxlzk.dsasystem.dto.DashboardStatsDTO;
import com.djxlzk.dsasystem.dto.ResultDTO;

public interface StatsService {
    ResultDTO<DashboardStatsDTO> getDashboardStats();
}
