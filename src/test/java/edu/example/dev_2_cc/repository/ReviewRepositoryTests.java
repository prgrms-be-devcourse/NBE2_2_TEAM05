package edu.example.dev_2_cc.repository;

import edu.example.dev_2_cc.entity.Member;
import edu.example.dev_2_cc.entity.Product;
import edu.example.dev_2_cc.entity.Review;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Log4j2
public class ReviewRepositoryTests {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void testCreate(){

        Product product = productRepository.findById(1L).orElseThrow();

        Member member = memberRepository.findById("member1").orElseThrow();

        Review review = Review.builder().product(product).member(member).content("이집 잘합니다").star(5).build();

        Review savedReview = reviewRepository.save(review);
        assertNotNull(savedReview);
    }

    @Test
    @Commit
    @Transactional
    public void testUpdate(){
        Long reviewId = 1L;
        String content = "리뷰 수정합니다";
        int star = 1;

        Optional<Review> foundReview = reviewRepository.findById(reviewId);
        assertTrue(foundReview.isPresent());

        Review review = foundReview.get();
        review.changeContent(content);
        review.changeStar(star);

        foundReview = reviewRepository.findById(reviewId);
        assertEquals(content, foundReview.get().getContent());
        assertEquals(star, foundReview.get().getStar());
    }
}
