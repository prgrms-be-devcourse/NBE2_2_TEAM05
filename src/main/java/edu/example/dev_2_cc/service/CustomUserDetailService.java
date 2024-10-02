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
public class CustomUserDetailService implements UserDetailsService {
    private final MemberRepository memberRepository;

    public CustomUserDetailService(MemberRepository memberRepository) {
        super();
        this.memberRepository = memberRepository;
    }

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
