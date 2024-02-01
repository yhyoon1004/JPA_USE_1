package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor //-> final인 필드값이 있으면 자동으로 생성자를 만들어줌 -> 생성자를필드당 한개씩 만드니까 @Autowired 없어도 자동 의존성 주입
public class MemberService {

//    의존성주입의 예
//    1. 필드 주입 @Autowired 필드
//    @Autowired
    private final MemberRepository memberRepository;
//    2. setter를 통한 주입
//    - 의존성 주입은 프로젝트 실행 초기에 하고 이후에는 거의 할 일이 없고, 누군가 의존성을 바꿀 수 있어서
//    @Autowired
//    public void setMemberRepository(MemberRepository memberRepository) {
//        this.memberRepository =memberRepository
//    }

//    3. 생성자 주입
//    - 생성자로 주입하는 것이기에 중간에 누가 바꿔갈 수 없다, 테스트 코드 주입 시 생성자에 원하는 의존성으로 넣어줄 수 있어 좋다.
//    - 최신의 스프링에서는 생성자가 하나밖에 없을 경우 @Autowired를 넣어주지 않아도 된다.
//    @Autowired
//    public MemberService(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

    public Long join(Member member) {
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> byName = memberRepository.findByName(member.getName());
        if (!byName.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원");
        }
    }

    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }

    @Transactional
    public void update(Long id, String name) {  //변경감[
        Member member = memberRepository.findOne(id);
        member.setName(name);
    }
}
