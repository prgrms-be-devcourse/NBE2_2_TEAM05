package edu.example.dev_2_cc.api_controller;


import edu.example.dev_2_cc.dto.board.BoardRequestDTO;
import edu.example.dev_2_cc.dto.board.BoardResponseDTO;
import edu.example.dev_2_cc.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cc/board")
@Log4j2
public class BoardController {
    private final BoardService boardService;

    @GetMapping("/{boardId}")
    public ResponseEntity<BoardResponseDTO> getBoard(@PathVariable("boardId") Long boardId) {
        return ResponseEntity.ok(boardService.read(boardId));
    }
  
    @PostMapping
    public ResponseEntity<BoardResponseDTO> createBoard(@RequestBody BoardRequestDTO boardRequestDTO) {
        return ResponseEntity.ok(boardService.createBoard(boardRequestDTO));
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<Map<String, String>> deleteboard(@PathVariable("boardId") Long boardId) {
        boardService.delete(boardId);
        return ResponseEntity.ok(Map.of("message", "Board deleted"));

    }
}
