package edu.example.dev_2_cc.dto.board;

import edu.example.dev_2_cc.entity.Board;
import edu.example.dev_2_cc.entity.Category;
import edu.example.dev_2_cc.entity.Member;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BoardRequestDTO {

    private String memberId;
    private String title;
    private String description;
    private Category category;

    public BoardRequestDTO(Board board) {
        this.memberId = board.getMember().getMemberId();
        this.title = board.getTitle();
        this.description = board.getDescription();
        this.category = board.getCategory();
    }

    public Board toEntity(Member member){
        Board board = Board.builder().member(member).title(this.title).description(this.description).category(this.category).build();

        return board;
    }

}
