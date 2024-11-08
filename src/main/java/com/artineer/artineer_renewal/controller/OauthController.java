package com.artineer.artineer_renewal.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class OauthController {
    @RequestMapping("/kakao")
    @ResponseBody
    public String kakao() {
        return "kakao";
    }

    @RequestMapping("/naver")
    public String naver() {
        return "naver";
    }

    @RequestMapping("/naverCallback")
    public String naverCallback() {
        return "naverCallback";
    }
}
