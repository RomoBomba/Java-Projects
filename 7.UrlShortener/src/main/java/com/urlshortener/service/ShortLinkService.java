package com.urlshortener.service;

import com.urlshortener.dto.LinkStatsResponse;

public interface ShortLinkService {
    String createShortLink(String originalLink, String apiKey);
    String getOriginalUrl(String shortKey);
    void deleteLink(String shortKey, String apiKey);
    LinkStatsResponse getLinkStats(String shortKey, String apiKey);
    boolean shortKeyExists(String shortKey);
}
