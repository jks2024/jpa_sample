package com.human.jpa_sample.repository;

import com.human.jpa_sample.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    // null 방지를 위해 사용하는 Wrapper Calss 임
    Optional<Member> findByEmail(String email);  // 이메일을 전달 받아 회원 정보를 반환
    boolean existsByEmail(String email);  // 이메일을 전달 받아 가입 여부 확인
    Optional<Member> findByEmailAndPwd(String email, String pwd); // 로그인 성공/실패

}
