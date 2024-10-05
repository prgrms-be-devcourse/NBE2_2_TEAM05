package edu.example.dev_2_cc.jwt;

import edu.example.dev_2_cc.dto.userDetails.CustomUserDetails;
import edu.example.dev_2_cc.entity.Member;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@RequiredArgsConstructor
@Log4j2
public class JWTFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //request에서 Authorization 헤더를 찾음
        String authorization= request.getHeader("Authorization");
        String refreshToken = request.getHeader("RefreshToken");

        // Authorization Header가 null이 아닐 경우에만 로그 출력
        if (authorization != null) {
            log.info("Access Token: " + authorization);
        }
        // Refresh Token이 null이 아닐 경우에만 로그 출력
        if (refreshToken != null) {
            log.info("Refresh Token: " + refreshToken);
        }

        //Authorization 헤더 검증
        if (authorization == null || !authorization.startsWith("Bearer ")) {

            log.info("token null");
            filterChain.doFilter(request, response);

            //조건이 해당되면 메소드 종료 (필수)
            return;
        }

        log.info("--authorization now--");
        //Bearer 부분 제거 후 순수 토큰만 획득
        String token = authorization.split(" ")[1].trim();

        //토큰 소멸 시간 검증
        if (jwtUtil.isExpired(token)) {
            log.info("--token expired--");

            //refreshToken 유효기간 만료되지 않을 경우 accessToken, refreshToken 재생성
            if (refreshToken != null && !jwtUtil.isExpired(refreshToken)) {
                log.info("--refreshToken valid, regenerating accessToken--");

                String memberId = jwtUtil.getMemberId(refreshToken);
                String role = jwtUtil.getRole(refreshToken);
                String newAccessToken = jwtUtil.regenerateAccessToken(refreshToken, memberId, role, 1000 * (60 * 60 * 10L));
                String newRefreshToken = jwtUtil.regenerateRefreshToken(refreshToken, memberId, role, 1000 * (60 * 60 * 24 * 7L));

                response.addHeader("newAuthorization", "Bearer " + newAccessToken);
                response.addHeader("newRefreshToken", newRefreshToken);

                // 새로운 Token을 클라이언트가 사용할 수 있도록 현재 요청에 추가
                request.setAttribute("newAccessToken", newAccessToken);
                request.setAttribute("newRefreshToken", newRefreshToken);
                // 만료된 accessToken과 유효한 refreshToken을 요청 헤더에 넣을 경우 403에러 발생하고 토큰을 재생성하고
                // 로그에 --refreshToken valid, regenerating accessToken-- 가 찍힌다.
                // 응답헤더에 newAccessToken과 newRefreshToken값을 복사하여 재요청
                // 이후로는 새로 생성된 토큰을 다른 동작에서도 사용하면 된다.

            } else {
                log.info("--no valid refreshToken, unauthorized--");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }
        else {
            //access 토큰에서 memberId와 role 획득
            String memberId = jwtUtil.getMemberId(token);
            String role = jwtUtil.getRole(token);

            //Member를 생성하여 값 set
            Member member = new Member();
            member.setMemberId(memberId);
            member.setPassword("temppassword"); //임의로 지정해도 상관없다
            member.setRole(role);

            //UserDetails에 회원 정보 객체 담기
            CustomUserDetails customUserDetails = new CustomUserDetails(member);

            //사용자의 인증 정보 전달
            Authentication userAuthentication = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
            //세션에 사용자 등록
            SecurityContextHolder.getContext().setAuthentication(userAuthentication);

        }
        //필터체인의 다음 필터로 요청 넘긴다
        filterChain.doFilter(request, response);
    }
}
