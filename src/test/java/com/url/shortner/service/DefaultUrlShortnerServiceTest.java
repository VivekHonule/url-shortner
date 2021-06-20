package com.url.shortner.service;

import com.url.shortner.model.URL;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class DefaultUrlShortnerServiceTest {

    @Test
    void testShortenReturnsAShortenedUrl() throws Exception {
        UrlShortnerService shortnerService = new DefaultUrlShortnerService();
        URL url = new URL("http://www.abc.com/awxerdc123/new/new-url");

        URL shortenedUrl = shortnerService.shorten(url);

        assertEquals(url.getLongUrl(), shortenedUrl.getLongUrl());
        assertEquals("http://www.short.com/0dLg6bY3", shortenedUrl.getShortenedUrl());
    }

    @Test
    void testTwoSameLongUrlsReturnSameShortenedUrls() throws Exception {
        UrlShortnerService shortnerService = new DefaultUrlShortnerService();
        URL url1 = new URL("http://www.abc.com/awxerdc123/new/new-url");
        URL url2 = new URL("http://www.abc.com/awxerdc123/new/new-url");

        URL shortenedUrl1 = shortnerService.shorten(url1);
        URL shortenedUrl2 = shortnerService.shorten(url2);

        assertEquals(shortenedUrl1.getLongUrl(), shortenedUrl2.getLongUrl());
        assertEquals(shortenedUrl1.getShortenedUrl(), shortenedUrl2.getShortenedUrl());
    }

    @Test
    void testTwoDifferentLongUrlsReturnsDifferentShortenedUrls() throws Exception {
        UrlShortnerService shortnerService = new DefaultUrlShortnerService();
        URL url1 = new URL("http://www.abc.com/awxerdc789/new/new-url1");
        URL url2 = new URL("http://www.abc.com/awxerdc123/new/new-url2");

        URL shortenedUrl1 = shortnerService.shorten(url1);
        URL shortenedUrl2 = shortnerService.shorten(url2);

        assertNotEquals(shortenedUrl1.getShortenedUrl(), shortenedUrl2.getShortenedUrl());
    }
}
