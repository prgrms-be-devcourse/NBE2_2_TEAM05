package edu.example.dev_2_cc.dto.reply;

import edu.example.dev_2_cc.entity.Reply;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReplyUpdateDTO {

    private Long replyId;

    private String content;

    public Reply toEntity(){
        Reply reply = Reply.builder().replyId(replyId).content(content).build();

        return reply;
    }
}