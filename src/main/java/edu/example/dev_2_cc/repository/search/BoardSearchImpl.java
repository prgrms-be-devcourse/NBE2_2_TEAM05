package edu.example.dev_2_cc.repository.search;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import edu.example.dev_2_cc.dto.board.BoardListDTO;
import edu.example.dev_2_cc.dto.product.ProductListDTO;
import edu.example.dev_2_cc.entity.Board;
import edu.example.dev_2_cc.entity.QBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class BoardSearchImpl extends QuerydslRepositorySupport implements BoardSearch {
    public BoardSearchImpl() {
        super(Board.class);
    }


    @Override
    public Page<BoardListDTO> list(Pageable pageable) {
        QBoard board = QBoard.board;

        JPQLQuery<Board> query = from(board);

        JPQLQuery<BoardListDTO> dtoQuery = query.select(Projections.fields(
                BoardListDTO.class,
                board.boardId,
                board.title,
                board.category,
                board.member.memberId
        ));

        getQuerydsl().applyPagination(pageable, dtoQuery);
        List<BoardListDTO> boardList = dtoQuery.fetch();
        long count = dtoQuery.fetchCount();

        return new PageImpl<BoardListDTO>(boardList, pageable, count);
    }
}
