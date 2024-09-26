package edu.example.dev_2_cc.repository;

import edu.example.dev_2_cc.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, String> {
}
