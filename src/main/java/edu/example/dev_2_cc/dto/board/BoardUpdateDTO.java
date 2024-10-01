package edu.example.dev_2_cc.dto.board;

import edu.example.dev_2_cc.entity.Board;
import edu.example.dev_2_cc.entity.Category;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BoardUpdateDTO {
    private Long boardId;
    private String title;
    private String description;
    private Category category;

    public Board toEntity(){
        Board board = Board.builder().boardId(this.boardId).title(this.title).description(this.description).category(this.category).build();
        return board;
    }
}