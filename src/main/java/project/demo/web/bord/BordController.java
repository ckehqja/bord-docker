package project.demo.web.bord;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriUtils;
import project.demo.domain.Member.Member;
import project.demo.domain.bord.Bord;
import project.demo.domain.bord.BordRepository;
import project.demo.domain.bord.file.FileStore;
import project.demo.web.login.SessionConst;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BordController {

    final private BordRepository bordRepository;
    final private FileStore fileStore;

    @Value("${file.dir}")
    private String fileDir;

    @GetMapping("bords")
    public String list(Model model) {

        List<Bord> bords = bordRepository.findAll();
        model.addAttribute("bords", bords);
        return "bord/bords";
    }

    @GetMapping("bords/bord/{id}")
    public String detail( @PathVariable Long id, Model model ) {
        Bord bord = bordRepository.find(id);
        if (bord.getAttachFile() != null) {
            String storeName = bordRepository.getStoreName(bord.getAttachFile().getOriginalFilename());
            model.addAttribute("storeName", storeName);
            log.info("storeName = {}", storeName);
        }

        model.addAttribute("bord", bord);
        return "bord/bord";
    }

    @GetMapping("bords/add")
    public String addForm(Model model, HttpServletRequest request) {
        Member m = getMember(request);
        if (m == null) {
            log.info("권한이 없는 사용자 입니다.");
            return "/redirect:bords";
        }
        createBord(model, m);
        return "bord/addForm";
    }



    @PostMapping("bords/add")
    public String addFormPost(@Validated @ModelAttribute Bord bord,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes,
                              HttpServletRequest request) throws IOException {
        if (bindingResult.hasErrors()) {
            log.info("error={}", bindingResult);
            return "bord/addForm";
        }
        Member member = getMember(request);

        fileStore.storeFile(bord.getAttachFile());

        bordRepository.save(bord, member.getId());
        log.info("save bord {}", bord.getLocalDateTime());

        redirectAttributes.addAttribute("id", bord.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/bords/bord/{id}";
    }

    @ResponseBody
    @GetMapping("/images/{filename}")
    public Resource downloadImage(@PathVariable String filename) throws MalformedURLException {
        return new UrlResource("file:" + fileStore.getFullPath(filename));
    }


    @GetMapping("/attach/{id}")
    public ResponseEntity<Resource> downloadAttach(@PathVariable Long id) throws MalformedURLException {
        Bord bord = bordRepository.find(id);
        String originalFilename = bord.getAttachFile().getOriginalFilename();
        String storeName = bordRepository.getStoreName(originalFilename);
        UrlResource resource = new UrlResource("file:" + fileStore.getFullPath(storeName));

        log.info("uploadFileName={}", originalFilename);

        String encode = UriUtils.encode(originalFilename, StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + originalFilename + "\"";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);
    }


    @GetMapping("bords/edit/{id}")
    public String editForm(@PathVariable Long id, Model model,
                           RedirectAttributes redirectAttributes,
                           HttpServletRequest request) {

        Bord bord = bordRepository.find(id);
        HttpSession session = request.getSession();

        if (session == null) {
            log.info("edit no session, login go");
            return "login";
        } else {
            Member sessionMember = (Member) session.getAttribute(SessionConst.C);
            Bord findBord = bordRepository.find(id);
            if (findBord.getMemberId() != sessionMember.getId()) {
                redirectAttributes.addAttribute("id", id);
                redirectAttributes.addAttribute("authority", true);
                return "redirect:/bords/bord/{id}";
            }
        }
        model.addAttribute("bord", bord);
        return "bord/editForm";
    }

    @PostMapping("bords/edit/{id}")
    public String editFormEdit(@PathVariable Long id,
                               @Validated @ModelAttribute Bord bord,
                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.info("bindingResult={}", bindingResult);
            return "bord/edit";
        }
        bordRepository.update(id, bord);
        return "redirect:/bords/bord/{id}";
    }

    @GetMapping("delete/{id}")
    public String delete(@PathVariable Long id, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        HttpSession session = request.getSession();
        Bord bordMember = bordRepository.find(id);
        Member member = (Member) session.getAttribute(SessionConst.C);
        log.info("m.id ={}, b.id={}", member.getId(), bordMember.getMemberId());
        if (member == null || (member.getId() != bordMember.getMemberId())) {
            redirectAttributes.addAttribute("id", id);
            redirectAttributes.addAttribute("authority", true);
            return "redirect:/bords/bord/{id}";
        }
        bordRepository.delete(id);
        return "redirect:/bords";
    }

    @ModelAttribute("regions")
    public Map<String, String> regions() {
        Map<String, String> regions = new LinkedHashMap<>();
        regions.put("SEOUL", "서울");
        regions.put("BUSAN", "부산");
        regions.put("JEJU", "제주");
        return regions;
    }

    private static Member getMember(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return (Member) session.getAttribute(SessionConst.C);
    }

    private static void createBord(Model model, Member m) {
        Bord bord = new Bord();
        bord.setMemberId(m.getId());
        bord.setUserName(m.getUserName());
        model.addAttribute("bord", bord);
    }
}
