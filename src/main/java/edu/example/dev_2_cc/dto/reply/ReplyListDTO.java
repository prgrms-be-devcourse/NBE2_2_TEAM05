package edu.example.dev_2_cc.dto.reply;

import edu.example.dev_2_cc.entity.Reply;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReplyListDTO {
    private Long replyId;
    private String content;
    private String memberId;
    private Long boardId;

    public ReplyListDTO(Reply reply) {
        this.replyId = reply.getReplyId();
        this.content = reply.getContent();
        this.memberId = reply.getMember().getMemberId();
        this.boardId = reply.getBoard().getBoardId();
    }

}
