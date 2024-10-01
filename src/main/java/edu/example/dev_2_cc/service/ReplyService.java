package edu.example.dev_2_cc.service;

import edu.example.dev_2_cc.dto.reply.ReplyResponseDTO;
import edu.example.dev_2_cc.entity.Reply;
import edu.example.dev_2_cc.exception.ReplyException;
import edu.example.dev_2_cc.repository.BoardRepository;
import edu.example.dev_2_cc.repository.MemberRepository;
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

    public void delete(Long replyId) {
        Optional<Reply> foundReply = replyRepository.findById(replyId);
        Reply reply = foundReply.orElseThrow(ReplyException.NOT_FOUND::get);

        try {
            replyRepository.delete(reply);
        }catch (Exception e) {
            log.error(e.getMessage());
            throw ReplyException.NOT_DELETED.get();
        }
    }
}
