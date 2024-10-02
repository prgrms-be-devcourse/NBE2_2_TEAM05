package edu.example.dev_2_cc.repository.search;

import edu.example.dev_2_cc.dto.board.BoardListDTO;
import edu.example.dev_2_cc.dto.product.ProductListDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardSearch {
    Page<BoardListDTO> list(Pageable pageable);
}
