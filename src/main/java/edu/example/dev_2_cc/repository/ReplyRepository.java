package edu.example.dev_2_cc.repository;

import edu.example.dev_2_cc.dto.reply.ReplyListDTO;
import edu.example.dev_2_cc.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    // Board ID 로 Reply 리스트 조회
    @Query("SELECT r FROM Reply r WHERE r.board.boardId = :boardId")
    List<Reply> findAllByBoard(Long boardId);

    // Member ID 로 Reply 리스트 조회
    @Query("SELECT r FROM Reply r WHERE r.member.memberId = :memberId")
    List<Reply> findAllByMember(String memberId);

}
