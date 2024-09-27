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
        // Given: 멤버 생성
        Member member = Member.builder()
                .memberId("member1")
                .email("test@example.com")
                .name("John Doe")
                .password("securepassword")
                .sex("M")
                .address("1234 Test Street")
                .profilePic("profilePicUrl")
                .role("USER")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // When: 멤버를 저장
        Member savedMember = memberRepository.save(member);

        // Then: 저장된 멤버가 존재하는지 검증
        assertThat(savedMember).isNotNull();
        assertThat(savedMember.getMemberId()).isEqualTo("member1");
        assertThat(savedMember.getEmail()).isEqualTo("test@example.com");

        log.info("---savedMember : " + savedMember);
    }

    @Test
    void testFindList() {
        // GIVEN 회원 등록
        Member member = Member.builder()
                .memberId("member1")
                .email("test@example.com")
                .name("John Doe")
                .password("securepassword")
                .sex("M")
                .address("1234 Test Street")
                .profilePic("profilePicUrl")
                .role("USER")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // WHEN 회원 저장, 회원 전체 리스트로 가져오기
        memberRepository.save(member);
        List<Member> memberList = memberRepository.findAll();

        // THEN
        assertThat(memberList).isNotEmpty(); // 회원 전체 리스트가 비었는지 확인
        assertThat(memberList.size()).isEqualTo(1); // 회원 전체 리스트의 회원이 1개인지 확인
        log.info("---memberList : " + memberList);
    }

    // 회원 단일 조회 테스트
    @Test
    public void testFindById_Success() {
        // Given - 회원을 저장
        Member savedMember = Member.builder()
                .memberId("member1")
                .email("test@example.com")
                .name("John Doe")
                .password("securepassword")
                .sex("M")
                .address("1234 Test Street")
                .profilePic("profilePicUrl")
                .role("USER")
                .build();
        memberRepository.save(savedMember);

        // When - 저장된 회원을 ID로 조회
        Optional<Member> foundMember = memberRepository.findById(savedMember.getMemberId());

        // Then - 조회된 회원이 존재하는지 검증
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
        // Given: 기존 멤버 생성 후 저장
        Member member = Member.builder()
                .memberId("member1")
                .email("test@example.com")
                .name("John Doe")
                .password("securepassword")
                .sex("M")
                .address("1234 Test Street")
                .profilePic("profilePicUrl")
                .role("USER")
                .build();
        memberRepository.save(member);

        // When: 멤버 정보 수정
        Optional<Member> optionalMember = memberRepository.findById("member1");
        assertThat(optionalMember).isPresent();
        Member existingMember = optionalMember.get();

        // 필드 값 수정
        existingMember.changeEmail("updated@example.com");
        existingMember.changeName("Jane Doe");
        existingMember.changeAddress("5678 Updated Street");
        existingMember.changeRole("ADMIN");

        // 수정된 멤버 저장
        memberRepository.save(existingMember);

        // Then: 수정된 내용 검증
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
        // Given: 멤버 생성 후 저장
        Member member = Member.builder()
                .memberId("member2")
                .email("test@example.com")
                .name("John Doe")
                .password("securepassword")
                .sex("M")
                .address("1234 Test Street")
                .profilePic("profilePicUrl")
                .role("USER")
                .build();
        memberRepository.save(member);

        // When: 멤버 삭제
        memberRepository.delete(member);

        // Then: 삭제된 멤버가 존재하지 않는지 검증
        Optional<Member> deletedMember = memberRepository.findById("member2");
        assertThat(deletedMember).isNotPresent(); // 멤버가 존재하지 않아야 함
    }

}
