package com.djxlzk.dsasystem.controller;

import com.djxlzk.dsasystem.dto.DashboardStatsDTO;
import com.djxlzk.dsasystem.dto.ResultDTO;
import com.djxlzk.dsasystem.service.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stats")
public class StatsController {

    @Autowired
    private StatsService statsService;

    @GetMapping("/dashboard")
    public ResultDTO<DashboardStatsDTO> getDashboardStats() {
        return statsService.getDashboardStats();
    }
}
