package edu.example.dev_2_cc.service;

import edu.example.dev_2_cc.dto.reply.ReplyRequestDTO;
import edu.example.dev_2_cc.dto.reply.ReplyResponseDTO;
import edu.example.dev_2_cc.entity.Board;
import edu.example.dev_2_cc.entity.Member;
import edu.example.dev_2_cc.entity.Reply;
import edu.example.dev_2_cc.exception.ReplyException;
import edu.example.dev_2_cc.repository.BoardRepository;
import edu.example.dev_2_cc.repository.MemberRepository;
import edu.example.dev_2_cc.repository.ProductRepository;
import edu.example.dev_2_cc.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class ReplyService {
    private final ReplyRepository replyRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;

    public ReplyResponseDTO createReply(ReplyRequestDTO replyRequestDTO) {
        try {

            String memberId = replyRequestDTO.getMemberId();
            Long boardId = replyRequestDTO.getBoardId();

            Member member = memberRepository.findById(memberId).orElseThrow();
            Board board = boardRepository.findById(boardId).orElseThrow();

            Reply reply = replyRequestDTO.toEntity(board, member);
            Reply savedReply = replyRepository.save(reply);

            return new ReplyResponseDTO(savedReply);

        }catch (Exception e) {

            log.error(e.getMessage());
            throw ReplyException.NOT_CREATED.get();

    public ReplyResponseDTO read(Long replyId) {
        try{
            Optional<Reply> foundReply = replyRepository.findById(replyId);
            Reply reply=foundReply.get();
            return new ReplyResponseDTO(reply);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw ReplyException.NOT_FOUND.get();
        }
    }
}
