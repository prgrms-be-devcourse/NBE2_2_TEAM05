package edu.example.dev_2_cc.service;

import edu.example.dev_2_cc.dto.board.BoardRequestDTO;
import edu.example.dev_2_cc.dto.board.BoardResponseDTO;
import edu.example.dev_2_cc.dto.board.BoardUpdateDTO;
import edu.example.dev_2_cc.entity.Board;
import edu.example.dev_2_cc.entity.Member;
import edu.example.dev_2_cc.exception.BoardException;
import edu.example.dev_2_cc.repository.BoardRepository;
import edu.example.dev_2_cc.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
        }catch (Exception e) {
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
        try{
            Optional<Board> foundBoard = boardRepository.findById(boardId);
            Board board = foundBoard.get();
            return new BoardResponseDTO(board);
        }catch (Exception e){
            log.error(e.getMessage());
            throw BoardException.NOT_FOUND.get();
        }
    }
}
