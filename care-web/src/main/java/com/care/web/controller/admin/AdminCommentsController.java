package com.care.web.controller.admin;

import com.care.domain.dto.CommentQueryRequest;
import com.care.domain.dto.CommentResponse;
import com.care.domain.dto.CommentUpdateDisplayRequest;
import com.care.domain.service.CommentsService;
import com.care.infrastructure.utils.PageResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/admin/care/")
public class AdminCommentsController {
    @Resource
    private CommentsService commentsService;

    @PostMapping("/comments/list")
    public ResponseEntity<PageResult<CommentResponse>> listComments(@RequestBody CommentQueryRequest request) {
        PageResult<CommentResponse> result = commentsService.getCommentsByPage(request);
        return ResponseEntity.ok(result);
    }

    @PostMapping("comments/update")
    public ResponseEntity<?> updateDisplay(@RequestBody CommentUpdateDisplayRequest request) {
        commentsService.updateDisplay(request);
        return ResponseEntity.ok("Display status updated successfully");
    }
}
