package edu.example.dev_2_cc.service;

import edu.example.dev_2_cc.dto.board.BoardListDTO;
import edu.example.dev_2_cc.dto.board.BoardRequestDTO;
import edu.example.dev_2_cc.dto.board.BoardResponseDTO;
import edu.example.dev_2_cc.dto.board.BoardUpdateDTO;
import edu.example.dev_2_cc.dto.reply.ReplyListDTO;
import edu.example.dev_2_cc.dto.review.PageRequestDTO;
import edu.example.dev_2_cc.entity.Board;
import edu.example.dev_2_cc.entity.Member;
import edu.example.dev_2_cc.entity.Reply;
import edu.example.dev_2_cc.entity.Review;
import edu.example.dev_2_cc.exception.BoardException;
import edu.example.dev_2_cc.exception.MemberException;
import edu.example.dev_2_cc.exception.ReviewException;
import edu.example.dev_2_cc.repository.BoardRepository;
import edu.example.dev_2_cc.repository.MemberRepository;
import edu.example.dev_2_cc.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class BoardService {
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;

    public BoardResponseDTO createBoard(BoardRequestDTO boardRequestDTO) {
        try {
            String memberId = boardRequestDTO.getMemberId();

            Member member = memberRepository.findById(memberId).orElseThrow();

            Board board = boardRequestDTO.toEntity(member);
            Board savedBoard = boardRepository.save(board);

            return new BoardResponseDTO(savedBoard);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw BoardException.NOT_CREATED.get();
        }
    }

    public BoardResponseDTO updateBoard(BoardUpdateDTO boardUpdateDTO) {
        Optional<Board> foundBoard = boardRepository.findById(boardUpdateDTO.getBoardId());
        Board board = foundBoard.orElseThrow(BoardException.NOT_FOUND::get);

        try {
            board.changeTitle(boardUpdateDTO.getTitle());
            board.changeDescription(boardUpdateDTO.getDescription());
            board.changeCategory(boardUpdateDTO.getCategory());

            return new BoardResponseDTO(boardRepository.save(board));
        }catch (Exception e) {
            log.error(e.getMessage());
            throw BoardException.NOT_UPDATED.get();
        }
    }

    public BoardResponseDTO read(Long boardId) {
        try {
            Optional<Board> foundBoard = boardRepository.findById(boardId);
            Board board = foundBoard.get();
            return new BoardResponseDTO(board);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw BoardException.NOT_FOUND.get();
        }
    }

    public Page<BoardListDTO> getList(PageRequestDTO pageRequestDTO) {
        try {
            Sort sort = Sort.by("createdAt").descending();
            Pageable pageable = pageRequestDTO.getPageable(sort);
            Page<BoardListDTO> boardList = boardRepository.list(pageable);
            return boardList;
        }catch (Exception e){
            log.error(e.getMessage());
            throw BoardException.NOT_FOUND.get();   //임시
        }
    }

    public void delete(Long boardId) {
        Optional<Board> foundBoard = boardRepository.findById(boardId);
        Board board = foundBoard.orElseThrow(BoardException.NOT_FOUND::get);

        try{
            boardRepository.delete(board);
        }catch (Exception e){
            log.error("--- " + e.getMessage());
            throw ReviewException.NOT_DELETED.get();
        }
    }

    // Member ID 로 Reply 리스트 조회
    public List<BoardListDTO> listByMemberId(String memberId) {
        List<Board> boards = boardRepository.findAllByMember(memberId);

        if (boards.isEmpty()) {
            throw MemberException.NOT_FOUND.get();
        }

        return boards.stream()
                .map(BoardListDTO::new)
                .collect(Collectors.toList());
    }

}