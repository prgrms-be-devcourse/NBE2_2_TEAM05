package edu.example.dev_2_cc.jwt;

import edu.example.dev_2_cc.dto.userDetails.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collection;
import java.util.Iterator;

@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    //입력받은 로그인 정보를 토큰으로 만들어 검증단계로 전달하기 위한 메서드
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        //클라이언트 요청에서 name, password 추출
        String memberId = request.getParameter("memberId");
        String password = obtainPassword(request);

        //스프링 시큐리티에서 username과 password를 검증하기 위해서는 token에 담아야 함
        //권한 정보(마지막 파라미터)는 토큰 검증 이후에 별도로 설정을 위해 null값 전달
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(memberId, password, null);

        //AuthenticationManager로 token 전달
        return authenticationManager.authenticate(authToken);
    }

    //로그인 성공시 실행하는 메서드 (여기로 JWT 발급 메서드 가져와서 최종적으로 JWT 생성)
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {
        //UserDetails
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        //memberId 받아오기
        String memberId = customUserDetails.getMemberId();

        //role 받아오기

        //Spring Security는 다중 권한 기능을 지원하기 때문에, 권한은 단일 값이 아니라 Collection 형태로 관리
        //예를 들어, 한 사용자가 ROLE_USER와 ROLE_ADMIN 두 가지 권한을 동시에 가질 수 있다
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        //authorities 리스트에서 권한들을 차례대로 가져온다
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        //Iterator가 현재 가리키고 있는 첫 번째 권한을 반환
        GrantedAuthority auth = iterator.next();

        String role = auth.getAuthority();

        //받아온 내용으로 token 생성
        String token = jwtUtil.createJwt(memberId, role, 60 * 60 * 10 * 1000L);

        //응답의 헤더부분에 표시
        response.addHeader("Authorization", "Bearer " + token);
    }

    //로그인 실패시 실행하는 메서드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        response.setStatus(401);
    }

}
