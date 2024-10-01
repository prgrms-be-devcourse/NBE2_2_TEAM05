package edu.example.dev_2_cc.api_controller;


import edu.example.dev_2_cc.dto.board.BoardRequestDTO;
import edu.example.dev_2_cc.dto.board.BoardResponseDTO;
import edu.example.dev_2_cc.dto.board.BoardUpdateDTO;
import edu.example.dev_2_cc.exception.BoardException;
import edu.example.dev_2_cc.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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

    @PutMapping("/{boardId}")
    public ResponseEntity<BoardResponseDTO> updateBoard(@PathVariable Long boardId, @RequestBody BoardUpdateDTO boardUpdateDTO) {

        if(!boardId.equals(boardUpdateDTO.getBoardId())) {
            throw BoardException.NOT_FOUND.get();
        }

        return ResponseEntity.ok(boardService.updateBoard(boardUpdateDTO));
    }
}
