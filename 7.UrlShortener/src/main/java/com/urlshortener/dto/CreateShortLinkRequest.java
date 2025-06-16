package com.urlshortener.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateShortLinkRequest {
    private String originalUrl;
}
