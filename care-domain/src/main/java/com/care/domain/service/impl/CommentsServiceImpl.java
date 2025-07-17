package com.care.domain.service.impl;

import com.care.domain.dto.CommentQueryRequest;
import com.care.domain.dto.CommentResponse;
import com.care.domain.service.CommentsService;
import com.care.infrastructure.repository.mapper.care.CommentsBizMapper;
import com.care.infrastructure.repository.model.care.Comments;
import com.care.infrastructure.utils.PageResult;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
}
