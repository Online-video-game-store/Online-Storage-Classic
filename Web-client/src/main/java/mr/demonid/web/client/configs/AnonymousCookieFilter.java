package mr.demonid.web.client.configs;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import mr.demonid.web.client.utils.IdnUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Log4j2
public class AnonymousCookieFilter extends OncePerRequestFilter {

    private final IdnUtil idnUtil;

    public AnonymousCookieFilter(IdnUtil idnUtil) {
        this.idnUtil = idnUtil;
    }

    /**
     * Вставляет во входящие запросы куки, идентифицирующие
     * анонимных пользователей.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // Проверяем наличие cookie и если его нет, то создаем.
        if (idnUtil.getAnonymousId(request) == null) {
            Cookie cookie = idnUtil.setAnonymousCookie(request, response);
            log.info("-- Cookie created: {}", cookie.getValue());
//        } else {
//            log.info("-- Cookie already exists: {}", idnUtil.getAnonymousId(request));
        }

        filterChain.doFilter(request, response);
    }

}
