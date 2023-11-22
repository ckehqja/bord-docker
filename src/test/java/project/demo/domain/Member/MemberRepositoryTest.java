package project.demo.domain.Member;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


class MemberRepositoryTest {

    MemberRepository memberRepository = new MemberRepository();

    @Test
    public void save() throws Exception {
        //given
        Member member1 = new Member("userA", "1111", "dobam1");
        Member member2 = new Member("userB", "2222", "dobam2");
        Member member3 = new Member("userC", "3333", "dobam3");
        //when
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        System.out.println("member1 = " + member1);
        System.out.println("member2 = " + member2);
        System.out.println("member3 = " + member3);

        List<Member> members = memberRepository.findAll();
        //then
        assertThat(members.size()).isEqualTo(3);

    }

    @Test
    public void findIdPassword() throws Exception {
        //given
        Member member1 = new Member("userA", "1111", "dobam1");
        Member member2 = new Member("userA1", "11111", "dobam2");
        Member member3 = new Member("userA2", "111211", "dobam3");
        memberRepository.save(member1);

        //when
        Member findMember = memberRepository.find("userA", "1111");
        System.out.println(findMember);
        //then
        assertThat(findMember.getId()).isEqualTo(1);

    }

    @Test
    public void test() throws Exception
    {
        //given
        System.out.println(memberRepository);
        //when

        //then

    }
}