package com.djxlzk.dsasystem.controller;

import com.djxlzk.dsasystem.dto.ResultDTO;
import com.djxlzk.dsasystem.service.MessageService;
import com.djxlzk.dsasystem.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @GetMapping
    public ResultDTO<?> getMessages(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        Long studentId = JwtUtil.getUserIdFromRequest(request);
        return ResultDTO.success(messageService.getMessagesByStudentId(studentId, page, size));
    }

    @PostMapping("/read")
    public ResultDTO<?> markAsRead(@RequestBody List<Long> ids) {
        boolean success = messageService.markAsRead(ids);
        return success ? ResultDTO.success(null) : ResultDTO.error(500, "标记已读失败");
    }

    @DeleteMapping
    public ResultDTO<?> deleteMessages(@RequestParam("ids") String idsStr) {
        List<Long> ids = java.util.Arrays.stream(idsStr.split(","))
                .map(Long::parseLong)
                .toList();
        boolean success = messageService.deleteMessages(ids);
        return success ? ResultDTO.success(null) : ResultDTO.error(500, "删除消息失败");
    }

    @GetMapping("/{id}")
    public ResultDTO<?> getMessageById(@PathVariable Long id) {
        return ResultDTO.success(messageService.getMessageById(id));
    }
}
