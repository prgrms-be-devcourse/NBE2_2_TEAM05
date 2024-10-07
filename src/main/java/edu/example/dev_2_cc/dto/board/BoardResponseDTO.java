package edu.example.dev_2_cc.dto.board;

import edu.example.dev_2_cc.dto.reply.ReplyListDTO;
import edu.example.dev_2_cc.dto.reply.ReplyResponseDTO;
import edu.example.dev_2_cc.entity.Board;
import edu.example.dev_2_cc.entity.BoardImage;
import edu.example.dev_2_cc.entity.Category;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class BoardResponseDTO {
    private Long boardId;
    private String title;
    private String description;
    private Category category;
    private String memberId;
    private String thumbnail; // 썸네일 필드 추가
    private List<String> imageFilenames; // 이미지 파일 이름들을 담을 리스트 추가
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<ReplyResponseDTO> replies; // 댓글 리스트 필드 추가

    public BoardResponseDTO(Board board) {
        this.boardId = board.getBoardId();
        this.title = board.getTitle();
        this.description = board.getDescription();
        this.category = board.getCategory();
        this.memberId = board.getMember().getMemberId();

        // 해당 멤버의 "s_"로 시작하는 썸네일 이미지 지정
        this.thumbnail = "s_" + board.getMember().getImage().getFilename();

        // BoardImage 리스트를 순회하며 Filename을 추출하고 문자열 리스트로 변환하여 추가
        this.imageFilenames = board.getImages().stream()
                .map(image -> image.getFilename())
                .collect(Collectors.toList());

        this.createdAt = board.getCreatedAt();
        this.updatedAt = board.getUpdatedAt();

        // 댓글 리스트를 ReplyListDTO로 변환하여 추가
        this.replies = board.getReplies().stream()
                .map(ReplyResponseDTO::new)
                .collect(Collectors.toList());
    }

}
