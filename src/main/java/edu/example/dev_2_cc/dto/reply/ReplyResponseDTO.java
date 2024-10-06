package edu.example.dev_2_cc.dto.reply;

import edu.example.dev_2_cc.entity.Reply;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ReplyResponseDTO {
    private Long replyId;
    private Long boardId;
    private String memberId;
    private String content;
    private String thumbnail; // 작성자의 프로필 썸네일 필드 추가
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ReplyResponseDTO(Reply reply) {
        this.replyId = reply.getReplyId();
        this.boardId = reply.getBoard().getBoardId();
        this.memberId = reply.getMember().getMemberId();
        this.content = reply.getContent();

        // 해당 멤버의 "s_"로 시작하는 썸네일 이미지 지정
        this.thumbnail = "s_" + reply.getMember().getImage().getFilename();

        this.createdAt = reply.getCreatedAt();
        this.updatedAt = reply.getUpdatedAt();
    }

}