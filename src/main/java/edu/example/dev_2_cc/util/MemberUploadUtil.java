package edu.example.dev_2_cc.util;

import edu.example.dev_2_cc.entity.Member;
import edu.example.dev_2_cc.exception.MemberException;
import edu.example.dev_2_cc.repository.MemberRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Component
@Log4j2
@RequiredArgsConstructor
public class MemberUploadUtil {

    @Value("${edu.example.upload.path}") // 업로드 경로 설정
    private String uploadPath;

    private final MemberRepository memberRepository;

    @PostConstruct // 객체 생성 후 자동 실행
    public void init() {
        File uploadDir = new File(uploadPath);

        if (!uploadDir.exists()) {
            log.info("--- uploadDir 생성 : " + uploadDir);
            uploadDir.mkdir();
        }

        uploadPath = uploadDir.getAbsolutePath();
        log.info("--- uploadPath : " + uploadPath);
    }

    // 이미지 업로드 메서드
    public String upload(MultipartFile file, String memberId) {
        // 파일이 비어있는 경우 기본 이미지 설정
        if (file == null || file.isEmpty()) {
            return "default_avatar.png";
        }

        // 파일이 이미지가 아닐 경우 처리
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
            Thumbnails.of(new File(savePath)).size(150, 150).toFile(uploadPath + File.separator + "s_" + saveFilename);

            // 이미지 적용 위해서 회원 정보 조회 및 업데이트
            Member member = memberRepository.findById(memberId).orElseThrow(MemberException.NOT_FOUND::get);
            member.addImage(saveFilename);
            memberRepository.save(member);

            return saveFilename;
        } catch (IOException e) {
            log.error("파일 업로드 중 에러 발생: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    // 업로드 이미지 파일 삭제
    public void deleteFile(String memberId) {
        // 회원 정보 가져오기
        Member member = memberRepository.findById(memberId).orElseThrow(MemberException.NOT_FOUND::get);

        String filename = member.getImage().getFilename();

        // 기본 이미지 삭제 방지
        if ("default_avatar.png".equals(filename)) {
            log.error("default_avatar.png는 삭제할 수 없습니다.");
            throw new IllegalStateException("default_avatar.png는 삭제할 수 없습니다.");
        }

        File file = new File(uploadPath + File.separator + filename);
        File thumbFile = new File(uploadPath + File.separator + "s_" + filename);

        try {
            if (file.exists()) file.delete();
            if (thumbFile.exists()) thumbFile.delete();

            // 프로필 이미지 삭제 후 기본 이미지로 설정
            member.addImage("default_avatar.png");
            memberRepository.save(member);
        } catch (Exception e) {
            log.error("파일 삭제 중 에러 발생: {}", e.getMessage());
        }
    }

}