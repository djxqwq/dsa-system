package com.djxlzk.dsasystem.controller;

import com.djxlzk.dsasystem.dto.CoachDTO;
import com.djxlzk.dsasystem.dto.ResultDTO;
import com.djxlzk.dsasystem.service.CoachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/coach")
@Validated
public class CoachController {

    @Autowired
    private CoachService coachService;

    @PostMapping
    public ResultDTO<?> addCoach(@RequestBody @Validated CoachDTO coachDTO) {
        return coachService.addCoach(coachDTO);
    }

    @DeleteMapping("/{id}")
    public ResultDTO<?> deleteCoach(@PathVariable Long id) {
        return coachService.deleteCoach(id);
    }

    @PutMapping
    public ResultDTO<?> updateCoach(@RequestBody @Validated CoachDTO coachDTO) {
        return coachService.updateCoach(coachDTO);
    }

    @GetMapping("/list")
    public ResultDTO<?> listCoaches(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return coachService.listCoaches(keyword, page, size);
    }

    @PutMapping("/resetPassword/{id}")
    public ResultDTO<?> resetPassword(@PathVariable Long id) {
        return coachService.resetPassword(id);
    }
}
