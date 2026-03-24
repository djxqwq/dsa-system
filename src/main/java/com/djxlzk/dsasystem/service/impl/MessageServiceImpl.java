package com.djxlzk.dsasystem.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.djxlzk.dsasystem.entity.Message;
import com.djxlzk.dsasystem.mapper.MessageMapper;
import com.djxlzk.dsasystem.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageMapper messageMapper;

    @Override
    public Page<Message> getMessagesByStudentId(Long studentId, int page, int size) {
        Page<Message> messagePage = new Page<>(page, size);
        List<Message> messages = messageMapper.selectByStudentId(studentId, (page - 1) * size, size);
        int total = messageMapper.countByStudentId(studentId);
        messagePage.setRecords(messages);
        messagePage.setTotal(total);
        return messagePage;
    }

    @Override
    public boolean markAsRead(List<Long> ids) {
        String idsStr = String.join(",", ids.stream().map(String::valueOf).toArray(String[]::new));
        return messageMapper.updateReadStatus(idsStr, 1) > 0;
    }

    @Override
    public boolean deleteMessages(List<Long> ids) {
        return messageMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public Message getMessageById(Long id) {
        return messageMapper.selectById(id);
    }
}
