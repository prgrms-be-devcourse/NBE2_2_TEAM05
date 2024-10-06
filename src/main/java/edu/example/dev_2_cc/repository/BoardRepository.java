package edu.example.dev_2_cc.repository;

import edu.example.dev_2_cc.entity.Board;
import edu.example.dev_2_cc.entity.Reply;
import edu.example.dev_2_cc.repository.search.BoardSearch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board,Long>, BoardSearch {

    // Member ID 로 Board 리스트 조회
    @Query("SELECT b FROM Board b WHERE b.member.memberId = :memberId")
    List<Board> findAllByMember(String memberId);

}
