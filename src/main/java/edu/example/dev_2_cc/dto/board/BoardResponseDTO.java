package edu.example.dev_2_cc.dto.board;

import edu.example.dev_2_cc.entity.Board;
import edu.example.dev_2_cc.entity.Category;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class BoardResponseDTO {
    private Long boardId;
    private String title;
    private String description;
    private Category category;
    private String memberId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public BoardResponseDTO(Board board) {
        this.boardId = board.getBoardId();
        this.title = board.getTitle();
        this.description = board.getDescription();
        this.category = board.getCategory();
        this.memberId = board.getMember().getMemberId();
        this.createdAt = board.getCreatedAt();
        this.updatedAt = board.getUpdatedAt();

    }

}