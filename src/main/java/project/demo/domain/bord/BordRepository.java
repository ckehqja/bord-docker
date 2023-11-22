package project.demo.domain.bord;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import project.demo.domain.Member.Member;
import project.demo.domain.Member.MemberRepository;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Repository
@RequiredArgsConstructor
public class BordRepository {
    private final MemberRepository memberRepository;
    private Map<Long, Bord> store = new HashMap<>();
    private Long sequence = 0L;
    private HashMap<String, String> fileStore = new HashMap<>();


    public void save(Bord bord) {
        bord.setId(++sequence);
        bord.setLocalDateTime(LocalDateTime.now());
        store.put(bord.getId(), bord);
        log.info("save bord = {}", bord);
    }

    public void save(Bord bord, long memberId) {
        bord.setId(++sequence);
        bord.setMemberId(memberId);
        bord.setLocalDateTime(LocalDateTime.now());
        store.put(bord.getId(), bord);
        log.info("save bord = {}", bord);

    }

    public void fileNameSave(String a, String b) {
        fileStore.put(a, b);
    }

    public List<Bord> findAll() {
        return new ArrayList<>(store.values());
    }

    public Bord find(Long id) {
        return store.get(id);
    }

    public void update(Long id, Bord bord) {
        Bord findBord = find(id);
        findBord.setBody(bord.getBody());
        findBord.setTitle(bord.getTitle());
        findBord.setUserName(bord.getUserName());
        findBord.setBordType(bord.getBordType());
        findBord.setOpen(bord.getOpen());
        findBord.setLocation(bord.getLocation());
        findBord.setRegions(bord.getRegions());
        findBord.setAttachFile(bord.getAttachFile());
    }

    public void delete(Long id) {
        store.remove(id);
    }

    public String getStoreName(String originalFileName) {
        return fileStore.get(originalFileName);
    }
}
