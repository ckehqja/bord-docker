package project.demo.web.intercepter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import project.demo.domain.Member.Member;
import project.demo.web.login.SessionConst;

@Slf4j
public class EditInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            HttpSession session = request.getSession();
            log.info("1");

            if (session == null) {
                log.info("session == null");
                return false;
            }
            StringBuffer requestURL = request.getRequestURL();
            log.info("session check complete id check start, requestURL = {}", requestURL);

            String StringId="";

            if (requestURL.indexOf("?") == -1) {
                StringId = requestURL.substring(requestURL.lastIndexOf("/") + 1);
            } else {
                StringId = requestURL.substring(requestURL.lastIndexOf("/") + 1, requestURL.indexOf("?") - 1);
            }

            Integer id = Integer.valueOf(StringId);

            Member member = (Member) session.getAttribute(SessionConst.C);
            log.info("id = {} ", id);
            if (member.getId() != id) {
                log.info("id 불일치 m.id = {},  id = {}",member.getUserId(), id);
                response.sendRedirect("/bords");
                return false;
            }
        } catch (Exception e) {

        }
        return true;
    }
}
