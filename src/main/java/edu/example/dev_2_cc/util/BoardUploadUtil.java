package edu.example.dev_2_cc.util;

import edu.example.dev_2_cc.entity.Board;
import edu.example.dev_2_cc.entity.BoardImage;
import edu.example.dev_2_cc.exception.BoardException;
import edu.example.dev_2_cc.repository.BoardRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@Log4j2
@RequiredArgsConstructor
public class BoardUploadUtil {

    @Value("${edu.example.upload.path}")
    private String uploadPath;

    private final BoardRepository boardRepository;
    private final SecurityUtil securityUtil;

    @PostConstruct
    public void init() {
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            log.info("--- uploadDir 생성 : " + uploadDir);
            uploadDir.mkdir();
        }
        uploadPath = uploadDir.getAbsolutePath();
        log.info("--- uploadPath : " + uploadPath);
    }

    // 다중 파일 업로드 메서드
    public List<String> upload(MultipartFile[] files, Long boardId) {
        List<String> filenames = new ArrayList<>();

        if (files == null || files.length == 0) {
            log.info("No files provided for upload.");
            return filenames; // 파일이 없는 경우 빈 리스트 반환
        }

        Board board = boardRepository.findById(boardId).orElseThrow(BoardException.NOT_FOUND::get);

        for (MultipartFile file : files) {
            if (file.isEmpty()) continue; // 빈 파일은 건너뜀

            // 파일이 이미지인지 체크
            if (!file.getContentType().startsWith("image")) {
                log.error("지원하지 않는 파일 타입: " + file.getContentType());
                throw new IllegalArgumentException("이미지 파일만 업로드할 수 있습니다.");
            }

            String uuid = UUID.randomUUID().toString();
            String saveFilename = uuid + "_" + file.getOriginalFilename();
            String savePath = uploadPath + File.separator + saveFilename;

            try {
                // 원본 파일 저장
                file.transferTo(new File(savePath));

                // 썸네일 파일 생성
//                Thumbnails.of(new File(savePath)).size(150, 150).toFile(uploadPath + File.separator + "s_" + saveFilename);

                filenames.add(saveFilename);

                // BoardImage 엔티티 생성 및 추가
                BoardImage boardImage = BoardImage.builder()
                        .filename(saveFilename)
                        .board(board)
                        .build();
                board.addImage(boardImage); // Board 엔티티에 이미지 추가

            } catch (IOException e) {
                log.error("파일 업로드 중 에러 발생: {}", e.getMessage());
                throw new RuntimeException(e);
            }
        }

        boardRepository.save(board);
        return filenames;
    }

    // 개별 파일 삭제
    public void deleteFile(Long boardId, String filename) {
        Board board = boardRepository.findById(boardId).orElseThrow(BoardException.NOT_FOUND::get);

        // 이미지 삭제 전 해당 게시물에 해당 이미지가 있는지 확인
        BoardImage boardImage = board.getImages().stream()
                .filter(image -> image.getFilename().equals(filename))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 이미지가 게시물에 존재하지 않습니다."));

        File file = new File(uploadPath + File.separator + filename);
//        File thumbFile = new File(uploadPath + File.separator + "s_" + filename);

        try {
            if (file.exists()) file.delete();
//            if (thumbFile.exists()) thumbFile.delete();

            // 게시물의 이미지 목록에서 해당 이미지 제거
            board.removeImage(boardImage);
            boardRepository.save(board);

        } catch (Exception e) {
            log.error("파일 삭제 중 에러 발생: {}", e.getMessage());
        }
    }

}