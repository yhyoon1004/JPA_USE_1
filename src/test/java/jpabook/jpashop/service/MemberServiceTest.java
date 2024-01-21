package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    @Rollback(value = false)
    public void 회원가입 () throws Exception{
        //given
        Member member = new Member();
        member.setName("sung");

        //when
        Long savedId = memberService.join(member);

        //then
        assertEquals(member, memberRepository.findOne(savedId));

    }

    @Test(expected = IllegalStateException.class)
    //@Test 어노테이션의 expected에 원하는 예외클래스를 넣어주면 해당 예외발생시 예외처리는 해줌
    public void 중복_회원_예외 () throws Exception{
        //given
        Member member1 = new Member();
        member1.setName("yun");
        Member member2 = new Member();
        member2.setName("yun");
        //when
        memberService.join(member1);
        memberService.join(member2);

        //then
        fail("예외 발생 코드를 확인 필요!!");
    }

}