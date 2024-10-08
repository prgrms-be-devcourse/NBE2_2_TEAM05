package edu.example.dev_2_cc.dto.board;

import edu.example.dev_2_cc.entity.Board;
import edu.example.dev_2_cc.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardListDTO {

    private Long boardId;
    private String title;
    private Category category;
    private String memberId;
    private LocalDateTime createdAt;

    public BoardListDTO(Board board) {
        this.boardId = board.getBoardId();
        this.title = board.getTitle();
        this.category = board.getCategory();
        this.memberId = board.getMember().getMemberId();
        this.createdAt = board.getCreatedAt();
    }

}
