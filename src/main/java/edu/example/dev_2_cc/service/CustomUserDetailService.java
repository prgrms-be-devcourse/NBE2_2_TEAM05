package edu.example.dev_2_cc.service;

import edu.example.dev_2_cc.dto.userDetails.CustomUserDetails;
import edu.example.dev_2_cc.entity.Member;
import edu.example.dev_2_cc.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final MemberRepository memberRepository;

    //loadUserByUsername은 기존에 존재하는 메서드이기 때문에 기능과 달라도 이름은 변경하지 않고 override로 구현
    //실질적 기능은 Username이 아닌 memberId로 DB에서 조회
    //DB에 존재하면 DTO로 return 한다.
    @Override
    public UserDetails loadUserByUsername(String memberId) throws UsernameNotFoundException {

        //DB에서 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with memberId: " + memberId));

        if (member != null) {

            //UserDetails에 담아서 return하면 AutneticationManager가 검증 함
            return new CustomUserDetails(member);
        }
        return null;
    }
}
