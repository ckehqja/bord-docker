package project.demo;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import project.demo.domain.Member.Member;
import project.demo.domain.Member.MemberRepository;
import project.demo.domain.bord.Bord;
import project.demo.domain.bord.BordRepository;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class TestDataInit {
    private final MemberRepository memberRepository;
    private final BordRepository bordRepository;
    @PostConstruct
    public void init() {

        memberRepository.save(new Member("a", "a", "a"));
        memberRepository.save(new Member("b", "b", "b"));
        memberRepository.save(new Member("c", "c", "c"));


        Bord bord1 = new Bord("a", "a", "asdfasdfdsfsdffsdfdfs", 1);
        Bord bord2 = new Bord("b", "c", "asdfasdfdsfsdffsdfdfs", 2);
        Bord bord3 = new Bord("c", "c", "asdfasdfdsfsdffsdfdfs", 3);

        bord1.setLocalDateTime(LocalDateTime.now());
        bord2.setLocalDateTime(LocalDateTime.now());
        bord3.setLocalDateTime(LocalDateTime.now());

        bordRepository.save(bord1);
        bordRepository.save(bord2);
        bordRepository.save(bord3);

    }
}
