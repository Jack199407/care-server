package com.care.domain.service.impl;

import com.care.domain.dto.CommentCreateRequest;
import com.care.domain.dto.CommentQueryRequest;
import com.care.domain.dto.CommentResponse;
import com.care.domain.dto.CommentUpdateDisplayRequest;
import com.care.domain.service.CommentsService;
import com.care.infrastructure.repository.mapper.care.CommentsBizMapper;
import com.care.infrastructure.repository.model.care.Comments;
import com.care.infrastructure.utils.PageResult;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
@Service
@Log4j2
public class CommentsServiceImpl implements CommentsService {
    @Resource
    private CommentsBizMapper commentsBizMapper;

    @Override
    public PageResult<CommentResponse> getCommentsByPage(CommentQueryRequest request) {
        if (!request.isValid()) {
            throw new IllegalArgumentException("Display filter must include at least one value (true or false)");
        }

        int offset = (request.getPageNum() - 1) * request.getPageSize();
        int limit = request.getPageSize();
        List<Boolean> displayList = request.getDisplay();

        List<Comments> commentEntities = commentsBizMapper.selectByCondition(offset, limit, displayList);
        int total = commentsBizMapper.countByCondition(displayList);

        List<CommentResponse> responses = commentEntities.stream().map(entity -> {
            CommentResponse r = new CommentResponse();
            BeanUtils.copyProperties(entity, r);
            return r;
        }).collect(Collectors.toList());

        PageResult<CommentResponse> result = new PageResult<>();
        result.setRecords(responses);
        result.setTotal(total);
        result.setPageNum(request.getPageNum());
        result.setPageSize(request.getPageSize());
        return result;
    }

    @Override
    public void addComment(CommentCreateRequest request) {
        // default value for display is false
        Comments comment = new Comments();
        comment.setStar(request.getStar());
        comment.setContent(request.getContent());
        comment.setDisplay(false);
        comment.setCreateAt(LocalDateTime.now());
        comment.setUpdateAt(LocalDateTime.now());
        comment.setDeleteAt(0L);

        commentsBizMapper.insertComment(comment);
    }

    @Override
    public void updateDisplay(CommentUpdateDisplayRequest request) {
        int rows = commentsBizMapper.updateDisplayById(request.getId(), request.getDisplay());
        if (rows == 0) {
            throw new IllegalArgumentException("Comment not found or already deleted: id=" + request.getId());
        }
    }
}
