package edu.example.dev_2_cc.dto.board;

import edu.example.dev_2_cc.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardListDTO {
    private Long boardId;
    private String title;
    private Category category;
    private String memberId;

}
