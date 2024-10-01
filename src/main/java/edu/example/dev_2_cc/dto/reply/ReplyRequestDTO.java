package edu.example.dev_2_cc.dto.reply;

import edu.example.dev_2_cc.entity.Board;
import edu.example.dev_2_cc.entity.Member;
import edu.example.dev_2_cc.entity.Reply;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@Getter
@Setter
public class ReplyRequestDTO {
    private String content;

    @NotEmpty
    private String memberId;

    @NotEmpty
    private Long boardId;

    public Reply toEntity(Board board, Member member){
        Reply reply = Reply.builder().member(member).board(board).content(content).build();
        return reply;
    }
}
