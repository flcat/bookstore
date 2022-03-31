package shop.bookstore.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import shop.bookstore.member.Member;
import shop.bookstore.member.MemberRepository;
import shop.bookstore.member.MemberService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    public void memberJoin() throws Exception {
        //given
        Member member = new Member();
        member.setName("KwonJaeChan");

        //when
        Long saveId = memberService.join(member);

        //then
        assertEquals(member, memberRepository.findOne(saveId));
    }

    @Test(expected = IllegalStateException.class)
    public void memberDuplicationCheck() throws Exception {
        //given
        Member member1 = new Member();
        member1.setName("KwonJaeChan");
        Member member2 = new Member();
        member2.setName("KwonJaeChan");
        //when
        memberService.join(member1);
        memberService.join(member2); //예외가 발생해야 한다!!!

        //then
        fail("예외가 발생해야 한다.");
    }

}