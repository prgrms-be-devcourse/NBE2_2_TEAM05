package edu.example.dev_2_cc.dto.userDetails;

import edu.example.dev_2_cc.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    // Member 엔터티 객체의 정보를 Spring Security가 요구하는 형식으로 변환
    // 계정 만료여부, 계정 잠김여부, 비밀번호 만료여부, 계정 활성화여부는 기능구현 하지 않기 때문에 true로 반환

    private final Member member;

    //사용자의 권한 반환
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {

                return member.getRole();
            }
        });

        return collection;
    }

    //비밀번호 반환
    @Override
    public String getPassword() {
        return member.getPassword();
    }

    // 사용자명 반환 (로그인에 사용할 고유 식별자를 반환해야 함)
    @Override
    public String getUsername() {
        return member.getName();
    }

    //계정 만료 여부 반환
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //계정 잠김 여부 반환
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //비밀번호 만료 여부 반환
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //계정 활성화 여부 반환
    @Override
    public boolean isEnabled() {
        return true;
    }

    //회원Id 반환
    public String getMemberId() {
        return member.getMemberId();
    }
}
