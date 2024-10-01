package edu.example.dev_2_cc.repository;

import edu.example.dev_2_cc.entity.Board;
import edu.example.dev_2_cc.repository.search.BoardSearch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board,Long>, BoardSearch {

}
