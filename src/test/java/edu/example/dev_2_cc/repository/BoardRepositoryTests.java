package edu.example.dev_2_cc.repository;

import edu.example.dev_2_cc.entity.Board;
import edu.example.dev_2_cc.entity.Category;
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
public class BoardRepositoryTests {

    @Autowired
    private BoardRepository boardRepository;

    @Test
    public void testCreate(){
        Board board = Board.builder().title("테스트제목1").description("테스트설명1").category(Category.TIP).build();
        Board savedBoard =  boardRepository.save(board);
        assertNotNull(savedBoard);
    }

    @Test
    @Commit
    @Transactional
    public void testUpdate(){
        Long BoardId = 2L;
        String title = "수정된제목";
        String description = "수정된 설명";
        Category category = Category.GENERAL;

        Optional<Board> foundBoard = boardRepository.findById(BoardId);
        assertTrue(foundBoard.isPresent());

        Board board = foundBoard.get();

        board.changeTitle(title);
        board.changeDescription(description);
        board.changeCategory(category);

        foundBoard = boardRepository.findById(BoardId);
        assertEquals(title, foundBoard.get().getTitle());
        assertEquals(description, foundBoard.get().getDescription());
        assertEquals(category, foundBoard.get().getCategory());
    }


}
