package edu.example.dev_2_cc.repository;

import edu.example.dev_2_cc.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@SpringBootTest
class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

//    @Test
//    void testSaveMember() {
//        // Given: 멤버 생성
//        Member member = Member.builder()
//                .memberId("member1")
//                .email("test@example.com")
//                .name("John Doe")
//                .password("securepassword")
//                .sex("M")
//                .address("1234 Test Street")
//                .profilePic("profilePicUrl")
//                .role("USER")
//                .createdAt(LocalDateTime.now())
//                .updatedAt(LocalDateTime.now())
//                .build();
//
//        // When: 멤버를 저장
//        Member savedMember = memberRepository.save(member);
//
//        // Then: 저장된 멤버가 존재하는지 검증
//        assertThat(savedMember).isNotNull();
//        assertThat(savedMember.getMemberId()).isEqualTo("member1");
//        assertThat(savedMember.getEmail()).isEqualTo("test@example.com");
//    }

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
