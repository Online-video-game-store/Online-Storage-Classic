package mr.demonid.web.client.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.web.client.configs.AppConfiguration;
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
    private IdnUtil idnUtil;
    private AppConfiguration config;


    /**
     * Перенос данных анонимного пользователя в его авторизированный
     * профиль.
     */
    @GetMapping("/login")
    public String login(HttpServletRequest request, HttpServletResponse response) {
        log.info("-->> login");
        Cookie anonCookie = idnUtil.getCookie(config.getCookieAnonId(), request.getCookies());
        if (anonCookie != null) {
            // Переносим данные из анонимного контекста в авторизованный
            log.info("  -- auth from {} to {}", anonCookie.getValue(), idnUtil.getUserId());
            cartServices.authUser(UUID.fromString(anonCookie.getValue()), idnUtil.getUserId());

            // Удаляем куки после успешной авторизации
            anonCookie.setMaxAge(0);
            anonCookie.setPath("/");
            response.addCookie(anonCookie);
        }
        return "redirect:/pk8000/catalog/index";
    }

//    /**
//     * Действия после выхода пользователя из профиля.
//     */
//    @GetMapping("/logout")
//    public String logout(HttpServletRequest request, HttpServletResponse response) throws ServletException {
//        request.logout();
//        // Дополнительно, можно очистить куки:
//        Cookie cookie = new Cookie("ANON_ID", null);
//        cookie.setMaxAge(0);
//        cookie.setPath("/");
//        response.addCookie(cookie);
//
//        return "redirect:/pk8000/catalog/index";
//    }

}
