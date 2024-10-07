package edu.example.dev_2_cc.util;

import edu.example.dev_2_cc.dto.userDetails.CustomUserDetails;
import edu.example.dev_2_cc.entity.Member;
import edu.example.dev_2_cc.exception.AuthorizationException;
import edu.example.dev_2_cc.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Log4j2
public class SecurityUtil {

    private final MemberRepository memberRepository;

    // Authentication 객체를 가져오는 메서드 -> 중복되기 때문에 메서드로 합침
    private Authentication getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AuthorizationException("인증 정보가 없습니다.");
        }
        return authentication;
    }

    // 현재 인증된 사용자 정보 가져오기
    public Member getCurrentUser() {
        Authentication authentication = getAuthentication();
//        log.info("아니 어써티케이션이 왜?" + authentication);

        Object principal = authentication.getPrincipal();
//        log.info("아니 프린시펄가 왜?" + authentication.getPrincipal());

        CustomUserDetails customUserDetails = (CustomUserDetails) principal;

        String memberId;
        if (principal instanceof UserDetails) {
            memberId = customUserDetails.getMemberId(); // 인증된 사용자의 이름(아이디)을 가져옴
        } else if (principal instanceof String) {
            memberId = (String) principal;
        } else {
            throw new AuthorizationException("인증된 사용자 정보를 가져올 수 없습니다.");
        }
//        log.info("아니 멤버아이디가 왜?" + memberId);

        // username을 사용하여 Member 엔티티를 가져옴
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new AuthorizationException("사용자를 찾을 수 없습니다."));
    }

    // 현재 사용자의 역할(Role) 가져오기
    public String getCurrentUserRole() {
        Authentication authentication = getAuthentication();

        return authentication.getAuthorities().stream()
                .findFirst()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .orElseThrow(() -> new AuthorizationException("권한 정보를 가져올 수 없습니다."));
    }

    // 현재 사용자가 작성자거나 관리자 인지 확인
    public void checkUserAuthorization(Member member) {
        Member currentUser = getCurrentUser();
        boolean isAuthorized = currentUser.getMemberId().equals(member.getMemberId()) ||
                "ROLE_ADMIN".equals(getCurrentUserRole());

        if (!isAuthorized) {
            throw new AuthorizationException("작성자나 관리자만 해당 작업을 수행할 수 있습니다.");
        }
    }

}

// authentication.getName()의 반환값은 LoginFilter에서 설정