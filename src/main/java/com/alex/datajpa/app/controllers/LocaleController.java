package com.alex.datajpa.app.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;



@Controller
public class LocaleController {

    // debemos permitir a todos en  SpringSecurityConfig.java
    @GetMapping("/locale")
    public String locale(HttpServletRequest request) {
        String lastUrl = request.getHeader("referer");  // last url

        return "redirect:".concat(lastUrl);
    }

}
