package com.packge.manager.tosam.br.libraryApi.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Tag(name = "Logins")
public class LoginViwerController {

    @GetMapping("/login")
    public String paginaLogin() {


        return "login";


    }

    @GetMapping("/")
    @ResponseBody
    public String PaginaHome(Authentication authentication) {

        return "Olá" + authentication.getName();

    }

    @GetMapping("/authorized")
    @ResponseBody
    public String getAuthrizationCode (@RequestParam("code") String code)  {

        return "Seu Authorization code" + code;

    }



}
