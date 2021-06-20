package com.url.shortner.service;

import com.url.shortner.model.URL;
import org.springframework.stereotype.Component;

@Component
public class DefaultUrlShortnerService implements UrlShortnerService {

    @Override
    public URL shorten(URL url) {
        return new URL(url.getLongUrl(), "http://www.short.com//abc123x");
    }
}
