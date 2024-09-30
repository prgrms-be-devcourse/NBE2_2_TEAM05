package edu.example.dev_2_cc.repository.search;

import edu.example.dev_2_cc.dto.review.ReviewListDTO;
import edu.example.dev_2_cc.dto.review.ReviewRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewSearch {

    Page<ReviewListDTO> list(Pageable pageable);
}
