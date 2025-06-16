package com.urlshortener.service;

import com.urlshortener.dto.LinkStatsResponse;
import com.urlshortener.entity.ShortLink;
import com.urlshortener.exception.AccessDeniedException;
import com.urlshortener.exception.LinkNotFoundException;
import com.urlshortener.repository.ShortLinkRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class ShortLinkServiceImpl implements ShortLinkService {
    private final ShortLinkRepository repository;

    @Value("${app.base-url}") // http://localhost:8080
    private String baseUrl;

    public ShortLinkServiceImpl(ShortLinkRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public String createShortLink(String originalLink, String apiKey) {
        if (!isValidUrl(originalLink)) throw new IllegalArgumentException("Invalid URL format");

        String shortKey = generateUniqueKey();

        ShortLink link = new ShortLink();
        link.setShortKey(shortKey);
        link.setOriginalUrl(originalLink);
        link.setCreatedAt(LocalDateTime.now());
        link.setClickCount(0L);
        link.setApiKey(apiKey);

        repository.save(link);

        return baseUrl + "/" + shortKey;
    }

    public String generateUniqueKey() {
        String key;
        int maxAttempts = 10;
        int attempts = 0;

        do {
            key = RandomStringUtils.randomAlphanumeric(6);
            attempts++;
        } while(repository.findByShortKey(key).isPresent() && attempts < maxAttempts);

        if (attempts >= maxAttempts) {
            throw new IllegalStateException("Failed to generate unique key");
        }

        return key;
    }

    private boolean isValidUrl(String url) {
        return url != null && (url.startsWith("http://") || url.startsWith("https://")) && url.length() > 10;
    }

    @Override
    @Transactional
    public String getOriginalUrl(String shortKey) {
        ShortLink link = repository.findByShortKey(shortKey)
                .orElseThrow(() -> new LinkNotFoundException("Link not found"));

        repository.incrementClickCount(shortKey, LocalDateTime.now());

        return link.getOriginalUrl();
    }

    @Override
    @Transactional(readOnly = true)
    public LinkStatsResponse getLinkStats(String shortKey, String apiKey) {
        ShortLink link = repository.findByShortKey(shortKey)
                .orElseThrow(() -> new LinkNotFoundException("Link not found"));

        if (!link.getApiKey().equals(apiKey)) {
            throw new AccessDeniedException("Access denied");
        }

        return new LinkStatsResponse(
                baseUrl + "/" + link.getShortKey(),
                link.getOriginalUrl(),
                link.getClickCount(),
                link.getCreatedAt(),
                link.getLastClickedAt()
        );
    }

    @Override
    @Transactional
    public void deleteLink(String shortKey, String apiKey) {
        ShortLink link = repository.findByShortKey(shortKey)
                .orElseThrow(() -> new LinkNotFoundException("Link not found or access denied"));

        if (!link.getApiKey().equals(apiKey)) {
            throw new AccessDeniedException("Access denied");
        }

        repository.delete(link);
    }

    @Override
    public boolean shortKeyExists(String shortKey) {
        return repository.findByShortKey(shortKey).isPresent();
    }
}
