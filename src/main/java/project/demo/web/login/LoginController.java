package project.demo.web.login;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import project.demo.domain.Member.Member;
import project.demo.domain.Member.MemberRepository;
import project.demo.web.filter.LoginForm;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final MemberRepository memberRepository;


    @GetMapping("login")
    public String login(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        return "login";
    }

    @PostMapping("login")
    public String loginPost(@Validated @ModelAttribute LoginForm loginForm,
                            BindingResult bindingResult, HttpServletRequest request,
                            @RequestParam(defaultValue = "/") String redirectURL) {
        if (bindingResult.hasErrors()) {
            log.info("bindingResult={}", bindingResult);
            return "login";
        }
        Member findMember = memberRepository.find(loginForm.getUserId(), loginForm.getPassword());
        log.info("findMember={}", findMember);
        if (findMember == null) {
            bindingResult.reject("discord", null, null);
            return "login";
        }

        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.C, findMember);
        return "redirect:" + redirectURL;
    }

    @GetMapping("join")
    public String join(Model model) {
        model.addAttribute("joinDTO", new JoinDTO());
        return "join";
    }

    @PostMapping("join")
    public String joinA(@Validated @ModelAttribute JoinDTO joinDTO, BindingResult bindingResult,
                        HttpServletRequest request, Model model) {
        if (bindingResult.hasErrors()) {
            log.info("bindingResult={}", bindingResult);
            return "join";
        }
        Member member = new Member(joinDTO.getUserId(), joinDTO.getPassword(), joinDTO.getUserName());
        memberRepository.save(member);
        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.C, member);
        model.addAttribute("member", member);
        return "redirect:/";
    }

    @GetMapping("/")
    public String home(@SessionAttribute(name = SessionConst.C, required = false) Member loginMember, Model model) {
        if (loginMember == null) {
            return "home";
        }
        model.addAttribute("member", loginMember);
        return "loginHome";
    }

    @PostMapping("logout")
    public String out(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return "home";
        }
        session.invalidate();
        return "home";
    }
}
