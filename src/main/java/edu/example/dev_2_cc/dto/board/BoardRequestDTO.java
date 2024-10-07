package edu.example.dev_2_cc.dto.board;

import edu.example.dev_2_cc.entity.Board;
import edu.example.dev_2_cc.entity.Category;
import edu.example.dev_2_cc.entity.Member;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@NoArgsConstructor
public class BoardRequestDTO {

    private String memberId;
    private String title;
    private String description;
    private Category category;

    // 사용하지 않아서 일단 주석처리 했습니다.
//    public BoardRequestDTO(Board board) {
//        this.memberId = board.getMember().getMemberId();
//        this.title = board.getTitle();
//        this.description = board.getDescription();
//        this.category = board.getCategory();
//    }

    public Board toEntity(Member member){
        Board board = Board.builder().member(member).title(this.title).description(this.description).category(this.category).build();

        return board;
    }
}

