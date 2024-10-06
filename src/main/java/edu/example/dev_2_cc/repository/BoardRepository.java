package edu.example.dev_2_cc.repository;

import edu.example.dev_2_cc.entity.Board;
import edu.example.dev_2_cc.entity.Reply;
import edu.example.dev_2_cc.repository.search.BoardSearch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board,Long>, BoardSearch {

    // MemberID 로 Board 리스트 조회
    @Query("SELECT b FROM Board b WHERE b.member.memberId = :memberId")
    List<Board> findAllByMember(String memberId);

    // BoardId, filename으로 이미지 개별 삭제

    @Modifying // 수정, 삽입, 삭제에 필요한 어노테이션
    //    @Query 조회에 필요한 어노테이션
    @Transactional
    @Query("DELETE FROM BoardImage bi WHERE bi.board.boardId = :boardId AND bi.filename = :filename")
    void removeImage(Long boardId, String filename);

}
