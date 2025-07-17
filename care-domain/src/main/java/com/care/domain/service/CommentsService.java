package com.care.domain.service;

import com.care.domain.dto.CommentCreateRequest;
import com.care.domain.dto.CommentQueryRequest;
import com.care.domain.dto.CommentResponse;
import com.care.domain.dto.CommentUpdateDisplayRequest;
import com.care.infrastructure.utils.PageResult;

public interface CommentsService {
    PageResult<CommentResponse> getCommentsByPage(CommentQueryRequest request);

    void addComment(CommentCreateRequest request);

    void updateDisplay(CommentUpdateDisplayRequest request);

    void deleteById(Integer id);
}
