package com.urlshortener.controller;

import com.urlshortener.dto.CreateShortLinkRequest;
import com.urlshortener.exception.AccessDeniedException;
import com.urlshortener.exception.LinkNotFoundException;
import com.urlshortener.service.ShortLinkService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class ShortLinkController {
    private final ShortLinkService service;

    public ShortLinkController(ShortLinkService service) {
        this.service = service;
    }

    @PostMapping("/shorten")
    public ResponseEntity<?> createShortLink(@RequestBody CreateShortLinkRequest request,
                                             @RequestHeader("X-API-KEY") String apiKey) {
        try {
            String shortUrl = service.createShortLink(request.getOriginalUrl(), apiKey);
            return ResponseEntity.status(HttpStatus.CREATED).body(shortUrl);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error creating short link");
        }
    }

    @GetMapping("/status/{shortKey}")
    public ResponseEntity<?> getStats(@PathVariable String shortKey,
                                      @RequestHeader("X-API-KEY") String apiKey) {
        try {
            return ResponseEntity.ok(service.getLinkStats(shortKey, apiKey));
        } catch (LinkNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @DeleteMapping("/{shortKey}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteLink(@PathVariable String shortKey,
                                        @RequestHeader("X-API-KEY") String apiKey) {
        try {
            service.deleteLink(shortKey, apiKey);
            return ResponseEntity.noContent().build();
        } catch (LinkNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

}
