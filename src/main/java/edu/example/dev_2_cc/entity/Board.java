package edu.example.dev_2_cc.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private Category category;

    // Board 이미지 목록 필드
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<BoardImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @OrderBy("createdAt ASC") // 댓글을 생성일 순으로 조회
    @Builder.Default
    private List<Reply> replies = new ArrayList<>();
    // 빈 리스트로 먼저 초기화 -> 하지 않으면 null에러로 board 생성 불가
    // builder를 사용할 때도 기본 값으로 빈 리스트를 제공 -> 하지 않으면 null에러 board 생성 불가

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;


    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeDescription(String description) {
        this.description = description;
    }

    public void changeCategory(Category category) {
        this.category = category;
    }

    // 이미지 추가
    public void addImage(BoardImage image) {
        images.add(image);
        image.setBoard(this); // 양방향 연관관계 설정
    }

    // 이미지 삭제
    public void removeImage(BoardImage image) {
        images.remove(image);
        image.setBoard(null); // 양방향 연관관계 해제
    }

}
