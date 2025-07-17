package com.care.web.controller;

import com.care.domain.dto.CommentCreateRequest;
import com.care.domain.dto.CommentQueryRequest;
import com.care.domain.dto.CommentResponse;
import com.care.domain.service.CommentsService;
import com.care.infrastructure.utils.PageResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collections;

@RestController
@RequestMapping("/care/")
public class CommentsController {
    @Resource
    private CommentsService commentsService;

    @PostMapping("/comments/list")
    public ResponseEntity<PageResult<CommentResponse>> listComments(@RequestBody CommentQueryRequest request) {
        request.setDisplay(Collections.singletonList(true));
        PageResult<CommentResponse> result = commentsService.getCommentsByPage(request);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/comments/add")
    public ResponseEntity<?> addComment(@RequestBody CommentCreateRequest request) {
        commentsService.addComment(request);
        return ResponseEntity.ok("Comment added successfully");
    }
}
