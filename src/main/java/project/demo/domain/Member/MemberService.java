package project.demo.domain.Member;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public void join(Member member) {
        memberRepository.save(member);
    }

    public Member find(Long id) {
        return memberRepository.find(id);
    }

    public List<Member> findAll() {
        List<Member> members = memberRepository.findAll();
        return members;
    }

}
