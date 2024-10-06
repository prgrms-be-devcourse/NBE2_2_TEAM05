package edu.example.dev_2_cc.repository;

import edu.example.dev_2_cc.entity.Board;
import edu.example.dev_2_cc.entity.Member;
import edu.example.dev_2_cc.entity.Reply;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Log4j2
public class ReplyRepositoryTests {

    @Autowired
    private ReplyRepository replyRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Test
    public void testCreate(){
        Member member = memberRepository.findById("lego").orElseThrow();
        Board board = boardRepository.findById(1L).orElseThrow();

        Reply reply = Reply.builder().content("꿀팁").member(member).board(board).build();
        Reply savedReply = replyRepository.save(reply);

        assertNotNull(savedReply);
    }

}
