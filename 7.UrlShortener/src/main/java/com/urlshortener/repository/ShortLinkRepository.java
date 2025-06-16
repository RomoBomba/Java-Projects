package com.urlshortener.repository;

import com.urlshortener.entity.ShortLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.Optional;

public interface ShortLinkRepository extends JpaRepository<ShortLink, Long> {
    Optional<ShortLink> findByShortKey(String shortKey);

    default void incrementClickCount(String shortKey, LocalDateTime clickTime) {
        findByShortKey(shortKey).ifPresent(link -> {
            link.setClickCount(link.getClickCount() + 1);
            link.setLastClickedAt(clickTime);
            save(link);
        });
    }

    @Modifying
    @Query("DELETE FROM ShortLink s WHERE s.shortKey = :shortKey AND s.apiKey = :apiKey")
    int deleteByShortKeyAndApiKey(@Param("shortKey") String shortKey, @Param("apiKey") String apiKey);

    void deleteByShortKey(String shortKey);
}
