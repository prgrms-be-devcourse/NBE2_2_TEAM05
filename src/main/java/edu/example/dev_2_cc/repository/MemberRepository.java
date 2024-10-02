package edu.example.dev_2_cc.repository;

import edu.example.dev_2_cc.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, String> {

    //name을 받아 DB 테이블에서 회원을 조회하는 메소드 작성
    Member findByMemberId(String memberId);
}
