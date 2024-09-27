package edu.example.dev_2_cc.util;

import edu.example.dev_2_cc.entity.Product;
import edu.example.dev_2_cc.entity.ProductImage;
import edu.example.dev_2_cc.exception.ProductException;

import edu.example.dev_2_cc.repository.ProductRepository;
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
public class ProductUploadUtil {

    @Value("${edu.example.upload.path}") //application.properties 파일에서 업로드 설정 경로 읽어오기
    private String uploadPath;

    private final ProductRepository productRepository;

    @PostConstruct  //객체 생성 후 자동 실행 메서드 지정
    public void init() {    //업로드 경로 실제 존재 여부 확인 및 처리
        File tempDir = new File(uploadPath);

        if(!tempDir.exists()){  //업로드 경로가 존재하지 않으면
            log.info("--- tempDir : " + tempDir);
            tempDir.mkdir();    // 업로드 디렉토리 생성
        }

        uploadPath = tempDir.getAbsolutePath();
        log.info("--- getPath() : " + tempDir.getPath());
        log.info("--- uploadPath : " + uploadPath);
        log.info("--------------------------------");

    }

    //0. 업로드 수행 - FileController에서 업로드 파일 확장자 체크가 끝난 뒤
    public List<String> upload(MultipartFile[] files, Long productId) { // 1. 컨트롤러에 전달된 업로드 파일들을 매개변수로 받아서
        List<String> filenames = new ArrayList<>();

        for(MultipartFile file : files){
            log.info("------------------------------");
            log.info("name : " + file.getName());
            log.info("origin name : " + file.getOriginalFilename());
            log.info("type : " + file.getContentType());

            if(!file.getContentType().startsWith("image")) { // 2. 전달받은 파일이 image 타입이 아닌 경우
                log.error("--- 지원하지 않는 파일 타입 : " + file.getContentType());// "--- 지원하지 않는 파일타입 : OO" 로그 출력하고
                continue; // 다음 파일 확인
            }

            String uuid = UUID.randomUUID().toString();
            String saveFilename = uuid + "_" + file.getOriginalFilename(); // 3. 이미지 파일 명이 중복되지 않게 파일명에 uuis_를 결합하여 저장
            String savePath = uploadPath + File.separator;   // 4. 업로드 경로에 파일 구분자File.separator를 결합하여 저장

            try {
                file.transferTo(new File(savePath + saveFilename)); // --------- 실제 파일 업로드 처리 -------------

                //썸네일 파일 생성
                Thumbnails.of(new File(savePath+saveFilename)).size(150,150).toFile(savePath+ "s_" + saveFilename);

                filenames.add(saveFilename);    //5. 업로드된 파일명을 List 객체에 저장

                Product product = productRepository.findById(productId).orElseThrow(ProductException.NOT_FOUND::get);
                product.addImage(saveFilename);
                productRepository.save(product);


            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        // 5.1 저장한 객체 반환
        return filenames;
        // 6. FileController에서는 전달받은 값을 상태코드 200으로 반환
    }

    //업로드 파일 삭제
    public void deleteFile(Long productId, Long ino) {
        // 1. Product 엔티티에서 해당 이미지 가져오기
        Product product = productRepository.findById(productId)
                .orElseThrow(ProductException.NOT_FOUND::get);

        // 2. 이미지 컬렉션에서 삭제할 이미지 찾기
        ProductImage productImage = product.getImages().stream()
                .filter(image -> image.getIno() == ino)
                .findFirst()
                .orElseThrow(ProductException.NOT_FOUND::get); //나중에 IMAGE_NOT_FOUND 만들기

        String filename = productImage.getFilename();

        File file = new File(uploadPath + File.separator + filename); // 원본 파일
        File thumbFile = new File(uploadPath + File.separator + "s_" + filename); // 썸네일 파일

        try {
            // 파일 삭제
            if (file.exists()) file.delete();
            if (thumbFile.exists()) thumbFile.delete(); // 썸네일 파일도 삭제

            // 3. 이미지 목록에서 해당 이미지 제거
            product.getImages().remove(productImage);
            productRepository.save(product); // 엔티티 저장
        } catch (Exception e) {
            log.error("파일 삭제 중 에러 발생: {}", e.getMessage());
        }
    }



}
