package edu.example.dev_2_cc.api_controller;

import edu.example.dev_2_cc.exception.UploadNotSupportedException;
import edu.example.dev_2_cc.util.MemberUploadUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/cc/memberImage")
public class MemberFileController {
    private final MemberUploadUtil memberUploadUtil;

    @PostMapping("/upload/{memberId}")
    public ResponseEntity<String> uploadFile(
            @RequestParam("file") MultipartFile file, @PathVariable String memberId) {
        log.info("--- uploadFile() : " + file);

        // 확장자 체크
        checkFileExt(file.getOriginalFilename());

        // 파일 업로드
        String uploadedFilename = memberUploadUtil.upload(file, memberId);
        return ResponseEntity.ok(uploadedFilename);
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<Map<String, String>> deleteFile(@PathVariable String memberId) {
        memberUploadUtil.deleteFile(memberId);
        return ResponseEntity.ok(Map.of("message", "Member Image deleted"));
    }

    // 업로드 파일 확장자 체크 메서드
    public void checkFileExt(String filename) throws UploadNotSupportedException {
        String ext = filename.substring(filename.lastIndexOf(".") + 1);
        String regExp = "^(jpg|jpeg|JPG|JPEG|png|PNG|gif|GIF|bmp|BMP)$";

        if (!ext.matches(regExp)) {
            throw new UploadNotSupportedException("지원하지 않는 형식입니다: " + ext);
        }
    }

}

// String regExp = "^(jpg|jpeg|JPG|JPEG|png|PNG|gif|GIF|bmp|BMP)$";
// -> 위 코드가 좀 더 정확하게 확장자 식별을 할 수 있다고 합니다.