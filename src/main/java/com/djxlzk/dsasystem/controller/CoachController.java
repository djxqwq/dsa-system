package com.djxlzk.dsasystem.controller;

import com.djxlzk.dsasystem.dto.CoachDTO;
import com.djxlzk.dsasystem.dto.ResultDTO;
import com.djxlzk.dsasystem.service.CoachService;
import com.djxlzk.dsasystem.service.StudentService;
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

    @Autowired
    private StudentService studentService;

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

    @GetMapping("/all")
    public ResultDTO<?> listAllCoaches() {
        return coachService.listAllCoaches();
    }

    @PutMapping("/workStatus")
    public ResultDTO<?> updateWorkStatus(@RequestBody java.util.Map<String, Object> body) {
        Long id = Long.valueOf(body.get("id").toString());
        Integer workStatus = Integer.valueOf(body.get("workStatus").toString());
        return coachService.updateWorkStatus(id, workStatus);
    }

    @PutMapping("/student/{studentId}/requiredHours")
    public ResultDTO<?> updateStudentRequiredHours(
            @PathVariable Long studentId,
            @RequestParam Integer requiredHours) {
        return studentService.updateRequiredHours(studentId, requiredHours);
    }

    @GetMapping("/students")
    public ResultDTO<?> listStudents(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return studentService.listStudents(keyword, status, page, size);
    }
}
