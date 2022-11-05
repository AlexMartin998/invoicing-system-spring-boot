package com.alex.datajpa.app.controllers;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, @RequestParam(required = false) String logout,
            Model model, Principal principal,
            RedirectAttributes flash) {
        if (principal != null) { // ya ha hecho login
            flash.addFlashAttribute("info", "Ya ha iniciado session.");
            return "redirect:/"; // no muestra el /login si ya esta auth
        }

        if (error != null) {
            model.addAttribute("error",
                    "There was a problem logging in. Check your email and password or create an account.");
        }

        if (logout != null) {
            model.addAttribute("success",
                    "You have successfully logged out!");
        }

        return "login";
    }

}
