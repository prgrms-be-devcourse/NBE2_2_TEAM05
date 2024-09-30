package edu.example.dev_2_cc.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "상품 목록 조회 시 페이징 정보 지정")
public class PageRequestDTO {

    @Schema(description = "표시할 페이지 번호")
    @Builder.Default
    @Min(1)
    private int page = 1;

    @Schema(description = "한 페이지에 표시할 상품의 수")
    @Builder.Default
    @Min(10)
    @Max(100)
    private int size = 10;

    public Pageable getPageable(Sort sort){
        int pageNum = page < 0 ? 1 : page - 1;
        int sizeNum = size <= 10 ? 10 : size ;

        return PageRequest.of(pageNum, sizeNum, sort);
    }
}
