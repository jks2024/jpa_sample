package com.human.jpa_sample.repository;

import com.human.jpa_sample.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    // 제목에 특정 문자열이 포함되었는지 검색 (LIKE 검색)
    List<Board> findByTitleContaining(String title);

    // email로 게시글 검색하기 (결과를 List<Board>)
    List<Board> findByMemberEmail(String email); // entity 이름 필요

    // 페이지네이션
}
