package mr.demonid.web.client.controllers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.web.client.links.CartServiceClient;
import mr.demonid.web.client.services.CartServices;
import mr.demonid.web.client.utils.IdnUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;
import java.util.UUID;

@Controller
@Log4j2
@AllArgsConstructor
@RequestMapping("/pk8000/auth")
public class AuthController {

    private CartServices cartServices;

    private static final String COOKIE_NAME = "ANON_ID";

    /**
     * Перенос данных анонимного пользователя в его авторизированный
     * профиль.
     */
    @GetMapping("/login")
    public String login(HttpServletRequest request, HttpServletResponse response) {
        log.info("-->> login");
        Cookie anonCookie = IdnUtil.getCookie(COOKIE_NAME, request.getCookies());
        if (anonCookie != null) {
            // Переносим данные из анонимного контекста в авторизованный
            log.info("  -- auth from {} to {}", anonCookie.getValue(), IdnUtil.getUserId());
            cartServices.authUser(UUID.fromString(anonCookie.getValue()), IdnUtil.getUserId());

            // Удаляем куки после успешной авторизации
            anonCookie.setMaxAge(0);
            anonCookie.setPath("/");
            response.addCookie(anonCookie);
        }
        return "redirect:/pk8000/catalog/index";
    }

    /**
     * Действия после выхода пользователя из профиля.
     */
    @GetMapping("/logout")
    public String logout() {
        log.info("-->> logout");

        return "redirect:/pk8000/catalog/index";
    }

}
