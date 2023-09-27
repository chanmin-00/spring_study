package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    @Test
    public void 회원가입() throws Exception{
        //given
        Member member = new Member();
        member.setName("Kim");
        //when
        Long saveId = memberService.join(member);
        //then
        assertThat(member).isEqualTo(memberRepository.findOne(saveId));

    }
    @Test
    public void 중복_회원_예외() throws Exception{
        Member member1 = new Member();
        member1.setName("kim1");
        Member member2 = new Member();
        member2.setName("kim1");

        memberService.join(member1);
        try {
            memberService.join(member2);
        } catch (IllegalStateException e){
            return ;
        }
        fail("예외가 발생해야 한다");
    }
}