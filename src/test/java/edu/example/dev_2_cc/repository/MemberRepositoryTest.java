package edu.example.dev_2_cc.repository;

import edu.example.dev_2_cc.entity.Member;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Log4j2
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void testSaveMember() {
        // Given 멤버 생성
        Member member = Member.builder()
                .memberId("member2")
                .email("test@example.com")
                .name("John Doe")
                .password("securepassword")
                .sex("M")
                .address("1234 Test Street")
                .role("USER")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        member.addImage("test.png");

        // When 멤버를 저장
        Member savedMember = memberRepository.save(member);

        // Then 저장된 멤버가 존재하는지 검증
        assertThat(savedMember).isNotNull();
        assertThat(savedMember.getMemberId()).isEqualTo("member2");
        assertThat(savedMember.getEmail()).isEqualTo("test@example.com");

        // 이미지 이름 검증
        assertThat(savedMember.getImage().getFilename()).isEqualTo("test.png");

        log.info("---savedMember : " + savedMember);
    }

    @Test
    void testFindList() {
        // When 회원 정보 전체 조회
        List<Member> memberList = memberRepository.findAll();

        // Then 리스트가 비었는지 검증
        assertThat(memberList).isNotEmpty();
        memberList.forEach(member -> log.info("---member : " + member));
    }

    // 회원 단일 조회 테스트
    @Test
    public void testFindById_Success() {
        // Given
        String memberId = "member1";

        // When 저장된 회원을 ID로 조회
        Optional<Member> foundMember = memberRepository.findById(memberId);

        // Then 조회된 회원이 존재하는지 검증
        assertThat(foundMember.isPresent()).isTrue();
        assertThat(foundMember.get().getName()).isEqualTo("John Doe");
        assertThat(foundMember.get().getEmail()).isEqualTo("test@example.com");
        assertThat(foundMember.get().getPassword()).isEqualTo("securepassword");
        assertThat(foundMember.get().getSex()).isEqualTo("M");
        assertThat(foundMember.get().getAddress()).isEqualTo("1234 Test Street");
        assertThat(foundMember.get().getRole()).isEqualTo("USER");
    }

    // 회원 수정 테스트
    @Test
    void testUpdateMember() {
        // Given
        String memberId = "member1";

        // When
        Optional<Member> optionalMember = memberRepository.findById("member1");
        assertThat(optionalMember).isPresent();
        Member member = optionalMember.get();

        // 필드 값 수정
        member.changeEmail("updated@example.com");
        member.changePhoneNumber("010-2222-2222");
        member.changeName("Jane Doe");
        member.changeAddress("5678 Updated Street");
        member.changeRole("ADMIN");

        member.clearImage();
        member.addImage("test2.png");

        // 수정된 멤버 저장
        memberRepository.save(member);

        // Then 수정된 내용 검증
        Optional<Member> updatedMemberOptional = memberRepository.findById("member1");
        assertThat(updatedMemberOptional).isPresent();
        Member updatedMember = updatedMemberOptional.get();
        assertThat(updatedMember.getEmail()).isEqualTo("updated@example.com");
        assertThat(updatedMember.getName()).isEqualTo("Jane Doe");
        assertThat(updatedMember.getAddress()).isEqualTo("5678 Updated Street");
        assertThat(updatedMember.getRole()).isEqualTo("ADMIN");
    }

    @Test
    void testDeleteMember() {
        // Given
        String memberId = "member1";

        // When
        memberRepository.deleteById(memberId);

        // Then 삭제가 잘 되었는지(존재하는지) 검증
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        assertThat(optionalMember).isNotPresent();
    }

}
