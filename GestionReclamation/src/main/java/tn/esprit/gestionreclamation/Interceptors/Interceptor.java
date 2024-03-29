package tn.esprit.gestionreclamation.Interceptors;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import tn.esprit.gestionreclamation.models.Users;
import tn.esprit.gestionreclamation.services.Authentication;
import tn.esprit.gestionreclamation.services.UserService;

import java.util.Optional;

@RequiredArgsConstructor
@Component
@Slf4j
public class Interceptor implements HandlerInterceptor {
    private final Authentication authentication;
    private final UserService userService;

    @Override
    public boolean preHandle(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            Object handler
    ){
        log.info("Running Auth Interceptor");
        log.info("Request email: {}",httpServletRequest.getHeader("email"));
        Optional<Users> user = userService.getUserByUserName(httpServletRequest.getHeader("email"));
        String authorities = httpServletRequest.getHeader("authorities");
        if(user.isEmpty()) throw new EntityNotFoundException("User Not Found");
        authentication.setName(user.get().getEmail());
        authentication.setProfile(user.get());
        authentication.setAuthorities(authorities);
        log.info("Auth username: {}", authentication.getName());
        return true;
    }

}
