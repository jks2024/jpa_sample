package com.human.jpa_sample.repository;

import com.human.jpa_sample.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    // 댓글 내용 조회

    // 게시글 조회
}
