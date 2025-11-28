package com.human.jpa_sample.service;

import com.human.jpa_sample.dto.BoardResDto;
import com.human.jpa_sample.dto.BoardWriteDto;
import com.human.jpa_sample.entity.Board;
import com.human.jpa_sample.entity.Member;
import com.human.jpa_sample.repository.BoardRepository;
import com.human.jpa_sample.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    // 게시글 등록
    public boolean saveBoard(BoardWriteDto dto) {
        try {
            Board board = createBoardFromDto(dto); // dto -> entity
            boardRepository.save(board);
            return true;
        } catch (Exception e) {
            log.error("게시글 등록 실패 : {}", e.getMessage());
            return false;
        }
    }

    // 게시글 전체 조회
    public List<BoardResDto> getBoardList() {
        List<Board> boards = boardRepository.findAll();  // DB에서 게시글 목록 전체를 가져 옴
        List<BoardResDto> list = new ArrayList<>();  // 프론트엔드에 전달하기 위한 리스트
        for(Board board : boards) {
            list.add(convertEntityToDto(board));  // DB 게시글 row 를 dto 객체로 변환 후 리스트에 담음
        }
        return list;
    }

    // 게시글 단건 조회 (id)
    public BoardResDto getBoardDetail(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 게시글이 존재하지 않습니다."));
        return  convertEntityToDto(board);  // entity -> dto로 변환
    }

    // 게시글 수정
    public boolean modifyBoard(Long id, BoardWriteDto dto) {
        try {
            Board board = boardRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("해당 게시글이 존재 하지 않습니다"));

            Member member = memberRepository.findByEmail(dto.getEmail())
                    .orElseThrow(() -> new RuntimeException("회원이 존재 하지 않습니다."));

            board.setTitle(dto.getTitle());
            board.setContent(dto.getContent());
            board.setImage(dto.getImg());
            boardRepository.save(board);
            return true;
        } catch (Exception e) {
            log.error("게시글 수정 실패 : {}", e.getMessage());
            return false;
        }
    }

    // 페이지 네이션

    // 게시글 검색
    public List<BoardResDto> searchBoardList(String keyword) {
        List<Board> boards = boardRepository.findByTitleContaining(keyword);
        List<BoardResDto> list = new ArrayList<>();
        for (Board board : boards) {
            list.add(convertEntityToDto(board));
        }
        return list;
    }

    // 공통 메서드 : Dto -> Entity 로 변환
    private Board createBoardFromDto(BoardWriteDto dto) {
        Member member = memberRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("해당 회원이 존재 하지 않습니다."));

        return Board.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .image(dto.getImg())
                .member(member)
                .build();
    }

    // 공통 메서드 : Entity ->Dto 로 변환
    private BoardResDto convertEntityToDto(Board board) {
        return BoardResDto.builder()
                .boardId(board.getId())
                .email(board.getMember().getEmail())
                .title(board.getTitle())
                .img(board.getImage())
                .content(board.getContent())
                .regDate(board.getCreateTime())
                .build();
    }

}
