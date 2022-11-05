package com.alex.datajpa.app.auth.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.support.SessionFlashMapManager;

@Component // poder inyectarla en SpringSecurityConfig.java
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    // Se hace asi xq se lo maneja fuera del controller, y NO se puede usar el
    // ReditectAttribute para iyectar un mesaje flash
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        SessionFlashMapManager flashMapManager = new SessionFlashMapManager();

        FlashMap flashMap = new FlashMap();
        flashMap.put("success", "Hi " + authentication.getName() + ", you have successfully logged in!");

        flashMapManager.saveOutputFlashMap(flashMap, request, response);

        if (authentication != null)
            logger.info("User: ".concat(authentication.getName()).concat(" have successfully logged in!")); // acceso al logger x herencia de SimpleUrl..

        super.onAuthenticationSuccess(request, response, authentication);
    }

}
