package com.human.jpa_sample.controller;

import com.human.jpa_sample.dto.CommentResDto;
import com.human.jpa_sample.dto.CommentWriteDto;
import com.human.jpa_sample.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    // 댓글 등록
    @PostMapping("/new")
    public ResponseEntity<Boolean> commentWrite(@RequestBody CommentWriteDto dto) {
        return ResponseEntity.ok(commentService.saveComment(dto));
    }

    // 댓글 수정
    @PutMapping("/{id}")
    public ResponseEntity<Boolean> commentModify(@PathVariable Long id, @RequestBody CommentWriteDto dto) {
        return ResponseEntity.ok(commentService.modifyComment(id, dto));
    }

    // 댓글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> commentDelete(@PathVariable Long id) {
        return ResponseEntity.ok(commentService.deleteComment(id));
    }
    // 댓글 목록 조회
    @GetMapping("/{id}")
    public ResponseEntity<List<CommentResDto>> commentList(@PathVariable Long id) {
        return ResponseEntity.ok(commentService.getCommentList(id));
    }
    // 댓글 검색
    @GetMapping("/search")
    public ResponseEntity<List<CommentResDto>> commentSearch(@RequestParam String key) {
        return ResponseEntity.ok(commentService.searchCommentList(key));
    }
}
