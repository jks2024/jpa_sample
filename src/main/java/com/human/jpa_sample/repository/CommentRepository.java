package com.human.jpa_sample.repository;

import com.human.jpa_sample.entity.Board;
import com.human.jpa_sample.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    // 댓글 내용 조회
    List<Comment> findByContentContaining(String keyword);

    // 댓글 조회
    List<Comment> findByBoardId(Long boardId);
}
