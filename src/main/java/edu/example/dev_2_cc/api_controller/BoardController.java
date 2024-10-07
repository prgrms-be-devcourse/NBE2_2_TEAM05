package edu.example.dev_2_cc.api_controller;


import edu.example.dev_2_cc.dto.board.BoardListDTO;
import edu.example.dev_2_cc.dto.board.BoardRequestDTO;
import edu.example.dev_2_cc.dto.board.BoardResponseDTO;
import edu.example.dev_2_cc.dto.board.BoardUpdateDTO;
import edu.example.dev_2_cc.dto.reply.ReplyListDTO;
import edu.example.dev_2_cc.dto.review.PageRequestDTO;
import edu.example.dev_2_cc.exception.BoardException;
import edu.example.dev_2_cc.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cc/board")
@Log4j2
public class BoardController {
    private final BoardService boardService;

    //-----------------------------------------------게시판----------------------------------------------------

    // Board 단일 조회 -> ADMIN/USER/ANONYMOUS
    @GetMapping("/{boardId}")
    public ResponseEntity<BoardResponseDTO> getBoard(@PathVariable("boardId") Long boardId) {
        return ResponseEntity.ok(boardService.read(boardId));
    }

    // Board 전체 리스트 조회 -> ADMIN/USER/ANONYMOUS
    @GetMapping
    public ResponseEntity<Page<BoardListDTO>> getBoardList(PageRequestDTO pageRequestDTO) {
        return ResponseEntity.ok(boardService.getList(pageRequestDTO));
    }

    // Board 등록 -> ADMIN/USER
    @PostMapping("/") // "/cc/board" 경로는 누구라도 접근 가능하기 때문에 "/"로 비회원 접근 차단
    public ResponseEntity<BoardResponseDTO> createBoard(@Validated @RequestBody BoardRequestDTO boardRequestDTO) {
        return ResponseEntity.ok(boardService.createBoard(boardRequestDTO));
    }

    // Board 삭제 -> ADMIN/USER
    @DeleteMapping("/{boardId}")
    public ResponseEntity<Map<String, String>> deleteboard(@PathVariable("boardId") Long boardId) {
        boardService.delete(boardId);
        return ResponseEntity.ok(Map.of("message", "Board deleted"));
    }

    // Board 수정 -> ADMIN/USER
    @PutMapping("/{boardId}")
    public ResponseEntity<BoardResponseDTO> updateBoard(@PathVariable Long boardId, @RequestBody BoardUpdateDTO boardUpdateDTO) {
        if(!boardId.equals(boardUpdateDTO.getBoardId())) {
            throw BoardException.NOT_FOUND.get();
        }
        return ResponseEntity.ok(boardService.updateBoard(boardUpdateDTO));
    }

    // Board 멤버 별로 조회 -> ADMIN/해당USER -> AdminController/MypageController
    @GetMapping("/listByMember/{memberId}") // Member ID로 board 리스트 조회
    public ResponseEntity<List<BoardListDTO>> listByMemberId(@Validated @PathVariable("memberId") String memberId) {
        List<BoardListDTO> boards = boardService.listByMemberId(memberId);
        return ResponseEntity.ok(boards);
    }

}