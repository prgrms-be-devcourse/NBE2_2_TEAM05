package edu.example.dev_2_cc.repository.search;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import edu.example.dev_2_cc.dto.review.ReviewListDTO;
import edu.example.dev_2_cc.dto.review.ReviewRequestDTO;
import edu.example.dev_2_cc.dto.product.ProductListDTO;
import edu.example.dev_2_cc.entity.QMember;
import edu.example.dev_2_cc.entity.QProduct;
import edu.example.dev_2_cc.entity.QReview;
import edu.example.dev_2_cc.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class ReviewSearchImpl extends QuerydslRepositorySupport implements ReviewSearch {
    public ReviewSearchImpl() {super(Review.class);}

    @Override
    public Page<ReviewListDTO> list(Pageable pageable) {
        QReview review = QReview.review;
        JPQLQuery<Review> query = from(review);

        JPQLQuery<ReviewListDTO> dtoQuery=query.select(Projections.fields(
                ReviewListDTO.class,
                review.reviewId,
                review.content,
                review.star,
                review.member.memberId,
                review.product.productId
        ));

        getQuerydsl().applyPagination(pageable, dtoQuery);      //페이징
        List<ReviewListDTO> reviewList = dtoQuery.fetch();    //쿼리 실행
        long count = dtoQuery.fetchCount();         //레코드 수 조회

        return new PageImpl<>(reviewList, pageable, count);
    }

}
