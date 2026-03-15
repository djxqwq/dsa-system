package com.djxlzk.dsasystem.controller;

import com.djxlzk.dsasystem.dto.ResultDTO;
import com.djxlzk.dsasystem.dto.StudentDTO;
import com.djxlzk.dsasystem.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student")
@Validated
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PostMapping
    public ResultDTO<?> addStudent(@RequestBody @Validated StudentDTO studentDTO) {
        return studentService.addStudent(studentDTO);
    }

    @DeleteMapping("/{id}")
    public ResultDTO<?> deleteStudent(@PathVariable Long id) {
        return studentService.deleteStudent(id);
    }

    @PutMapping
    public ResultDTO<?> updateStudent(@RequestBody @Validated StudentDTO studentDTO) {
        return studentService.updateStudent(studentDTO);
    }

    @GetMapping("/list")
    public ResultDTO<?> listStudents(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return studentService.listStudents(keyword, status, page, size);
    }

    @PutMapping("/resetPassword/{id}")
    public ResultDTO<?> resetPassword(@PathVariable Long id) {
        return studentService.resetPassword(id);
    }
}
