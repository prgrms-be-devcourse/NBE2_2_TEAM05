package edu.example.dev_2_cc.api_controller;

import edu.example.dev_2_cc.util.BoardUploadUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/cc/boardImage")
public class BoardFileController {
    
    private final BoardUploadUtil boardUploadUtil;

    // 다중 이미지 업로드
    @PostMapping("/upload/{boardId}")
    public ResponseEntity<List<String>> uploadFiles(
            @RequestParam("files") MultipartFile[] files, @PathVariable Long boardId) {
        log.info("--- uploadFiles() : " + files.length + " files");

        // 파일 업로드
        List<String> uploadedFilenames = boardUploadUtil.upload(files, boardId);
        return ResponseEntity.ok(uploadedFilenames);
    }

    // 개별 이미지 파일 삭제
    @DeleteMapping("/{boardId}/{filename}")
    public ResponseEntity<Map<String, String>> deleteFile(
            @PathVariable Long boardId, @PathVariable String filename) {
        boardUploadUtil.deleteFile(boardId, filename);
        return ResponseEntity.ok(Map.of("message", "Board Image deleted"));
    }

}