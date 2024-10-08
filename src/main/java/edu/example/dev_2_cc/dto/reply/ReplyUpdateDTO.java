package edu.example.dev_2_cc.dto.reply;

import edu.example.dev_2_cc.entity.Reply;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReplyUpdateDTO {

    private String content;
    private Long replyId;
    private String memberId; // 작성자,관리자 등 식별을 위한 필드 추가

    public Reply toEntity() {
        return Reply.builder()
                .replyId(replyId)
                .content(content)
                .build();
    }

}