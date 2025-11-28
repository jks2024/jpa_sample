package com.human.jpa_sample.controller;

import com.human.jpa_sample.dto.BoardResDto;
import com.human.jpa_sample.dto.BoardWriteDto;
import com.human.jpa_sample.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/board")
public class BoardController {
    private final BoardService boardService;  // 의존성 주입

    // 게시글 등록
    @PostMapping("/new")
    public ResponseEntity<Boolean> boardWrite(@RequestBody BoardWriteDto dto) {
        return ResponseEntity.ok(boardService.saveBoard(dto));
    }

    // 게시글 전체 조회
    @GetMapping("/list")
    public ResponseEntity<List<BoardResDto>> boardList() {
        return ResponseEntity.ok(boardService.getBoardList());
    }

    // 게시글 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<BoardResDto> boardDetail(@PathVariable Long id) {
        return ResponseEntity.ok(boardService.getBoardDetail(id));
    }

    // 게시글 수정
    @PutMapping("/{id}")
    public ResponseEntity<Boolean> boardModify(@PathVariable Long id, @RequestBody BoardWriteDto dto) {
        return ResponseEntity.ok(boardService.modifyBoard(id, dto));
    }

    // 게시글 페이지네이션

    // 게시글 검색
    @GetMapping("/search")
    public ResponseEntity<List<BoardResDto>> searchBoards(@RequestParam String keyword) {
        return ResponseEntity.ok(boardService.searchBoardList(keyword));
    }
}
