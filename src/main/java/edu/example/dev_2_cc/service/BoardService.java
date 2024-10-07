package edu.example.dev_2_cc.service;

import edu.example.dev_2_cc.dto.board.BoardListDTO;
import edu.example.dev_2_cc.dto.board.BoardRequestDTO;
import edu.example.dev_2_cc.dto.board.BoardResponseDTO;
import edu.example.dev_2_cc.dto.board.BoardUpdateDTO;
import edu.example.dev_2_cc.dto.review.PageRequestDTO;
import edu.example.dev_2_cc.entity.Board;
import edu.example.dev_2_cc.entity.Category;
import edu.example.dev_2_cc.entity.Member;
import edu.example.dev_2_cc.exception.AuthorizationException;
import edu.example.dev_2_cc.exception.BoardException;
import edu.example.dev_2_cc.exception.MemberException;
import edu.example.dev_2_cc.repository.BoardRepository;
import edu.example.dev_2_cc.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class BoardService {

    private final BoardRepository boardRepository;
    private final SecurityUtil securityUtil;

    // 게시물 작성
    @Transactional
    public BoardResponseDTO createBoard(BoardRequestDTO boardRequestDTO) {
        // 카테고리 권한 체크
        checkCategoryAuthorization(boardRequestDTO.getCategory());

        Member currentUser = securityUtil.getCurrentUser();
//        log.info("아니 큐렌트 유저가 왜?" + currentUser);

        // 게시물 생성
        Board board = Board.builder()
                .member(currentUser)
                .title(boardRequestDTO.getTitle())
                .description(boardRequestDTO.getDescription())
                .category(boardRequestDTO.getCategory())
                .build();

        boardRepository.save(board);

        return new BoardResponseDTO(board);
    }

    // 게시물 조회
    @Transactional(readOnly = true)
    public BoardResponseDTO read(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(BoardException.NOT_FOUND::get);
        return new BoardResponseDTO(board);
    }

    // 게시물 수정
    @Transactional
    public BoardResponseDTO updateBoard(BoardUpdateDTO boardUpdateDTO) {
        Board board = boardRepository.findById(boardUpdateDTO.getBoardId())
                .orElseThrow(BoardException.NOT_FOUND::get);

        // 권한 체크: 작성자 또는 관리자만 가능
        securityUtil.checkUserAuthorization(board.getMember());

        // 카테고리 변경 검증: USER는 GENERAL -> TIP, NOTICE로 변경할 수 없음
        String currentRole = securityUtil.getCurrentUserRole();
        if ("ROLE_USER".equals(currentRole) && board.getCategory() == Category.GENERAL &&
                (boardUpdateDTO.getCategory() == Category.TIP || boardUpdateDTO.getCategory() == Category.NOTICE)) {
            throw new AuthorizationException("USER는 GENERAL 게시물을 TIP 또는 NOTICE로 변경할 수 없습니다.");
        }

        board.changeTitle(boardUpdateDTO.getTitle());
        board.changeDescription(boardUpdateDTO.getDescription());
        board.changeCategory(boardUpdateDTO.getCategory());

        boardRepository.save(board);

        return new BoardResponseDTO(board);
    }

    // 게시물 삭제
    @Transactional
    public void delete(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(BoardException.NOT_FOUND::get);

        // 권한 체크: 작성자 또는 관리자만 가능
        securityUtil.checkUserAuthorization(board.getMember());

        boardRepository.delete(board);
    }

    @Transactional(readOnly = true)
    public Page<BoardListDTO> getList(PageRequestDTO pageRequestDTO) {
        try {
            Sort sort = Sort.by("createdAt").descending();
            Pageable pageable = pageRequestDTO.getPageable(sort);
            Page<BoardListDTO> boardList = boardRepository.list(pageable);
            return boardList;
        }catch (Exception e){
            log.error(e.getMessage());
            throw BoardException.NOT_FOUND.get();   //임시
        }
    }

    // MemberID로 Board 리스트 조회
    @Transactional(readOnly = true)
    public List<BoardListDTO> listByMemberId(String memberId) {

        // 현재 로그인한 사용자의 ID를 가져옴
        String currentUserId = securityUtil.getCurrentUser().getMemberId();

        // 현재 사용자가 요청한 memberId와 동일한지 확인
        if (!memberId.equals(currentUserId)) {
            throw new AuthorizationException("작성자만 자신의 게시물을 조회할 수 있습니다.");
        }

        List<Board> boards = boardRepository.findAllByMember(memberId);

        if (boards.isEmpty()) {
            throw MemberException.NOT_FOUND.get();
        }

        return boards.stream()
                .map(BoardListDTO::new)
                .collect(Collectors.toList());
    }

    // 카테고리 권한 체크 메서드
    private void checkCategoryAuthorization(Category category) {
        String currentRole = securityUtil.getCurrentUserRole();

        boolean isAuthorized = switch (category) {
            case GENERAL -> "ROLE_USER".equals(currentRole) || "ROLE_ADMIN".equals(currentRole);
            case NOTICE, TIP -> "ROLE_ADMIN".equals(currentRole);
            default -> false;
        };

        if (!isAuthorized) {
            throw new AuthorizationException("카테고리에 대한 권한이 없습니다.");
        }
    }

}
