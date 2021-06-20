package com.url.shortner.service;

import com.url.shortner.model.URL;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DefaultUrlShortnerServiceTest {

    @Test
    void testShortenReturnsAShortenUrl() {
        UrlShortnerService shortnerService = new DefaultUrlShortnerService();
        URL url = new URL("http://www.abc.com/awxerdc123/new/new-url");

        URL shortenedUrl = shortnerService.shorten(url);

        assertEquals(url.getLongUrl(), shortenedUrl.getLongUrl());
        assertEquals("http://www.short.com//abc123x", shortenedUrl.getShortenedUrl());
    }
}
