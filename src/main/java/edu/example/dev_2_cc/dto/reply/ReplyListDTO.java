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
    private String thumbnail; // 작성자의 프로필 썸네일 필드 추가
    private String memberId;
    private Long boardId;

    public ReplyListDTO(Reply reply) {
        this.replyId = reply.getReplyId();
        this.content = reply.getContent();

        this.thumbnail = "s_" + reply.getMember().getImage().getFilename();

        this.memberId = reply.getMember().getMemberId();
        this.boardId = reply.getBoard().getBoardId();
    }

}
