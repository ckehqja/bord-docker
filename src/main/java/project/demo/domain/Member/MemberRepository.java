package project.demo.domain.Member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MemberRepository {
    Map<Long, Member> store = new ConcurrentHashMap<>();
    Long sequence = 0L;

    public void save(Member member) {
        member.setId(++sequence);
        store.put(member.getId(), member);
        log.info("save member = {}", member);
    }

    public Member find(Long id) {
        return store.get(id);
    }

    public Member find(String userid, String password) {
        List<Member> members = findAll();
        for (Member member : members) {
            if (member.getUserId().equals(userid)) {
                if (member.getPassword().equals(password)) {
                    return member;
                }
            }
            log.info("member.getPassword={}, password={}", member.getPassword(), password);
            log.info("member.getUserid={}, userid={}", member.getUserId(), userid);
        }
        return null;
    }

    public List<Member> findAll() {
        return new ArrayList<>(store.values());
    }
}
