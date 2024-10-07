package edu.example.dev_2_cc.config;

import edu.example.dev_2_cc.jwt.JWTFilter;
import edu.example.dev_2_cc.jwt.JWTUtil;
import edu.example.dev_2_cc.jwt.LoginFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    //AuthenticationManager가 인자로 받을 AuthenticationConfiguraion 객체 생성자 주입
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;

    //AuthenticationManager Bean 등록
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        //아래에 disabled된 설정들은 jwt에서 개별적으로 설정해줘야 되는 부분이기 때문에 작동해제 시킨다.

        //csrf disable
        http
                .csrf((auth) -> auth.disable());

        //From 로그인 방식 disable
        http
                .formLogin((auth) -> auth.disable());

        //http basic 인증 방식 disable
        http
                .httpBasic((auth) -> auth.disable());

        //경로별 인가 작업 -> 로그인 유무만 따짐 -> 회원 별 식별은 따로 구현 해야함
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/login", "/",
                                         "/cc/member",
                                         "/cc/product/**",
                                         "/cc/review/**",
                                         "/cc/board",
                                         "cc/reply").permitAll()
                        .requestMatchers("/cc/mypage/**").hasRole("USER")
                        .requestMatchers("/cc/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated());

        //JWTFilter는 로그인 된 사용자에 대한 토큰 검증 필터이다
        //LoginFilter 앞에서 동작하게 배치하여 JWTFilter가 정상적으로 작동할 경우 로그인이 되어있는 것으로 처리
        //LoginFilter 단계는 건너뛰게 된다.
        http
                .addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class);

        //LoginFilter() 필터가 작동할 수 있도록 등록
        //LoginFilter()는 인자를 받음(AuthenticationManager() 메소드에 authenticationConfiguration 객체를 넣어야 함) 따라서 등록 필요
        http
                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration),jwtUtil), UsernamePasswordAuthenticationFilter.class);

        //세션 설정
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }


}
