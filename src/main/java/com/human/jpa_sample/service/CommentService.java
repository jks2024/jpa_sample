package com.human.jpa_sample.service;

import com.human.jpa_sample.dto.CommentResDto;
import com.human.jpa_sample.dto.CommentWriteDto;
import com.human.jpa_sample.entity.Board;
import com.human.jpa_sample.entity.Comment;
import com.human.jpa_sample.entity.Member;
import com.human.jpa_sample.repository.BoardRepository;
import com.human.jpa_sample.repository.CommentRepository;
import com.human.jpa_sample.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    // 댓글 등록
    public boolean saveComment(CommentWriteDto dto) {
        try {
            Comment comment = new Comment();
            Board board = boardRepository.findById(dto.getBoardId())
                    .orElseThrow(()-> new RuntimeException("해당 게시글이 존재하지 않습니다"));
            Member member = memberRepository.findByEmail(dto.getEmail())
                    .orElseThrow(() -> new RuntimeException("해당 회원이 존재하지 않습니다"));

            comment.setContent(dto.getContent());
            comment.setBoard(board);
            comment.setMember(member);
            commentRepository.save(comment);
            return true;
        } catch (Exception e) {
            log.error("댓글 등록 실패 : {}", e.getMessage());
            return false;
        }
    }

    // 댓글 수정
    public boolean modifyComment(Long id, CommentWriteDto dto) {
        try {
            Comment comment = commentRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("해당 댓글이 존재하지 않습니다."));
            comment.setContent(dto.getContent());
            commentRepository.save(comment);
            return true;
        } catch (Exception e) {
            log.error("댓글 수정 실패 : {}", e.getMessage());
            return false;
        }
    }

    // 댓글 삭제
    public boolean deleteComment(Long id) {
        try {
            Comment comment = commentRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("해당 댓글이 존재하지 않습니다."));
            commentRepository.delete(comment);
            return true;
        } catch (Exception e) {
            log.error("댓글 삭제 실패 : {}", e.getMessage());
            return false;
        }

    }

    // 댓글 목록 조회
    public List<CommentResDto> getCommentList(Long boardId) {
        try {
            Board board = boardRepository.findById(boardId)
                    .orElseThrow(() -> new RuntimeException("해당 게시글이 존재하지 않습니다."));

            List<Comment> comments = commentRepository.findByBoardId(boardId);
            List<CommentResDto> list = new ArrayList<>();
            for(Comment comment : comments) {
                list.add(convertEntityToDto(comment));
            }
            return list;
        } catch (Exception e) {
            log.error("댓글 목록 조회 실패 : {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    // 댓글 검색
    public List<CommentResDto> searchCommentList(String key) {
        List<Comment> comments = commentRepository.findByContentContaining(key);
        List<CommentResDto> list = new ArrayList<>();
        for (Comment e : comments) list.add(convertEntityToDto(e));
        return list;
    }

    // Entity -> Dto
    private CommentResDto convertEntityToDto(Comment comment) {
        return CommentResDto.builder()
                .commentId(comment.getId())
                .boardId(comment.getBoard().getId())
                .email(comment.getMember().getEmail())
                .content(comment.getContent())
                .regDate(comment.getRegTime())
                .build();
    }
}
