package com.djxlzk.dsasystem.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.djxlzk.dsasystem.entity.Message;
import java.util.List;

public interface MessageService {
    Page<Message> getMessagesByStudentId(Long studentId, int page, int size);
    Page<Message> getMessagesByCoachId(Long coachId, int page, int size);
    boolean markAsRead(List<Long> ids);
    boolean deleteMessages(List<Long> ids);
    Message getMessageById(Long id);
}
