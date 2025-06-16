package com.urlshortener.controller;

import com.urlshortener.service.ShortLinkService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class RedirectController {
    private final ShortLinkService service;

    public RedirectController(ShortLinkService service) {
        this.service = service;
    }

    @GetMapping("/{shortKey}")
    public String redirect(@PathVariable String shortKey) {
        String originalUrl = service.getOriginalUrl(shortKey);
        return "redirect:" + originalUrl;
    }
}
