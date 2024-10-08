package edu.example.dev_2_cc.service;

import edu.example.dev_2_cc.dto.reply.ReplyListDTO;
import edu.example.dev_2_cc.dto.reply.ReplyRequestDTO;
import edu.example.dev_2_cc.dto.reply.ReplyResponseDTO;
import edu.example.dev_2_cc.dto.reply.ReplyUpdateDTO;
import edu.example.dev_2_cc.entity.Board;
import edu.example.dev_2_cc.entity.Member;
import edu.example.dev_2_cc.entity.Reply;
import edu.example.dev_2_cc.exception.*;
import edu.example.dev_2_cc.repository.BoardRepository;
import edu.example.dev_2_cc.repository.MemberRepository;
import edu.example.dev_2_cc.repository.ReplyRepository;
import edu.example.dev_2_cc.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class ReplyService {
    private final ReplyRepository replyRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final SecurityUtil securityUtil;

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
        }
    }

    public ReplyResponseDTO update(ReplyUpdateDTO replyUpdateDTO){
        Optional<Reply> foundReply = replyRepository.findById(replyUpdateDTO.getReplyId());
        Reply reply=foundReply.orElseThrow(ReplyException.NOT_FOUND::get);
        try {
            reply.changeContent(replyUpdateDTO.getContent());

            return new ReplyResponseDTO(replyRepository.save(reply));
        }catch (Exception e) {
            log.error(e.getMessage());
            throw ReplyException.NOT_UPDATED.get();
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

    // Member ID 로 Reply 리스트 조회
    public List<ReplyListDTO> listByMemberId(String memberId) {
        List<Reply> replies = replyRepository.findAllByMember(memberId);

        if (replies.isEmpty()) {
            throw MemberException.NOT_FOUND.get();
        }

        return replies.stream()
                .map(ReplyListDTO::new) // ReplyListDTO(Reply reply) 생성자 사용
                .collect(Collectors.toList());
    }

    public boolean checkDeleteReplyAuthorization(Long replyId) {
        try {
            Reply reply = replyRepository.findById(replyId).orElseThrow(ReplyException.NOT_FOUND::get);
            Member currentUser = securityUtil.getCurrentUser();

            // 관리자이거나, 댓글 작성자이거나, 해당 댓글이 달린 게시판의 작성자인지 확인
            if (currentUser.getMemberId().equals(reply.getMember().getMemberId()) ||
                    currentUser.getMemberId().equals(reply.getBoard().getMember().getMemberId()) ||
                    "ROLE_ADMIN".equals(securityUtil.getCurrentUserRole())) {
                return true;
            }

            // 권한이 없는 경우 예외 발생
            throw new AccessDeniedException();
        } catch (AuthorizationException e) {
            // 예외가 발생하면 false 반환
            return false;
        }
    }

    //    public ReplyResponseDTO read(Long replyId) {
//        try{
//            Optional<Reply> foundReply = replyRepository.findById(replyId);
//            Reply reply=foundReply.get();
//            return new ReplyResponseDTO(reply);
//        } catch (Exception e) {
//            log.error(e.getMessage());
//            throw ReplyException.NOT_FOUND.get();
//        }
//    }

    //    // Board ID 로 Reply 리스트 조회
//    public List<ReplyListDTO> listByBoard(Long boardId){
//        List<Reply> replies = replyRepository.findAllByBoard(boardId);
//
//        if (replies.isEmpty()) {
//            throw BoardException.NOT_FOUND.get();
//        }
//
//        return replies.stream()
//                .map(ReplyListDTO::new) // ReplyListDTO(Reply reply) 생성자 사용
//                .collect(Collectors.toList());
//    }

}